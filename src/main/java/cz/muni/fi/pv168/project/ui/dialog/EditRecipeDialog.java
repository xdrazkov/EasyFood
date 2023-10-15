package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.ui.model.ComboBoxModelAdapter;

import javax.swing.*;
import java.util.List;

public final class EditRecipeDialog extends EntityDialog<Recipe> {

    private final JTextField title = new JTextField();
    private final JTextField description = new JTextField();
    private final JTextField portionCount = new JTextField();
    private final JTextField instructions = new JTextField();
    private final JTextField timeToPrepare = new JTextField();
    private final JComboBox<Category> category = new JComboBox<>();
    private final JTextField ingredientList = new JTextField();

    private final Recipe recipe;
    private final List<Category> categories;

    public EditRecipeDialog(Recipe recipe, List<Category> categories) {
        this.recipe = recipe;
        this.categories = categories;
        setValues();
        addFields();
    }

    private void setValues() {
        panel.setBackground(recipe.getCategory().getColor());
        title.setText(recipe.getTitle());
        description.setText(recipe.getDescription());
        portionCount.setText(Integer.toString(recipe.getPortionCount()));
        instructions.setText(recipe.getInstructions());
        timeToPrepare.setText(Integer.toString(recipe.getTimeToPrepare()));
        category.setModel(new javax.swing.DefaultComboBoxModel<>(categories.toArray(new Category[categories.size()])));
        category.getModel().setSelectedItem(recipe.getCategory());
        ingredientList.setText(recipe.getIngredientList().toString());
    }

    private void addFields() {
        add("Title:", title);
        add("Description:", description);
        add("Portions:", portionCount);
        add("Instructions:", instructions);
        add("Time to prepare:", timeToPrepare);
        add("Category:", category);
        add("List of Ingredients:", ingredientList);
    }

    @Override
    Recipe getEntity() {
        recipe.setTitle(title.getText());
        recipe.setDescription(description.getText());
        recipe.setPortionCount(Integer.parseInt(portionCount.getText()));
        recipe.setInstructions(instructions.getText());
        recipe.setTimeToPrepare(Integer.parseInt(timeToPrepare.getText()));
        recipe.setCategory((Category) category.getSelectedItem());
        //recipe.setIngredientList(Arrays.stream(ingredientList.getText().split(" ")).toList());
        return recipe;
    }
}

