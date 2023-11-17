package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.dialog.AddCategoryDialog;
import cz.muni.fi.pv168.project.ui.dialog.AddIngredientDialog;
import cz.muni.fi.pv168.project.ui.dialog.AddRecipeDialog;
import cz.muni.fi.pv168.project.ui.dialog.AddUnitDialog;
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

        BasicTableModel model = (BasicTableModel) table.getModel();
        model.performAddAction(table, unitTableModel, categories, ingredients, units);
    }

    @Override
    protected void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "Add " + super.getCurrentTabName());
    }
}
