package cz.muni.fi.pv168.project.service.export;

import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.service.crud.CrudService;
import cz.muni.fi.pv168.project.service.export.batch.BatchImporter;
import cz.muni.fi.pv168.project.service.export.batch.BatchOperationException;
import cz.muni.fi.pv168.project.service.export.format.Format;
import cz.muni.fi.pv168.project.service.export.format.FormatMapping;

import java.util.Collection;

/**
 * Generic synchronous implementation of the {@link ImportService}.
 */
public class GenericImportService implements ImportService {
    // TODO all CRUD services
    private final CrudService<Recipe> recipeCrudService;
    private final FormatMapping<BatchImporter> importers;

    public GenericImportService(
            CrudService<Recipe> recipeCrudService, Collection<BatchImporter> importers
    ) {
        this.recipeCrudService = recipeCrudService;
        this.importers = new FormatMapping<>(importers);
    }

    @Override
    public void importData(String filePath) {
        var batch = getImporter(filePath).importBatch(filePath);

        recipeCrudService.deleteAll();
        batch.recipes().forEach(this::createRecipe);
    }


    // TODO other categories
    private void createRecipe(Recipe recipe) {
        recipeCrudService.create(recipe)
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
