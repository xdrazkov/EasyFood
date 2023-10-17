package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class ExportAction extends GeneralAction {

    public ExportAction() {
        super("Export", Icons.EXPORT_ICON);
        putValue(SHORT_DESCRIPTION, "Export Recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_U);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl U"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO
    }

}
