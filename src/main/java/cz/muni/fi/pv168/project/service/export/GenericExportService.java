package cz.muni.fi.pv168.project.service.export;

import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.service.crud.CrudService;
import cz.muni.fi.pv168.project.service.export.batch.Batch;
import cz.muni.fi.pv168.project.service.export.batch.BatchExporter;
import cz.muni.fi.pv168.project.service.export.batch.BatchOperationException;
import cz.muni.fi.pv168.project.service.export.format.Format;
import cz.muni.fi.pv168.project.service.export.format.FormatMapping;

import java.util.Collection;

/**
 * Generic synchronous implementation of the {@link ExportService}
 */
public class GenericExportService implements ExportService {
    private final FormatMapping<BatchExporter> exporters;
    private final CrudService<Recipe> recipeCrudService;

    public GenericExportService(Collection<BatchExporter> exporters, CrudService<Recipe> recipeCrudService) {
        this.exporters = new FormatMapping<>(exporters);
        this.recipeCrudService = recipeCrudService;
    }

    @Override
    public Collection<Format> getFormats() {
        return exporters.getFormats();
    }

    @Override
    public void exportData(String filePath, Collection<String> guids) {
        var exporter = getExporter(filePath);
        var batch = new Batch(guids.stream().map(guid -> recipeCrudService.findByGuid(guid).get()).toList());
        exporter.exportBatch(batch, filePath);
    }

    private BatchExporter getExporter(String filePath) {
        var extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        var importer = exporters.findByExtension(extension);
        if (importer == null)
            throw new BatchOperationException("Extension %s has no registered formatter".formatted(extension));
        return importer;
    }
}
