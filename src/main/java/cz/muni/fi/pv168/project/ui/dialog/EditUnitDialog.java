package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.IngredientType;
import cz.muni.fi.pv168.project.model.Unit;

import javax.swing.*;

public class EditUnitDialog extends EntityDialog<Unit> {
    private final JTextField name = new JTextField();
    private final JTextField abbreviation = new JTextField();
    private final JTextField conversionRate = new JTextField();
    private final JComboBox<IngredientType> ingredientType = new JComboBox<>();

    private final Unit unit;

    public EditUnitDialog(Unit unit) {
        this.unit = unit;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(unit.getName());
        abbreviation.setText(unit.getAbbreviation());
        conversionRate.setText(Float.toString(unit.getConversionRate()));
        ingredientType.setModel(new javax.swing.DefaultComboBoxModel<>(IngredientType.values()));
    }

    private void addFields() {
        add("Name:", name);
        add("Abbreviation:", abbreviation);
        add("Unit type:", ingredientType);
        add("Conversion rate to base unit (g/ml/pcs):", conversionRate);
    }

    @Override
    Unit getEntity() {
        unit.setName(name.getText());
        unit.setAbbreviation(abbreviation.getText());
        unit.setIngredientType((IngredientType) ingredientType.getSelectedItem());
        unit.setConversionRate(Float.parseFloat(conversionRate.getText()));
        return unit;
    }
}
