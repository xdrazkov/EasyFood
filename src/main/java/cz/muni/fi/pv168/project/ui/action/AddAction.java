package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.service.crud.CrudService;
import cz.muni.fi.pv168.project.service.validation.ValidationException;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.model.BasicTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class AddAction extends GeneralAction {

    public AddAction() {
        super("Add", Icons.ADD_ICON);
        putValue(SHORT_DESCRIPTION, "Adds new");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable table = super.getTable();
        if (table.isEditing()) {
            table.getCellEditor().cancelCellEditing();
        }

        BasicTableModel model = (BasicTableModel) table.getModel();
        try {
            model.performAddAction(table);
        } catch (ValidationException ex) {
            EntityDialog.openErrorDialog(ex.getValidationErrors());
        } finally {
            refresh();
        }
    }

    @Override
    protected String getCurrentTabName() {
        return generalTablePanel.getTablePanelType().getSingularName();
    }

    @Override
    public void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "Add " + getCurrentTabName());
    }
}
