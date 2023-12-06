package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.IngredientType;
import cz.muni.fi.pv168.project.model.Unit;
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

    public EditUnitDialog(Unit unit, JTable unitTable) {
        this.unit = unit;
        this.unitTable = unitTable;
        setValues(unit.getName(), unit.getAbbreviation(), unit.getIngredientType(), unit.getConversionRate());
        addFields();
    }

    public EditUnitDialog(Unit unit, JTable unitTable, String name, String abbreviation, IngredientType ingredientType, float conversionRate) {
        this.unit = unit;
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
        if (Objects.equals(unit.getName(), "grams") || Objects.equals(unit.getName(), "milliliters") || Objects.equals(unit.getName(), "pieces")) {
            JOptionPane.showMessageDialog(panel, "Cannot edit base units", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        float conversionRateFloat;
        try {
            conversionRateFloat = Float.parseFloat(conversionRate.getText().replace(',', '.'));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Conversion rate must be a decimal number", "Error", JOptionPane.ERROR_MESSAGE);

            var dialog = new EditUnitDialog(unit, unitTable, name.getText(), abbreviation.getText(), (IngredientType) ingredientType.getSelectedItem(), unit.getConversionRate());
            UnitTableModel unitTableModel = (UnitTableModel) unitTable.getModel();
            dialog.show(unitTable, "Edit Unit").ifPresent(unitTableModel::updateRow);

            return null;
        }
        unit.setName(name.getText());
        unit.setAbbreviation(abbreviation.getText());
        unit.setIngredientType((IngredientType) ingredientType.getSelectedItem());
        unit.setConversionRate(conversionRateFloat);
        return unit;
    }
}
