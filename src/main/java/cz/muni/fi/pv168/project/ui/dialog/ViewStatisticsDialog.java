package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class ViewStatisticsDialog extends EntityDialog<String>  {

    private final JLabel totalNumber = new JLabel();

    private final List<Recipe> recipes;
    private final List<Ingredient> ingredients;


    public ViewStatisticsDialog(List<Recipe> recipes, List<Ingredient> ingredients) {
        this.recipes = recipes;
        this.ingredients = ingredients;
        setValues();
        addFields();
        addIngredients();
    }

    private void setValues() {
        totalNumber.setText("Total number of recipes is " + recipes.size());

    }

    private void addFields() {
        panel.add(totalNumber);
    }

    private void addIngredients(){
        for(Ingredient ingredient : ingredients){
            JLabel ingredientLabel = new JLabel();
            ingredientLabel.setText(ingredient.getName() + " is used in " + "/*?*/" + " recipes");
            panel.add(ingredientLabel);
        }
    }

    @Override
    String getEntity() {
        return "";
    }
}
