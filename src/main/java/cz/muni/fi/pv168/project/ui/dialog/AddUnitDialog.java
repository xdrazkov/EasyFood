package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.IngredientType;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.service.validation.Validator;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import javax.swing.*;
import java.util.Objects;

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
            EntityDialog.openErrorDialog("Conversion rate must be a decimal number");
            return null;
        }
        Unit unit = new Unit(name.getText(), abbreviation.getText(), (IngredientType) ingredientType.getSelectedItem(), conversionRateFloat);
        // name of unit equals name of base unit of given ingredient type
        if (UnitTableModel.hasBaseUnitName(unit.getName())) {
            EntityDialog.openErrorDialog("Cannot add base unit");
            return null;
        }
        return unit;
    }
}

