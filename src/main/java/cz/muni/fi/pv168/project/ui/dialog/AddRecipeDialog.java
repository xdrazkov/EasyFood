package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.ui.model.ComboBoxModelAdapter;

import javax.swing.*;
import java.util.List;

public final class AddRecipeDialog extends EntityDialog<Recipe> {

    private final JTextField title = new JTextField();
    private final JTextField description = new JTextField();
    private final JTextField portionCount = new JTextField();
    private final JTextField instructions = new JTextField();
    private final JTextField timeToPrepare = new JTextField();
    private final JComboBox<Category> category = new JComboBox<>();
    private final JTextField ingredientList = new JTextField();

    private final List<Category> categories;

    public AddRecipeDialog(List<Category> categories) {
        this.categories = categories;
        setValues();
        addFields();
    }

    private void setValues() {
        title.setText("");
        description.setText("");
        portionCount.setText("1");
        instructions.setText("");
        timeToPrepare.setText("0");
        category.setModel(new javax.swing.DefaultComboBoxModel<>(categories.toArray(new Category[categories.size()])));
        //category.getModel().setSelectedItem(recipe.getCategory());
        ingredientList.setText("");
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
        Recipe recipe = new Recipe();
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

