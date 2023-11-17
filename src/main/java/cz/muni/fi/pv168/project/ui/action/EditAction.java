package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.dialog.EditCategoryDialog;
import cz.muni.fi.pv168.project.ui.dialog.EditIngredientDialog;
import cz.muni.fi.pv168.project.ui.dialog.EditRecipeDialog;
import cz.muni.fi.pv168.project.ui.dialog.EditUnitDialog;
import cz.muni.fi.pv168.project.ui.model.BasicTableModel;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public final class EditAction extends GeneralAction {

    private final List<Category> categories;
    private final List<Ingredient> ingredients;
    private final List<Unit> units;
    private final UnitTableModel unitTableModel;

    public EditAction(List<Category> categories, List<Ingredient> ingredients, List<Unit> units, UnitTableModel unitTableModel) {
        super("Edit", Icons.EDIT_ICON);
        this.categories = categories;
        this.ingredients = ingredients;
        this.units = units;
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
        model.performEditAction(selectedRows, table, unitTableModel, categories, ingredients, units);
    }

    @Override
    protected void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "Edit " + super.getCurrentTabName());
    }
}
