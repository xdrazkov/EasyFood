package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.service.export.ImportService;
import cz.muni.fi.pv168.project.service.validation.ValidationException;
import cz.muni.fi.pv168.project.ui.panels.GeneralTablePanel;
import cz.muni.fi.pv168.project.ui.resources.Icons;
import cz.muni.fi.pv168.project.util.Filter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class ImportAction extends GeneralAction {
    private final GeneralTablePanel recipeTablePanel;
    private final ImportService importService;
    private final Runnable callback;

    public ImportAction(GeneralTablePanel recipeTablePanel, ImportService importService, Runnable callback) {
        super("Import", Icons.IMPORT_ICON);
        this.recipeTablePanel = recipeTablePanel;
        this.importService = importService;
        this.callback = callback;
        putValue(SHORT_DESCRIPTION, "Import recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    protected void actionPerformedImpl(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        importService.getFormats().forEach(f -> fileChooser.addChoosableFileFilter(new Filter(f)));

        int dialogResult = fileChooser.showOpenDialog(recipeTablePanel);

        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File importFile = fileChooser.getSelectedFile();
            ImportStrategy importStrategy = showAppendImportDecisionDialog();
            SwingWorker<List<String>, File> swingWorker = getSwingWorker(importFile, importStrategy);
            try {
                var validationErrors = swingWorker.get();
                if (!validationErrors.isEmpty()) {
                    throw new ValidationException("", validationErrors);
                }
            } catch (InterruptedException |  ExecutionException ex) {
                throw new RuntimeException(ex);
            }

            JOptionPane.showMessageDialog(recipeTablePanel, "Import was done");
        }
    }

    private SwingWorker<List<String>, File> getSwingWorker(File importFile, ImportStrategy importStrategy) {
        SwingWorker<List<String>, File> swingWorker = new SwingWorker<>() {
            @Override
            protected List<String> doInBackground() {
                List<String> validationErrors = List.of();
                try {
                    importService.importData(importFile.getAbsolutePath(), importStrategy);
                } catch (ValidationException e) {
                    validationErrors = e.getValidationErrors();
                }
                callback.run();
                return validationErrors;
            }

        };
        swingWorker.execute();
        return swingWorker;
    }

    @Override
    protected String getCurrentTabName() {
        return generalTablePanel.getTablePanelType().getPluralName();
    }

    @Override
    public void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "Import " + getCurrentTabName());
    }

    private static ImportStrategy showAppendImportDecisionDialog() {
        Object[] options = {"Replace all", "Append + replace", "Append or throw error"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Select import strategy option",
                "Import Strategy Decision",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        return switch (choice) {
            case JOptionPane.YES_OPTION -> ImportStrategy.REPLACE_ALL;
            case JOptionPane.NO_OPTION -> ImportStrategy.APPEND_REPLACE;
            case JOptionPane.CANCEL_OPTION -> ImportStrategy.APPEND_ERROR;
            default -> ImportStrategy.INVALID;
        };
    }
}
