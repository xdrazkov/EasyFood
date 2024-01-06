package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Entity;
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

public final class EditAction extends GeneralAction {
    private final DependencyProvider dependencyProvider;

    public EditAction(DependencyProvider dependencyProvider) {
        super("Edit", Icons.EDIT_ICON);
        this.dependencyProvider = dependencyProvider;
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable table = super.getTable();
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }
        if (table.isEditing()) {
            table.getCellEditor().cancelCellEditing();
        }

        BasicTableModel model = (BasicTableModel) table.getModel();
        try {
            model.performEditAction(selectedRows, table);
        } catch (ValidationException ex) {
            EntityDialog.openErrorDialog(ex.getValidationErrors());
        } finally {
            refresh();
        }
    }

    @Override
    public void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "Edit " + getCurrentTabName());
    }
}
