package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.service.validation.Validator;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EditRecipeDialog extends EntityDialog<Recipe> {

    private final JTextField title = new JTextField();
    private final JTextField description = new JTextField();
    private final JFormattedTextField portionCount = FieldMaker.makeIntField();
    private final JTextArea instructions = new JTextArea();
    private final JFormattedTextField timeToPrepare = FieldMaker.makeIntField();
    private final JComboBox<Category> category = new JComboBox<>();
    private final JButton newButton = new JButton();
    private final JPanel ingredientsPanel = new JPanel();

    private final Recipe recipe;
    private final List<Category> categories;
    private final List<Ingredient> ingredients;
    private final List<Unit> units;

    public EditRecipeDialog(Recipe recipe, List<Category> categories, List<Ingredient> ingredients, List<Unit> units, Validator<Recipe> recipeValidator) {
        super(recipeValidator);
        this.recipe = recipe;
        this.categories = categories;
        this.ingredients = ingredients;
        this.units = units;
        setValues();
        addFields();
        addIngredients();
        panel.revalidate();
        panel.repaint();
    }

    private void setValues() {
        title.setText(recipe.getTitle());
        description.setText(recipe.getDescription());
        portionCount.setText(Integer.toString(recipe.getPortionCount()));
        instructions.setText(recipe.getInstructions());
        timeToPrepare.setText(Integer.toString(recipe.getTimeToPrepare()));
        category.setModel(new javax.swing.DefaultComboBoxModel<>(categories.toArray(new Category[categories.size()])));
        category.getModel().setSelectedItem(recipe.getCategory());
        newButton.setText("New Ingredient");
        newButton.addActionListener(new AddIngredient());
        ingredientsPanel.setLayout(new MigLayout("wrap 1"));
    }

    private void addFields() {
        add("Title:", title, THIN_HEIGHT);
        add("Description:", description, THIN_HEIGHT);
        add("Portions:", portionCount, THIN_HEIGHT);
        add("Instructions:", new JScrollPane(instructions), THICC_HEIGHT);
        add("Time to prepare(min):", timeToPrepare, THIN_HEIGHT);
        add("Category:", category, THIN_HEIGHT);
        add("Ingredients:", new JScrollPane(ingredientsPanel), THICC_HEIGHT);
        add(newButton, THIN_HEIGHT);
    }

   private void addIngredients() {
       for (Map.Entry<Ingredient, AmountInUnit> amountInUnitEntry : recipe.getIngredients().entrySet()) {
           addIngredient(amountInUnitEntry.getKey(), Integer.toString(amountInUnitEntry.getValue().getAmount()), amountInUnitEntry.getValue().getUnit());
       }
   }

    public void addIngredient(Ingredient selectedIngredient, String count, Unit selectedUnit){
        JPanel newIngredient = new JPanel();
        newIngredient.setLayout(new MigLayout("wrap 4"));

        JComboBox<Ingredient> ingredient = new JComboBox<>();
        ingredient.setModel(new DefaultComboBoxModel<>(ingredients.toArray(new Ingredient[ingredients.size()])));
        if (selectedIngredient != null){
            ingredient.setSelectedItem(selectedIngredient);
        } else {
            ingredient.setSelectedIndex(-1);
        }
        ingredient.addActionListener(new FilterUnits());
        newIngredient.add(ingredient);

        JTextField quantity = FieldMaker.makeIntField();
        quantity.setText(count);
        newIngredient.add(quantity);

        JComboBox<Unit> unit = new JComboBox<>();
        Unit[] filteredUnits = filterUnits(ingredient);
        unit.setModel(new DefaultComboBoxModel<>(filteredUnits));

        if (selectedUnit != null){
            unit.setSelectedItem(selectedUnit);
        }
        newIngredient.add(unit);

        JButton xButton = new JButton();
        xButton.setText("x");
        xButton.addActionListener(new DeleteIngredient());
        newIngredient.add(xButton);

        ingredientsPanel.add(newIngredient);
    }

    public void deleteIngredient(JButton source){
        ingredientsPanel.remove(source.getParent());
    }

    @Override
    Recipe getEntity() {
        HashMap<Ingredient, AmountInUnit> newIgredients = getAllIngredients();
        if(newIgredients == null){
            return null;
        }
        recipe.setIngredients(newIgredients);
        recipe.setTitle(title.getText());
        recipe.setDescription(description.getText());
        recipe.setPortionCount(FieldMaker.parseIntField(portionCount));
        recipe.setInstructions(instructions.getText());
        recipe.setTimeToPrepare(FieldMaker.parseIntField(timeToPrepare));
        recipe.setCategory((Category) category.getSelectedItem());
        return recipe;
    }

    public HashMap<Ingredient, AmountInUnit> getAllIngredients(){
        HashMap<Ingredient, AmountInUnit> newIngredients = new HashMap<>();
        for (Component component : ingredientsPanel.getComponents()){
            Ingredient ingredient = (Ingredient)((JComboBox<Ingredient>)((JPanel)component).getComponent(0)).getSelectedItem();
            int count = FieldMaker.parseIntField((JFormattedTextField) ((JPanel)component).getComponent(1));
            Unit unit = (Unit)((JComboBox<Unit>)((JPanel)component).getComponent(2)).getSelectedItem();
            if (newIngredients.containsKey(ingredient)) {
                EntityDialog.openErrorDialog("There are some duplicities in your ingredients.\nNot possible to save.");
                return null;
            }
            newIngredients.put(ingredient, new AmountInUnit(unit, count));
        }
        return newIngredients;
    }

    public Unit[] filterUnits(JComboBox<Ingredient> source){
        Ingredient selectedIngredient = (Ingredient)source.getSelectedItem();
        ArrayList<Unit> filteredUnits = new ArrayList<>();
        for(Unit unit : units){
            if (selectedIngredient != null && selectedIngredient.getDefaultUnit().getIngredientType() == unit.getIngredientType()){
                filteredUnits.add(unit);
            }
        }
        return filteredUnits.toArray(new Unit[filteredUnits.size()]);
    }

    public class AddIngredient implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            addIngredient(null, "0", null);
            panel.revalidate();
            panel.repaint();
        }
    }

    public class DeleteIngredient implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            deleteIngredient((JButton)event.getSource());
            panel.revalidate();
            panel.repaint();
        }
    }

    public class FilterUnits implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            JComboBox<Ingredient> source = (JComboBox<Ingredient>)event.getSource();
            Unit[] filteredUnits = filterUnits(source);
            JComboBox<Unit> unitBox = (JComboBox<Unit>)(((JPanel)(source.getParent()))).getComponent(2);
            unitBox.setModel(new javax.swing.DefaultComboBoxModel<>(filteredUnits));
            unitBox.setSelectedItem(((Ingredient)source.getSelectedItem()).getDefaultUnit());
            panel.revalidate();
            panel.repaint();
        }
    }
}

