package cz.muni.fi.pv168.project.service.export;

import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.service.export.batch.Batch;
import cz.muni.fi.pv168.project.service.export.batch.BatchExporter;
import cz.muni.fi.pv168.project.service.export.batch.BatchOperationException;
import cz.muni.fi.pv168.project.service.export.format.Format;
import cz.muni.fi.pv168.project.service.export.format.FormatMapping;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.panels.TablePanel;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generic synchronous implementation of the {@link ExportService}
 */
public class GenericExportService implements ExportService {

    private final FormatMapping<BatchExporter> exporters;

    private final RowSorter<RecipeTableModel> recipeRowSorter;

    private final TablePanel tablePanel;

    public GenericExportService(RowSorter<RecipeTableModel> recipeRowSorter, TablePanel tablePanel, Collection<BatchExporter> exporters) {
        this.exporters = new FormatMapping<>(exporters);
        this.recipeRowSorter = recipeRowSorter;
        this.tablePanel = tablePanel;
    }

    @Override
    public Collection<Format> getFormats() {
        return exporters.getFormats();
    }

    @Override
    public void exportData(String filePath) {
        var exporter = getExporter(filePath);

        List<Recipe> selectedRecipes = Arrays.stream(tablePanel.getTable().getSelectedRows())
                .mapToObj(row -> recipeRowSorter.getModel().getEntity(recipeRowSorter.convertRowIndexToModel(row)))
                .collect(Collectors.toList());


        var batch = new Batch(selectedRecipes);
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
