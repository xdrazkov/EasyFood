package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.dialog.AddRecipeDialog;
import cz.muni.fi.pv168.project.ui.dialog.EditRecipeDialog;
import cz.muni.fi.pv168.project.ui.dialog.OpenRecipeDialog;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import javax.swing.*;
import java.util.List;

public class RecipeTableModel extends BasicTableModel<Recipe> {
    public RecipeTableModel(DependencyProvider dependencyProvider) {
        super(dependencyProvider, dependencyProvider.getRecipeValidator(), dependencyProvider.getRecipeCrudService());
    }

    public List<Column<Recipe, ?>> makeColumns() {
        return List.of(
                Column.readonly("Title", String.class, Recipe::getTitle),
                Column.readonly("Description", String.class, Recipe::getDescription),
                Column.readonly("Category", Category.class, Recipe::getCategory),
                Column.readonly("Preparation time (min)", Integer.class, Recipe::getTimeToPrepare),
                Column.readonly("Nutritional value (kcal)", Integer.class, Recipe::getNutritionalValue)
        );
    }

    @Override
    public void performAddAction(JTable table) {
        RecipeTableModel recipeTableModel = (RecipeTableModel) table.getModel();
        var dialog = new AddRecipeDialog(dependencyProvider, entityValidator);
        dialog.show(table, "Add Recipe").ifPresent(recipeTableModel::addRow);
    }

    @Override
    public void performEditAction(int[] selectedRows, JTable table) {
        int modelRow = table.convertRowIndexToModel(selectedRows[0]);
        RecipeTableModel recipeTableModel = (RecipeTableModel) table.getModel();
        var recipe = recipeTableModel.getEntity(modelRow);
        var dialog = new EditRecipeDialog(recipe.deepClone(), dependencyProvider, entityValidator);
        var optional = dialog.show(table, "Edit Recipe");
        setAndUpdate(optional, recipe);
    }

    @Override
    public void performOpenAction(JTable table, int modelRow) {
        RecipeTableModel recipeTableModel = (RecipeTableModel) table.getModel();
        var recipe = recipeTableModel.getEntity(modelRow);
        var dialog = new OpenRecipeDialog(recipe);
        dialog.show(table, "Open Recipe");
    }
}
