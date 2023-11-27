package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.service.export.ImportService;
import cz.muni.fi.pv168.project.ui.panels.GeneralTablePanel;
import cz.muni.fi.pv168.project.ui.resources.Icons;
import cz.muni.fi.pv168.project.util.Filter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

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
    public void actionPerformedImpl(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        importService.getFormats().forEach(f -> fileChooser.addChoosableFileFilter(new Filter(f)));

        // TODO import strategy (delete all / add to existing)
        int dialogResult = fileChooser.showOpenDialog(recipeTablePanel);

        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File importFile = fileChooser.getSelectedFile();

            importService.importData(importFile.getAbsolutePath());

            callback.run();
            JOptionPane.showMessageDialog(recipeTablePanel, "Import was done");
        }
    }

    @Override
    protected void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "Import " + super.getCurrentTabName());
    }
}
