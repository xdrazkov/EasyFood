package cz.muni.fi.pv168.project.ui.dialog;

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
    }

    private void setValues() {
        title.setText("<html>" + "<B>" + recipe.getTitle() + "</B>" + "</html>");
        description.setText("<html>" + "<B>" + "Description: " + "</B> <br>" + recipe.getDescription() + " </html> ");
        portionCount.setText("<html>" + "<B>" + "Count of portions: " + "</B>" + recipe.getPortionCount() + "</html>");
        instructions.setText(recipe.getInstructions());
        instructions.setOpaque(false);
        instructions.setLineWrap(true);
        instructions.setEditable(false);
        timeToPrepare.setText("<html>" + "<B>" + "Time to prepare: " + "</B>" + recipe.getTimeToPrepare() + " mins" + " </html> ");
        category.setText("<html>" + "<B>" + "Category: " + "</B>" + recipe.getCategory().getName() + " </html> ");
        ingredientList.setText("<html>" + "<B>" + "List of ingredients:" + "</B>" + "</html>");
    }

    private void addFields() {
        add(title, THIN_HEIGHT);
        add(description, THIN_HEIGHT);
        add(portionCount, THIN_HEIGHT);
        add(timeToPrepare, THIN_HEIGHT);
        var categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.add(category);
        categoryPanel.add(new ColoredCircle(recipe.getCategory().getColor()));
        add(categoryPanel, THIN_HEIGHT);
        add(ingredientList, THIN_HEIGHT);
        addIngredients();
        add(new JLabel("<html>" + "<B>" + "Instructions:" + "</B>" + "</html>"), THIN_HEIGHT);
        add(new JScrollPane(instructions), THICC_HEIGHT);
    }

    private void addIngredients(){
        for(Map.Entry<Ingredient, Pair<Unit, Integer>> ingredientPairEntry : recipe.getIngredients().entrySet()){
            JLabel ingredient = new JLabel();
            ingredient.setText(ingredientPairEntry.getKey().toString() + " -> " + ingredientPairEntry.getValue().getValue() + " " + ingredientPairEntry.getValue().getKey().getAbbreviation());
            add(ingredient, THIN_HEIGHT);
        }
    }

    @Override
    Recipe getEntity() {
        return recipe;
    }
}

