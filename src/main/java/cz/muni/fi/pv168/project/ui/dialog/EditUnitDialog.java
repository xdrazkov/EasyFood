package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.IngredientType;
import cz.muni.fi.pv168.project.model.Unit;

import javax.swing.*;
import java.util.Objects;

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
        ingredientType.getModel().setSelectedItem(unit.getIngredientType());
    }

    private void addFields() {
        add("Name:", name, THIN_HEIGHT);
        add("Abbreviation:", abbreviation, THIN_HEIGHT);
        add("Unit type:", ingredientType, THIN_HEIGHT);
        add("Conversion rate to base unit (g/ml/pcs):", conversionRate, THIN_HEIGHT);
    }

    @Override
    Unit getEntity() {
        System.out.println(unit.getName());
        if (Objects.equals(unit.getName(), "grams") || Objects.equals(unit.getName(), "milliliters") || Objects.equals(unit.getName(), "pieces")) {
            JOptionPane.showMessageDialog(panel, "Cannot edit base units", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        float conversionRateFloat;
        try {
            conversionRateFloat = Float.parseFloat(conversionRate.getText().replace(',', '.'));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Conversion rate must be a decimal number", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        unit.setName(name.getText());
        unit.setAbbreviation(abbreviation.getText());
        unit.setIngredientType((IngredientType) ingredientType.getSelectedItem());
        unit.setConversionRate(conversionRateFloat);
        return unit;
    }
}
