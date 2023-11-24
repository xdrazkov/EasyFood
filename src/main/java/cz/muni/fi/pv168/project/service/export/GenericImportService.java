package cz.muni.fi.pv168.project.service.export;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.service.crud.CrudService;
import cz.muni.fi.pv168.project.service.export.batch.Batch;
import cz.muni.fi.pv168.project.service.export.batch.BatchImporter;
import cz.muni.fi.pv168.project.service.export.batch.BatchOperationException;
import cz.muni.fi.pv168.project.service.export.format.Format;
import cz.muni.fi.pv168.project.service.export.format.FormatMapping;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Generic synchronous implementation of the {@link ImportService}.
 */
public class GenericImportService implements ImportService {
    private final CrudService<Recipe> recipeCrudService;
    private final CrudService<Ingredient> ingredientCrudService;
    private final CrudService<Category> categoryCrudService;
    private final FormatMapping<BatchImporter> importers;
    // units are not imported, because they are all base, which ARE PART of the system

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
        var batch = getImporter(filePath).importBatch(filePath);

        recipeCrudService.deleteAll();
        ingredientCrudService.deleteAll();
        categoryCrudService.deleteAll();

        batch.recipes().forEach(this::createRecipe);
        getUniqueIngredients(batch).forEach(this::createIngredient);
        getUniqueCategories(batch).forEach(this::createCategory);
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

    private void createRecipe(Recipe recipe) {
        recipeCrudService.create(recipe)
                .intoException();
    }

    private void createIngredient(Ingredient ingredient) {
        ingredientCrudService.create(ingredient)
                .intoException();
    }

    private void createCategory(Category category) {
        categoryCrudService.create(category)
                .intoException();
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
}
