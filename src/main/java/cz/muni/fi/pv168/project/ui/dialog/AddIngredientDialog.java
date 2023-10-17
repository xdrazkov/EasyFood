package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.IngredientType;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import javax.swing.*;
import java.util.Vector;

public class AddIngredientDialog extends EntityDialog<Ingredient> {
    private final JTextField name = new JTextField();
    private final JTextField nutritionalValue = new JTextField();
    private final JComboBox<Unit> baseUnit = new JComboBox<>();
    private final UnitTableModel unitTableModel;

    public AddIngredientDialog(UnitTableModel unitTableModel) {
        this.unitTableModel = unitTableModel;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText("");
        nutritionalValue.setText("0");
        baseUnit.setModel(new javax.swing.DefaultComboBoxModel<Unit>(unitTableModel.getUnits().toArray(new Unit[0])));
    }

    private void addFields() {
        add("Name:", name);
        add("Base Unit:", baseUnit);
        add("Nutritional value per base unit:", nutritionalValue);
    }

    @Override
    Ingredient getEntity() {
        return new Ingredient(name.getText(), (Unit) baseUnit.getSelectedItem(), Integer.parseInt(nutritionalValue.getText()));
    }
}
