package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.service.validation.ValidationException;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.model.BasicTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class AuthAction extends GeneralAction {

        public AuthAction() {
            super("Authenticate Admin", Icons.AUTH_ICON);
            putValue(SHORT_DESCRIPTION, "Authenticates");
            putValue(MNEMONIC_KEY, KeyEvent.VK_J);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl J"));
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
