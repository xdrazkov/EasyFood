package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.service.validation.ValidationException;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.model.BasicTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class LogoutAction extends GeneralAction {

    public LogoutAction() {
        super("Authenticate Admin", Icons.LOGOUT_ICON);
        putValue(SHORT_DESCRIPTION, "Logouts");
        putValue(MNEMONIC_KEY, KeyEvent.VK_K);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl K"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void setShortDescription() {
    }


    @Override
    public boolean accept(Object sender) {
        return super.accept(sender);
    }
}
