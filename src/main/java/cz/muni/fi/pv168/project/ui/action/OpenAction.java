package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.dialog.*;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class OpenAction extends GeneralAction {

    public OpenAction() {
        super("Open", Icons.OPEN_ICON);
        putValue(SHORT_DESCRIPTION, "Open");
        putValue(MNEMONIC_KEY, KeyEvent.VK_O);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl O"));
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
        if (table.getModel() instanceof RecipeTableModel recipeTableModel) {
            int modelRow = table.convertRowIndexToModel(selectedRows[0]);
            var recipe = recipeTableModel.getEntity(modelRow);
            var dialog = new OpenRecipeDialog(recipe);
            dialog.show(table, "Open Recipe");
        } else if (table.getModel() instanceof IngredientTableModel ingredientTableModel) {
            int modelRow = table.convertRowIndexToModel(selectedRows[0]);
            var ingredient = ingredientTableModel.getEntity(modelRow);
            var dialog = new OpenIngredientDialog(ingredient);
            dialog.show(table, "Open Ingredient").ifPresent(ingredientTableModel::updateRow);
        } else if (table.getModel() instanceof CategoryTableModel categoryTableModel) {
            int modelRow = table.convertRowIndexToModel(selectedRows[0]);
            var category = categoryTableModel.getEntity(modelRow);
            var dialog = new OpenCategoryDialog(category);
            dialog.show(table, "Open Category").ifPresent(categoryTableModel::updateRow);
        }  else if (table.getModel() instanceof UnitTableModel unitTableModel) {
            int modelRow = table.convertRowIndexToModel(selectedRows[0]);
            var unit = unitTableModel.getEntity(modelRow);
            var dialog = new OpenUnitDialog(unit, unitTableModel);
            dialog.show(table, "Open Unit").ifPresent(unitTableModel::updateRow);
        }
        else {
            System.out.println("Opening different class " + table.getModel().getClass());
        }
    }

    @Override
    protected void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "Open " + super.getCurrentTabName());
    }
}
