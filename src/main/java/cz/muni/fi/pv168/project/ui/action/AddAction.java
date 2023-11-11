package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.dialog.AddCategoryDialog;
import cz.muni.fi.pv168.project.ui.dialog.AddIngredientDialog;
import cz.muni.fi.pv168.project.ui.dialog.AddRecipeDialog;
import cz.muni.fi.pv168.project.ui.dialog.AddUnitDialog;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public final class AddAction extends GeneralAction {

    private final List<Category> categories;

    private final List<Ingredient> ingredients;
    private final List<Unit> units;
    private final UnitTableModel unitTableModel;

    public AddAction(List<Category> categories, List<Ingredient> ingredients, List<Unit> units, UnitTableModel unitTableModel) {
        super("Add", Icons.ADD_ICON);
        this.categories = categories;
        this.ingredients = ingredients;
        this.units = units;
        this.unitTableModel = unitTableModel;
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

        if (table.getModel() instanceof RecipeTableModel recipeTableModel) {
            var dialog = new AddRecipeDialog(categories, ingredients, units);
            dialog.show(table, "Add Recipe").ifPresent(recipeTableModel::addRow);
        } else if (table.getModel() instanceof UnitTableModel unitTable) {
            var dialog = new AddUnitDialog();
            dialog.show(table, "Add Unit").ifPresent(unitTable::addRow);
        } else if (table.getModel() instanceof CategoryTableModel categoryTableModel) {
            var dialog = new AddCategoryDialog(categoryTableModel);
            dialog.show(table, "Add Category").ifPresent(categoryTableModel::addRow);
        } else if (table.getModel() instanceof IngredientTableModel ingredientTableModel) {
            var dialog = new AddIngredientDialog(unitTableModel);
            dialog.show(table, "Add Ingredient").ifPresent(ingredientTableModel::addRow);
        } else {
            System.out.println("Editing different class " + table.getModel().getClass());
        }
    }

    @Override
    protected void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "Add " + super.getCurrentTabName());
    }
}
