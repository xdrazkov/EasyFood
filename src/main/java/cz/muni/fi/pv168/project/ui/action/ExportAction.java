package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class ExportAction extends AbstractAction {

    private final JTable recipeTable;

    public ExportAction(JTable recipeTable) {
        super("Export", Icons.EXPORT_ICON);
        this.recipeTable = recipeTable;
        putValue(SHORT_DESCRIPTION, "Exports recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_U);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl U"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO
    }
}
