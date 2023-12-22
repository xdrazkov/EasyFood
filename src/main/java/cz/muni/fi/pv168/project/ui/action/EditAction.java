package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.service.crud.CrudService;
import cz.muni.fi.pv168.project.ui.model.BasicTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class EditAction extends GeneralAction {
    private final UnitTableModel unitTableModel;
    private final CrudService<Ingredient> ingredientCrudService;
    private final CrudService<Category> categoryCrudService;
    private final CrudService<Unit> unitCrudService;

    public EditAction(UnitTableModel unitTableModel, CrudService<Ingredient> ingredientCrudService, CrudService<Category> categoryCrudService, CrudService<Unit> unitCrudService) {
        super("Edit", Icons.EDIT_ICON);
        this.ingredientCrudService = ingredientCrudService;
        this.categoryCrudService = categoryCrudService;
        this.unitCrudService = unitCrudService;
        this.unitTableModel = unitTableModel;
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
        model.performEditAction(selectedRows, table, unitTableModel, categoryCrudService.findAll(), ingredientCrudService.findAll(), unitCrudService.findAll());
        refresh();
    }

    @Override
    public void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "Edit " + getCurrentTabName());
    }
}
