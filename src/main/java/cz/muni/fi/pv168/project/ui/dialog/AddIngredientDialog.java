package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import javax.swing.*;

public class AddIngredientDialog extends EntityDialog<Ingredient> {
    private final JTextField name = new JTextField();
    private final JFormattedTextField nutritionalValue = FieldMaker.makeIntField();
    private final JComboBox<Unit> defaultUnit = new JComboBox<>();
    private final UnitTableModel unitTableModel;

    public AddIngredientDialog(UnitTableModel unitTableModel) {
        this.unitTableModel = unitTableModel;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText("");
        nutritionalValue.setText("0");
        defaultUnit.setModel(new javax.swing.DefaultComboBoxModel<>(unitTableModel.getEntities().toArray(new Unit[0])));
    }

    private void addFields() {
        add("Name:", name, THIN_HEIGHT);
        add("Default Unit:", defaultUnit, THIN_HEIGHT);
        add("Nutritional value per default unit:", nutritionalValue, THIN_HEIGHT);
    }

    @Override
    Ingredient getEntity() {
        return new Ingredient(name.getText(), (Unit) defaultUnit.getSelectedItem(), FieldMaker.parseIntField(nutritionalValue));
    }
}
