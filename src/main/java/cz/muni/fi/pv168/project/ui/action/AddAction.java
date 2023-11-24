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

public final class AddAction extends GeneralAction {

    private final UnitTableModel unitTableModel;

    private final CrudService<Ingredient> ingredientCrudService;
    private final CrudService<Category> categoryCrudService;
    private final CrudService<Unit> unitCrudService;

    public AddAction(UnitTableModel unitTableModel, CrudService<Ingredient> ingredientCrudService, CrudService<Category> categoryCrudService, CrudService<Unit> unitCrudService) {
        super("Add", Icons.ADD_ICON);
        this.unitTableModel = unitTableModel;
        this.ingredientCrudService = ingredientCrudService;
        this.categoryCrudService = categoryCrudService;
        this.unitCrudService = unitCrudService;
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
        model.performAddAction(table, unitTableModel, categoryCrudService.findAll(), ingredientCrudService.findAll(), unitCrudService.findAll());
    }

    @Override
    protected void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "Add " + super.getCurrentTabName());
    }
}
