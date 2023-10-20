package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.ui.dialog.OpenCategoryDialog;
import cz.muni.fi.pv168.project.ui.dialog.OpenIngredientDialog;
import cz.muni.fi.pv168.project.ui.dialog.OpenRecipeDialog;
import cz.muni.fi.pv168.project.ui.dialog.OpenUnitDialog;
import cz.muni.fi.pv168.project.ui.dialog.ViewStatisticsDialog;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class ViewStatisticsAction extends GeneralAction {

    private final List<Recipe> recipes;
    private final List<Ingredient> ingredients;

    public ViewStatisticsAction(List<Recipe> recipes, List<Ingredient> ingredients) {
        super("Statistics", Icons.STATISTICS_ICON);
        this.recipes = recipes;
        this.ingredients = ingredients;
        putValue(SHORT_DESCRIPTION, "Statistics");
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable table = super.getTable();
        var dialog = new ViewStatisticsDialog(recipes, ingredients);
        dialog.show(table, "View Statistics");
    }

    @Override
    protected void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "View Statistics");
    }
}
