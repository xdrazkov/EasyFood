package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;

public final class OpenRecipeDialog extends EntityDialog<Recipe> {

    private final JLabel title = new JLabel();
    private final JLabel description = new JLabel();
    private final JLabel portionCount = new JLabel();
    private final JLabel instructions = new JLabel();
    private final JLabel timeToPrepare = new JLabel();
    private final JLabel category = new JLabel();
    private final JLabel ingredientList = new JLabel();
    private final Recipe recipe;

    public OpenRecipeDialog(Recipe recipe) {
        this.recipe = recipe;
        setValues();
        addFields();
        addIngredients();
    }

    private void setValues() {
        panel.setBackground(recipe.getCategory().getColor());
        title.setText(recipe.getTitle());
        description.setText(recipe.getDescription());
        portionCount.setText("Count of portions: " + recipe.getPortionCount());
        instructions.setText(recipe.getInstructions());
        timeToPrepare.setText("Time to prepare: " + recipe.getTimeToPrepare() + " mins");
        category.setText("Category: " + recipe.getCategory().getName());
        ingredientList.setText("List of ingredients:");
    }

    private void addFields() {
        panel.add(title);
        panel.add(description);
        panel.add(portionCount);
        panel.add(instructions);
        panel.add(timeToPrepare);
        panel.add(category);
        panel.add(ingredientList);
    }

    private void addIngredients(){
        for(Map.Entry<Ingredient, Pair<Unit, Integer>> ingredientPairEntry : recipe.getIngredientList().entrySet()){
            JLabel ingredient = new JLabel();
            ingredient.setText(ingredientPairEntry.getKey().toString() + " -> " + ingredientPairEntry.getValue().getValue() + ingredientPairEntry.getValue().getKey().getAbbreviation());
            panel.add(ingredient);
        }
    }

    @Override
    Recipe getEntity() {
        return recipe;
    }
}

