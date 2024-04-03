package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.service.validation.ValidationException;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.model.BasicTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class LoginAction extends GeneralAction {

    public LoginAction() {
        super("Authenticate Admin", Icons.LOGIN_ICON);
        putValue(SHORT_DESCRIPTION, "Logins");
        putValue(MNEMONIC_KEY, KeyEvent.VK_J);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl J"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("login");
    }

    @Override
    public void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "Login");
    }
}
