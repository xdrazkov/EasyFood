package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.IngredientType;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.service.validation.Validator;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import javax.swing.*;
import java.util.Objects;

public class EditUnitDialog extends EntityDialog<Unit> {
    private final JTextField name = new JTextField();
    private final JTextField abbreviation = new JTextField();
    private final JTextField conversionRate = new JTextField();
    private final JComboBox<IngredientType> ingredientType = new JComboBox<>();

    private final Unit unit;
    private final JTable unitTable;

    public EditUnitDialog(Unit unit, JTable unitTable, Validator<Unit> unitValidator) {
        super(unitValidator);
        this.unit = unit;
        this.unitTable = unitTable;
        setValues(unit.getName(), unit.getAbbreviation(), unit.getIngredientType(), unit.getConversionRate());
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
        if (UnitTableModel.hasBaseUnitName(unit.getName())) {
            EntityDialog.openErrorDialog("Cannot edit base units");
            return null;
        }

        float conversionRateFloat;
        try {
            conversionRateFloat = Float.parseFloat(conversionRate.getText().replace(',', '.'));
        } catch (NumberFormatException e) {
            EntityDialog.openErrorDialog("Conversion rate must be a decimal number");
            return null;
        }
        unit.setName(name.getText());
        unit.setAbbreviation(abbreviation.getText());
        unit.setIngredientType((IngredientType) ingredientType.getSelectedItem());
        unit.setConversionRate(conversionRateFloat);
        return unit;
    }
}
