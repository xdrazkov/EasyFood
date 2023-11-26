package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.ui.dialog.ViewAboutDialog;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class ViewAboutAction extends GeneralAction {

    public ViewAboutAction() {
        super("About", Icons.ABOUT_ICON);
        putValue(SHORT_DESCRIPTION, "About");
        putValue(MNEMONIC_KEY, KeyEvent.VK_B);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl B"));
    }

    @Override
    public void actionPerformedImpl(ActionEvent e) {
        JTable table = super.getTable();
        var dialog = new ViewAboutDialog();
        dialog.show(table, "About");
    }

    @Override
    protected void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "About");
    }
}
