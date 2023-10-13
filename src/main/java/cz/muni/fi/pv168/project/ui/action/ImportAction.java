package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class ImportAction extends AbstractAction {

    private final JTable recipeTable;

    public ImportAction(JTable recipeTable) {
        super("Import", Icons.IMPORT_ICON);
        this.recipeTable = recipeTable;
        putValue(SHORT_DESCRIPTION, "Imports recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO
    }
}
