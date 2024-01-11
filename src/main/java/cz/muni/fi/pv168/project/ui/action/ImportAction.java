package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.service.export.ImportService;
import cz.muni.fi.pv168.project.service.validation.ValidationException;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
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
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        importService.getFormats().forEach(f -> fileChooser.addChoosableFileFilter(new Filter(f)));

        int dialogResult = fileChooser.showOpenDialog(recipeTablePanel);

        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File importFile = fileChooser.getSelectedFile();
            ImportStrategy importStrategy = showAppendImportDecisionDialog();
            if (importStrategy == ImportStrategy.INVALID) {
                return;
            }
            var swingWorker = getSwingWorker(importFile, importStrategy);

            swingWorker.execute();
        }
    }

    private SwingWorker<List<String>, Void> getSwingWorker(File importFile, ImportStrategy importStrategy) {
        return new SwingWorker<>() {
            @Override
            protected List<String> doInBackground() throws Exception {
                try {
                    Thread.sleep(5); // set to any number (5000) to see effect
                    importService.importData(importFile.getAbsolutePath(), importStrategy);
                } catch (ValidationException validationException) {
                    return validationException.getValidationErrors();
                }
                return List.of();
            }

            @Override
            protected void done() {
                super.done();
                List<String> validationErrors;
                try {
                    validationErrors = get();
                } catch (ExecutionException | InterruptedException ex) {
                    EntityDialog.openErrorDialog("Unexpected error during import.");
//                    EntityDialog.openErrorDialog(ex.getMessage());
                    return;
                }

                if (validationErrors.isEmpty()) {
                    JOptionPane.showMessageDialog(recipeTablePanel, "Import was done");
                } else {
                    EntityDialog.openErrorDialog(validationErrors);
                }

                callback.run();
            }
        };
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
        Object[] options = {"Replace all", "Append + skip", "Append or throw error"};
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
            case JOptionPane.NO_OPTION -> ImportStrategy.APPEND_SKIP;
            case JOptionPane.CANCEL_OPTION -> ImportStrategy.APPEND_ERROR;
            default -> ImportStrategy.INVALID;
        };
    }
}
