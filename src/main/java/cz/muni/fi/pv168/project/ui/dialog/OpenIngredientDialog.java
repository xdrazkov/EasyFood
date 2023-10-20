package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Ingredient;

import javax.swing.*;

public class OpenIngredientDialog extends EntityDialog<Ingredient> {

    private final JLabel name = new JLabel();
    private final JLabel nutritionalValue = new JLabel();
    private final JLabel baseUnit = new JLabel();

    private final Ingredient ingredient;

    public OpenIngredientDialog(Ingredient ingredient) {
        this.ingredient = ingredient;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(ingredient.getName());
        baseUnit.setText("Base unit is " + ingredient.getBaseUnit().toString());
        nutritionalValue.setText(Integer.toString(ingredient.getCaloriesPerUnit()) + "kcal per base unit");
    }

    private void addFields() {
        panel.add(name);
        panel.add(baseUnit);
        panel.add(nutritionalValue);
    }

    @Override
    Ingredient getEntity() {
        return ingredient;
    }
}
