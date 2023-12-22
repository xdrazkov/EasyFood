package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Entity;
import cz.muni.fi.pv168.project.service.export.DataManipulationException;
import cz.muni.fi.pv168.project.service.export.ExportService;
import cz.muni.fi.pv168.project.service.export.batch.BatchOperationException;
import cz.muni.fi.pv168.project.service.validation.ValidationException;
import cz.muni.fi.pv168.project.ui.resources.Icons;
import cz.muni.fi.pv168.project.util.Filter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public final class ExportAction extends GeneralAction {

    private final Component parent;
    private final ExportService exportService;

    public ExportAction(Component parent, ExportService exportService) {
        super("Export", Icons.EXPORT_ICON);
        this.parent = Objects.requireNonNull(parent);
        this.exportService = exportService;

        putValue(SHORT_DESCRIPTION, "Exports employees to a file");
        putValue(MNEMONIC_KEY, KeyEvent.VK_X);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl X"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        exportService.getFormats().forEach(f -> fileChooser.addChoosableFileFilter(new Filter(f)));

        int dialogResult = fileChooser.showSaveDialog(parent);
        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            String exportFile = fileChooser.getSelectedFile().getAbsolutePath();
            var filter = fileChooser.getFileFilter();
            if (filter instanceof Filter) {
                exportFile = ((Filter) filter).decorate(exportFile);
            }

            try {
                SwingWorker<List<String>, String> swingWorker = getSwingWorker(exportFile);
                swingWorker.execute();
                var  validationErrors = swingWorker.get();
                if (!validationErrors.isEmpty()) {
                    throw new ValidationException("", validationErrors);
                }
            } catch (DataManipulationException | BatchOperationException| ExecutionException | InterruptedException ex) {
                JOptionPane.showMessageDialog(parent, "Export has successfully failed.\n" + ex.getMessage());
                return;
            }
            JOptionPane.showMessageDialog(parent, "Export has successfully finished.");
        }
    }

    private SwingWorker<List<String>, String> getSwingWorker(String exportFile) {
        SwingWorker<List<String>, String> swingWorker = new SwingWorker<>() {
            @Override
            protected List<String> doInBackground() {
                List<String> validationErrors = List.of();
                try {
                    var exportGuids = ExportAction.super.getSelectedEntities().stream().map(Entity::getGuid).toList();
                    exportService.exportData(exportFile, exportGuids);
                } catch (ValidationException e) {
                    validationErrors = e.getValidationErrors();
                }
                return validationErrors;
            }
        };
        swingWorker.execute();
        return swingWorker;
    }

    @Override
    public void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "Export " + getCurrentTabName());
    }
}
