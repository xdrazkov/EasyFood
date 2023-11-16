package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EditRecipeDialog extends EntityDialog<Recipe> {

    private final JTextField title = new JTextField();
    private final JTextField description = new JTextField();
    private final JFormattedTextField portionCount = FieldMaker.makeIntField();
    private final JTextArea instructions = new JTextArea();
    private final JTextField timeToPrepare = FieldMaker.makeIntField();
    private final JComboBox<Category> category = new JComboBox<>();
    private final JLabel ingredientsLabel = new JLabel();
    private final JButton newButton = new JButton();
    private JScrollPane scroll;
    private final JPanel test = new JPanel();

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
        panel.revalidate();
        panel.repaint();
    }

    private void setValues() {
        panel.setPreferredSize(new Dimension(330, 560));
        title.setText(recipe.getTitle());
        description.setText(recipe.getDescription());
        portionCount.setText(Integer.toString(recipe.getPortionCount()));
        instructions.setText(recipe.getInstructions());
        timeToPrepare.setText(Integer.toString(recipe.getTimeToPrepare()));
        category.setModel(new javax.swing.DefaultComboBoxModel<>(categories.toArray(new Category[categories.size()])));
        category.getModel().setSelectedItem(recipe.getCategory());
        ingredientsLabel.setText("Ingredients");
        newButton.setText("New Ingredient");
        newButton.addActionListener(new AddIngredient());
        test.setLayout(new MigLayout("wrap 1"));
    }

    private void addFields() {
        add("Title:", title);
        add("Description:", description);
        add("Portions:", portionCount);
        JScrollPane scrollInstructions = new JScrollPane(instructions);
        scrollInstructions.setMinimumSize(new Dimension(300,100));
        add("Instructions:", scrollInstructions);
        add("Time to prepare(min):", timeToPrepare);
        add("Category:", category);
        panel.add(ingredientsLabel);
        panel.add(newButton);
        scroll = new JScrollPane(test);
        scroll.setMinimumSize(new Dimension(300,150));
        panel.add(scroll);
    }

   private void addIngredients() {
       for (Map.Entry<Ingredient, Pair<Unit, Integer>> ingredientPairEntry : recipe.getIngredients().entrySet()) {
           addIngredient(ingredientPairEntry.getKey(), Integer.toString(ingredientPairEntry.getValue().getValue()), ingredientPairEntry.getValue().getKey());
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

        test.add(newIngredient);
    }

    public void deleteIngredient(JButton source){
        test.remove(source.getParent());
    }

    @Override
    Recipe getEntity() {
        HashMap<Ingredient, Pair<Unit, Integer>> newIgredients = getAllIngredients();
        if(newIgredients == null){
            return null;
        }
        recipe.setIngredients(newIgredients);
        recipe.setTitle(title.getText());
        recipe.setDescription(description.getText());
        recipe.setPortionCount(Integer.parseInt(portionCount.getText().replaceAll(" ", "")));
        recipe.setInstructions(instructions.getText());
        recipe.setTimeToPrepare(Integer.parseInt(timeToPrepare.getText().replaceAll(" ", "")));
        recipe.setCategory((Category) category.getSelectedItem());
        return recipe;
    }

    public HashMap<Ingredient, Pair<Unit, Integer>> getAllIngredients(){
        HashMap<Ingredient, Pair<Unit, Integer>> newIgredients = new HashMap<>();
        for (Component component : test.getComponents()){
            Ingredient ingredient = (Ingredient)((JComboBox<Ingredient>)((JPanel)component).getComponent(0)).getSelectedItem();
            int count = Integer.parseInt(((JTextField)((JPanel)component).getComponent(1)).getText());
            Unit unit = (Unit)((JComboBox<Unit>)((JPanel)component).getComponent(2)).getSelectedItem();
            if (newIgredients.containsKey(ingredient)){
                JOptionPane.showConfirmDialog(null, "There are some duplicities in your ingredients.\nNot possible to save.", "Warning", JOptionPane.CLOSED_OPTION);
                return null;
            }
            newIgredients.put(ingredient, Pair.of(unit, count));
        }
        return newIgredients;
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

