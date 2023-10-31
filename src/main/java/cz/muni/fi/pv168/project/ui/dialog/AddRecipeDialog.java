package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.model.ComboBoxModelAdapter;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AddRecipeDialog extends EntityDialog<Recipe> {

    private final JTextField title = new JTextField();
    private final JTextField description = new JTextField();
    private final JTextField portionCount = FieldMaker.makeIntField();
    private final JTextField instructions = new JTextField();
    private final JTextField timeToPrepare = FieldMaker.makeIntField();
    private final JComboBox<Category> category = new JComboBox<>();
    private final JLabel ingredientList = new JLabel();

    private final JPanel newIngredient = new JPanel();
    private final JComboBox<Ingredient> ingredient = new JComboBox<>();
    private final JTextField quantity = new JTextField();
    private final JComboBox<Unit> unit = new JComboBox<>();
    private final JButton setButton = new JButton();

    private final List<Category> categories;
    private final List<Ingredient> ingredients;
    private final List<Unit> units;
    private HashMap<Ingredient, Pair<Unit, Integer>> usedIngredients;

    public AddRecipeDialog(List<Category> categories, List<Ingredient> ingredients, List<Unit> units) {
        this.categories = categories;
        this.ingredients = ingredients;
        this.units = units;
        this.usedIngredients = new HashMap<>();
        setValues();
        addFields();
        addIngredients();
    }

    private void setValues() {
        title.setText("");
        description.setText("");
        portionCount.setText("1");
        instructions.setText("");
        timeToPrepare.setText("0");
        category.setModel(new javax.swing.DefaultComboBoxModel<>(categories.toArray(new Category[categories.size()])));
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
        add("Time to prepare:", timeToPrepare);
        add("Category:", category);
        panel.add(ingredientList);
        panel.add(newIngredient);
        newIngredient.add(ingredient);
        newIngredient.add(quantity);
        newIngredient.add(unit);
        newIngredient.add(setButton);
    }

    private void addIngredients(){
        for(Map.Entry<Ingredient, Pair<Unit, Integer>> ingredientPairEntry : usedIngredients.entrySet()){
            JLabel ingredient = new JLabel();
            ingredient.setText(ingredientPairEntry.getKey().toString() + " -> " + ingredientPairEntry.getValue().getValue() + ingredientPairEntry.getValue().getKey().getAbbreviation());
            panel.add(ingredient);
        }
    }

    @Override
    Recipe getEntity() {
        Recipe recipe = new Recipe();
        recipe.setTitle(title.getText());
        recipe.setDescription(description.getText());
        recipe.setPortionCount(Integer.parseInt(portionCount.getText().replaceAll(" ", "")));
        recipe.setInstructions(instructions.getText());
        recipe.setTimeToPrepare(Integer.parseInt(timeToPrepare.getText().replaceAll(" ", "")));
        recipe.setCategory((Category) category.getSelectedItem());
        recipe.setIngredientList(usedIngredients);
        return recipe;
    }
}

