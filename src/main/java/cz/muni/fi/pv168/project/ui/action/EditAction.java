package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.dialog.EditCategoryDialog;
import cz.muni.fi.pv168.project.ui.dialog.EditIngredientDialog;
import cz.muni.fi.pv168.project.ui.dialog.EditRecipeDialog;
import cz.muni.fi.pv168.project.ui.dialog.EditUnitDialog;
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

        if (table.getModel() instanceof RecipeTableModel recipeTableModel) {
            int modelRow = table.convertRowIndexToModel(selectedRows[0]);
            var recipe = recipeTableModel.getEntity(modelRow);
            var dialog = new EditRecipeDialog(recipe, categories, ingredients, units);
            dialog.show(table, "Edit Recipe").ifPresent(recipeTableModel::updateRow);
        } else if (table.getModel() instanceof UnitTableModel unitTable) {
            int modelRow = table.convertRowIndexToModel(selectedRows[0]);
            var unit = unitTable.getEntity(modelRow);
            var dialog = new EditUnitDialog(unit);
            dialog.show(table, "Edit Unit").ifPresent(unitTable::updateRow);
        } else if (table.getModel() instanceof CategoryTableModel categoryTableModel) {
            int modelRow = table.convertRowIndexToModel(selectedRows[0]);
            var category = categoryTableModel.getEntity(modelRow);
            var dialog = new EditCategoryDialog(category, categoryTableModel);
            dialog.show(table, "Edit Category").ifPresent(categoryTableModel::updateRow);
        } else if (table.getModel() instanceof IngredientTableModel ingredientTableModel) {
            int modelRow = table.convertRowIndexToModel(selectedRows[0]);
            var ingredient = ingredientTableModel.getEntity(modelRow);
            var dialog = new EditIngredientDialog(ingredient, unitTableModel);
            dialog.show(table, "Edit Ingredient").ifPresent(ingredientTableModel::updateRow);
        } else {
            System.out.println("Editing different class " + table.getModel().getClass());
        }

    }

    @Override
    protected void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "Edit " + super.getCurrentTabName());
    }
}
