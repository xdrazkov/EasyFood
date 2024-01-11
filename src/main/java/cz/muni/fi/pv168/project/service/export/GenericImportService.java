package cz.muni.fi.pv168.project.service.export;

import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.service.crud.CrudService;
import cz.muni.fi.pv168.project.service.export.batch.Batch;
import cz.muni.fi.pv168.project.service.export.batch.BatchImporter;
import cz.muni.fi.pv168.project.service.export.batch.BatchOperationException;
import cz.muni.fi.pv168.project.service.export.format.Format;
import cz.muni.fi.pv168.project.service.export.format.FormatMapping;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionExecutor;
import cz.muni.fi.pv168.project.ui.action.ImportStrategy;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generic synchronous implementation of the {@link ImportService}.
 */
public class GenericImportService implements ImportService {
    private final CrudService<Recipe> recipeCrudService;
    private final CrudService<Ingredient> ingredientCrudService;
    private final CrudService<Category> categoryCrudService;
    private final FormatMapping<BatchImporter> importers;
    private final TransactionExecutor transactionExecutor;
    // units are not imported, because all imported units are base, which ARE PART of the system


    public GenericImportService(DependencyProvider dependencyProvider, Collection<BatchImporter> importers, TransactionExecutor transactionExecutor) {
        this.recipeCrudService = dependencyProvider.getRecipeCrudService();
        this.ingredientCrudService = dependencyProvider.getIngredientCrudService();
        this.categoryCrudService = dependencyProvider.getCategoryCrudService();
        this.importers = new FormatMapping<>(importers);
        this.transactionExecutor = transactionExecutor;
    }

    @Override
    public void importData(String filePath) {
        importData(filePath, ImportStrategy.REPLACE_ALL);
    }

    @Override
    public void importData(String filePath, ImportStrategy importStrategy) {
        var batch = getImporter(filePath).importBatch(filePath);

        switch (importStrategy) {
            case REPLACE_ALL -> transactionExecutor.executeInTransaction(() -> removeAllImport(batch));
            case APPEND_SKIP -> transactionExecutor.executeInTransaction(() -> appendSkipImport(batch));
            case APPEND_ERROR -> transactionExecutor.executeInTransaction(() ->appendErrorImport(batch));
        }
    }

    private void removeAllImport(Batch batch) {
        recipeCrudService.deleteAll();
        for (Category category: categoryCrudService.findAll()) {
            if (! CategoryTableModel.isDefaultCategory(category)) {
                categoryCrudService.deleteByGuid(category.getGuid());
            }
        }
        ingredientCrudService.deleteAll();

        appendSkipImport(batch);
    }

    /** if in memory same entity, fails */
    private void appendErrorImport(Batch batch) {
        getUniqueIngredients(batch).forEach(e -> createEntity(e, ingredientCrudService));
        getUniqueCategories(batch).forEach(e -> createEntity(e, categoryCrudService));
        replaceInvalidEntitiesInRecipe(batch).forEach(e -> createEntity(e, recipeCrudService));
    }

    /** if in memory same entity, continues */
    private void appendSkipImport(Batch batch) {
        appendSkipEntities(ingredientCrudService, getUniqueIngredients(batch));
        appendSkipEntities(categoryCrudService, getUniqueCategories(batch));
        appendSkipEntities(recipeCrudService, replaceInvalidEntitiesInRecipe(batch));
    }

    private static <E extends Entity> void appendSkipEntities(CrudService<E> crudService, Collection<E> appendCollection) {
        var inMemoryRecipes = crudService.findAll();
        for (var entity: appendCollection) {
            var equivalentEntity = getEquivalentEntity(entity, inMemoryRecipes);
            if (equivalentEntity == null) {
                createEntity(entity, crudService);
            }
        }
    }

    private static Collection<Category> getUniqueCategories(Batch batch) {
        return batch.recipes().stream()
                .map(Recipe::getCategory)
                .collect(Collectors.toSet());
    }

    private static Collection<Ingredient> getUniqueIngredients(Batch batch) {
        return batch.recipes().stream()
                .flatMap(recipe -> recipe.getIngredients().keySet().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<Format> getFormats() {
        return importers.getFormats();
    }

    private BatchImporter getImporter(String filePath) {
        var extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        var importer = importers.findByExtension(extension);
        if (importer == null) {
            throw new BatchOperationException("Extension %s has no registered formatter".formatted(extension));
        }

        return importer;
    }

    private static <E extends Entity> void createEntity(E entity, CrudService<E> crudService) {
        crudService.create(entity).intoException();
    }
    private static <E extends Entity> E getEquivalentEntity(E entity, Collection<E> collection) {
        return collection.stream().filter(o -> o.equals(entity)).findFirst().orElse(null);
    }

    private Collection<Recipe> replaceInvalidEntitiesInRecipe(Batch batch) {
        // all ingredients and categories in recipes ARE already in crud service
        Collection<Category> categories = categoryCrudService.findAll();
        Collection<Ingredient> ingredients = ingredientCrudService.findAll();

        for (Recipe recipe: batch.recipes()) {
            Category category = recipe.getCategory();
            Category inMemoryCategory = getEquivalentEntity(category, categories);
            recipe.setCategory(inMemoryCategory);

            HashMap<Ingredient, AmountInUnit> newIngredients = new HashMap<>();
            for (Map.Entry<Ingredient, AmountInUnit> entry: recipe.getIngredients().entrySet()) {
                Ingredient inMemoryIngredient = getEquivalentEntity(entry.getKey(), ingredients);
                newIngredients.put(inMemoryIngredient, entry.getValue());
            }

            recipe.setIngredients(newIngredients);
        }

        return batch.recipes();
    }
}
