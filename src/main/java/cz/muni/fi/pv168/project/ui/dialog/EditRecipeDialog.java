package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public final class EditRecipeDialog extends EntityDialog<Recipe> {

    private final JTextField title = new JTextField();
    private final JTextField description = new JTextField();
    private final JFormattedTextField portionCount = FieldMaker.makeIntField();
    private final JTextField instructions = new JTextField();
    private final JTextField timeToPrepare = FieldMaker.makeIntField();
    private final JComboBox<Category> category = new JComboBox<>();
    private final JLabel ingredientList = new JLabel();

    private final JPanel newIngredient = new JPanel();
    private final JComboBox<Ingredient> ingredient = new JComboBox<>();
    private final JTextField quantity = new JTextField();
    private final JComboBox<Unit> unit = new JComboBox<>();
    private final JButton setButton = new JButton();

    private final Recipe recipe;
    private final List<Category> categories;
    private final List<Ingredient> ingredients;
    private final List<Unit> units;

    public EditRecipeDialog(Recipe recipe, List<Category> categories, List<Ingredient> ingredients, List<Unit> units) {
        this.recipe = recipe;
        this.categories = categories;
        this.ingredients = ingredients;
        this.units = units;
        setValues();
        addFields();
        addIngredients();
    }

    private void setValues() {
        //panel.setBackground(recipe.getCategory().getColor());
        title.setText(recipe.getTitle());
        description.setText(recipe.getDescription());
        portionCount.setText(Integer.toString(recipe.getPortionCount()));
        instructions.setText(recipe.getInstructions());
        timeToPrepare.setText(Integer.toString(recipe.getTimeToPrepare()));
        category.setModel(new javax.swing.DefaultComboBoxModel<>(categories.toArray(new Category[categories.size()])));
        category.getModel().setSelectedItem(recipe.getCategory());
        ingredientList.setText("List of Ingredients:");

        newIngredient.setLayout(new MigLayout("wrap 4"));
        ingredient.setModel(new javax.swing.DefaultComboBoxModel<>(ingredients.toArray(new Ingredient[ingredients.size()])));
        unit.setModel(new javax.swing.DefaultComboBoxModel<>(units.toArray(new Unit[units.size()])));
        setButton.setText("Set");
    }

    private void addFields() {
        add("Title:", title);
        add("Description:", description);
        add("Portions:", portionCount);
        add("Instructions:", instructions);
        add("Time to prepare(min):", timeToPrepare);
        add("Category:", category);
        panel.add(ingredientList);
        panel.add(newIngredient);
        newIngredient.add(ingredient);
        newIngredient.add(quantity);
        newIngredient.add(unit);
        newIngredient.add(setButton);
    }

    private void addIngredients(){
        for(Map.Entry<Ingredient, Pair<Unit, Integer>> ingredientPairEntry : recipe.getIngredients().entrySet()){
            JLabel ingredient = new JLabel();
            ingredient.setText(ingredientPairEntry.getKey().toString() + " -> " + ingredientPairEntry.getValue().getValue() + ingredientPairEntry.getValue().getKey().getAbbreviation());
            panel.add(ingredient);
        }
    }

    @Override
    Recipe getEntity() {
        recipe.setTitle(title.getText());
        recipe.setDescription(description.getText());
        recipe.setPortionCount(Integer.parseInt(portionCount.getText().replaceAll(" ", "")));
        recipe.setInstructions(instructions.getText());
        recipe.setTimeToPrepare(Integer.parseInt(timeToPrepare.getText().replaceAll(" ", "")));
        recipe.setCategory((Category) category.getSelectedItem());
        return recipe;
    }
}

