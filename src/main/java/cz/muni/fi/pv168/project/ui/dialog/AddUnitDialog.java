package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.IngredientType;
import cz.muni.fi.pv168.project.model.Unit;

import javax.swing.*;

public final class AddUnitDialog extends EntityDialog<Unit> {

    private final JTextField name = new JTextField();
    private final JTextField abbreviation = new JTextField();
    private final JTextField conversionRate = new JTextField();
    private final JComboBox<IngredientType> ingredientType = new JComboBox<>();

    public AddUnitDialog() {
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText("");
        abbreviation.setText("");
        conversionRate.setText("1");
        ingredientType.setModel(new javax.swing.DefaultComboBoxModel<>(IngredientType.values()));
    }

    private void addFields() {
        add("Name:", name, THIN_HEIGHT);
        add("Abbreviation:", abbreviation, THIN_HEIGHT);
        add("Unit type:", ingredientType, THIN_HEIGHT);
        add("Conversion rate to base unit (g/ml/pcs):", conversionRate, THIN_HEIGHT);
    }

    @Override
    Unit getEntity() {
        float conversionRateFloat;
        try {
            conversionRateFloat = Float.parseFloat(conversionRate.getText().replace(',', '.'));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Conversion rate must be a decimal number", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return new Unit(name.getText(), abbreviation.getText(), (IngredientType) ingredientType.getSelectedItem(), conversionRateFloat);
    }
}

