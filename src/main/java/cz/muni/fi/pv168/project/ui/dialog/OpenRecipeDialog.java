package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.AmountInUnit;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.ColoredCircle;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public final class OpenRecipeDialog extends EntityDialog<Recipe> {

    private final JLabel title = new JLabel();
    private final JLabel description = new JLabel();
    private final JLabel portionCount = new JLabel();
    private final JTextArea instructions = new JTextArea();
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
        panel.setPreferredSize(new Dimension(330, 560));
        title.setText(recipe.getTitle());
        description.setText(recipe.getDescription());
        portionCount.setText("Count of portions: " + recipe.getPortionCount());
        instructions.setText(recipe.getInstructions());
        instructions.setEditable(false);
        instructions.setCursor(null);
        instructions.setOpaque(false);
        //instructions.setFocusable(true);


        timeToPrepare.setText("Time to prepare: " + recipe.getTimeToPrepare() + " mins");
        category.setText("Category: " + recipe.getCategory().getName() + " ");
        ingredientList.setText("List of ingredients:");
    }

    private void addFields() {
        panel.add(title);
        panel.add(description);
        panel.add(portionCount);
        panel.add(instructions);
        panel.add(timeToPrepare);
        var categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridBagLayout());
        categoryPanel.add(category);
        categoryPanel.add(new ColoredCircle(recipe.getCategory().getColor()));
        panel.add(categoryPanel);
        panel.add(ingredientList);
    }

    private void addIngredients(){
        for(Map.Entry<Ingredient, AmountInUnit> ingredientPairEntry : recipe.getIngredients().entrySet()){
            JLabel ingredient = new JLabel();
            ingredient.setText(ingredientPairEntry.getKey().toString() + " -> " + ingredientPairEntry.getValue().getAmount() + " " + ingredientPairEntry.getValue().getUnit().getAbbreviation());
            panel.add(ingredient);
        }
    }

    @Override
    Recipe getEntity() {
        return recipe;
    }
}

