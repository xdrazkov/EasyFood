package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import javax.swing.*;

public class EditIngredientDialog extends EntityDialog<Ingredient> {
    private final JTextField name = new JTextField();
    private final JFormattedTextField nutritionalValue = FieldMaker.makeIntField();
    private final JComboBox<Unit> defaultUnit = new JComboBox<>();
    private final UnitTableModel unitTableModel;
    private final Ingredient ingredient;

    public EditIngredientDialog(Ingredient ingredient, UnitTableModel unitTableModel) {
        this.ingredient = ingredient;
        this.unitTableModel = unitTableModel;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(ingredient.getName());
        nutritionalValue.setText(Float.toString(ingredient.getCaloriesPerUnit()));
        defaultUnit.setModel(new javax.swing.DefaultComboBoxModel<>(unitTableModel.getEntities().toArray(new Unit[0])));
        defaultUnit.getModel().setSelectedItem(ingredient.getDefaultUnit());
    }

    private void addFields() {
        add("Name:", name, THIN_HEIGHT);
        add("Default Unit:", defaultUnit, THIN_HEIGHT);
        add("Nutritional value per default ingredient:", nutritionalValue, THIN_HEIGHT);
    }

    @Override
    Ingredient getEntity() {
        ingredient.setName(name.getText());
        ingredient.setDefaultUnit((Unit) defaultUnit.getSelectedItem());
        ingredient.setCaloriesPerUnit(FieldMaker.parseIntField(nutritionalValue));
        return ingredient;
    }
}
