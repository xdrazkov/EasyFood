package cz.muni.fi.pv168.project.service.export;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Entity;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.service.crud.CrudService;
import cz.muni.fi.pv168.project.service.export.batch.Batch;
import cz.muni.fi.pv168.project.service.export.batch.BatchImporter;
import cz.muni.fi.pv168.project.service.export.batch.BatchOperationException;
import cz.muni.fi.pv168.project.service.export.format.Format;
import cz.muni.fi.pv168.project.service.export.format.FormatMapping;
import cz.muni.fi.pv168.project.ui.action.ImportStrategy;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Generic synchronous implementation of the {@link ImportService}.
 */
public class GenericImportService implements ImportService {
    private final CrudService<Recipe> recipeCrudService;
    private final CrudService<Ingredient> ingredientCrudService;
    private final CrudService<Category> categoryCrudService;
    private final FormatMapping<BatchImporter> importers;
    // units are not imported, because all imported units are base, which ARE PART of the system

    public GenericImportService(
            CrudService<Recipe> recipeCrudService,
            CrudService<Ingredient> ingredientCrudService,
            CrudService<Category> categoryCrudService,
            Collection<BatchImporter> importers
    ) {
        this.recipeCrudService = recipeCrudService;
        this.ingredientCrudService = ingredientCrudService;
        this.categoryCrudService = categoryCrudService;
        this.importers = new FormatMapping<>(importers);
    }

    @Override
    public void importData(String filePath) {
        importData(filePath, ImportStrategy.REPLACE_ALL);
    }

    @Override
    public void importData(String filePath, ImportStrategy importStrategy) {
        var batch = getImporter(filePath).importBatch(filePath);

        switch (importStrategy) {
            case REPLACE_ALL -> removeAllImport(batch);
            case APPEND_REPLACE -> appendSkipImport(batch);
            case APPEND_ERROR -> appendErrorImport(batch);
        }
    }

    private void removeAllImport(Batch batch) {
        categoryCrudService.deleteAll();
        ingredientCrudService.deleteAll();
        recipeCrudService.deleteAll();

        appendErrorImport(batch);
    }

    /** if in memory same entity, fails */
    private void appendErrorImport(Batch batch) {
        batch.recipes().forEach(e -> createEntity(e, recipeCrudService));
        getUniqueIngredients(batch).forEach(e -> createEntity(e, ingredientCrudService));
        getUniqueCategories(batch).forEach(e -> createEntity(e, categoryCrudService));
    }

    /** if in memory same entity, continues */
    private void appendSkipImport(Batch batch) {
        appendSkipEntities(recipeCrudService, batch.recipes());
        appendSkipEntities(ingredientCrudService, getUniqueIngredients(batch));
        appendSkipEntities(categoryCrudService, getUniqueCategories(batch));
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
}
