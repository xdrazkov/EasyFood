package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.IngredientType;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.service.validation.Validator;

import javax.swing.*;

public final class AddUnitDialog extends EntityDialog<Unit> {

    private final JTextField name = new JTextField();
    private final JTextField abbreviation = new JTextField();
    private final JTextField conversionRate = new JTextField();
    private final JComboBox<IngredientType> ingredientType = new JComboBox<>();
    private final JTable unitTable;

    public AddUnitDialog(JTable unitTable, Validator<Unit> unitValidator) {
        super(unitValidator);
        this.unitTable = unitTable;
        setValues("", "", IngredientType.COUNTABLE, 1);
        addFields();
    }

    public AddUnitDialog(JTable unitTable, String name, String abbreviation, IngredientType ingredientType, float conversionRate, Validator<Unit> unitValidator) {
        super(unitValidator);
        this.unitTable = unitTable;
        setValues(name, abbreviation, ingredientType, conversionRate);
        addFields();
    }

    private void setValues(String nameValue, String abbreviationValue, IngredientType ingredientTypeValue, float conversionRateValue) {
        name.setText(nameValue);
        abbreviation.setText(abbreviationValue);
        conversionRate.setText(Float.toString(conversionRateValue));
        ingredientType.setModel(new javax.swing.DefaultComboBoxModel<>(IngredientType.values()));
        ingredientType.getModel().setSelectedItem(ingredientTypeValue);
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
            var dialog = new AddUnitDialog(unitTable, name.getText(), abbreviation.getText(), (IngredientType) ingredientType.getSelectedItem(), 1, entityValidator);
            return dialog.show(unitTable, "Add Unit").orElse(null);
        }
        return new Unit(name.getText(), abbreviation.getText(), (IngredientType) ingredientType.getSelectedItem(), conversionRateFloat);
    }
}

