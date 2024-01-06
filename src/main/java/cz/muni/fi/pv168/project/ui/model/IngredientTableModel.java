package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.dialog.AddIngredientDialog;
import cz.muni.fi.pv168.project.ui.dialog.EditIngredientDialog;
import cz.muni.fi.pv168.project.ui.dialog.OpenIngredientDialog;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import javax.swing.*;
import java.util.List;

public class IngredientTableModel extends BasicTableModel<Ingredient> {
    public IngredientTableModel(DependencyProvider dependencyProvider) {
        super(dependencyProvider, dependencyProvider.getIngredientValidator(), dependencyProvider.getIngredientCrudService());
    }

    public List<Column<Ingredient, ?>> makeColumns() {
        return List.of(
                Column.readonly("Name", String.class, Ingredient::getName),
                Column.readonly("Default unit", Unit.class, Ingredient::getDefaultUnit),
                Column.readonly("Nutritional value (kcal)", Float.class, Ingredient::getCaloriesPerUnit),
                Column.readonly("Used in * recipe(s))", Integer.class, ingredient -> ingredient.countInstances(dependencyProvider.getRecipeCrudService().findAll()))
        );
    }

    @Override
    public void performAddAction(JTable table) {
        IngredientTableModel ingredientTableModel = (IngredientTableModel) table.getModel();
        var dialog = new AddIngredientDialog(dependencyProvider, entityValidator);
        dialog.show(table, "Add Ingredient").ifPresent(ingredientTableModel::addRow);
    }

    @Override
    public void performEditAction(int[] selectedRows, JTable table) {
        int modelRow = table.convertRowIndexToModel(selectedRows[0]);
        IngredientTableModel ingredientTableModel = (IngredientTableModel) table.getModel();
        var ingredient = ingredientTableModel.getEntity(modelRow);
        var dialog = new EditIngredientDialog(ingredient.deepClone(), dependencyProvider, entityValidator);
        var optional = dialog.show(table, "Edit Ingredient");
        setAndUpdate(optional, ingredient);
    }

    @Override
    public void performOpenAction(JTable table, int modelRow) {
        IngredientTableModel ingredientTableModel = (IngredientTableModel) table.getModel();
        var ingredient = ingredientTableModel.getEntity(modelRow);
        var dialog = new OpenIngredientDialog(ingredient);
        dialog.show(table, "Open Ingredient").ifPresent(ingredientTableModel::updateRow);
    }
}
