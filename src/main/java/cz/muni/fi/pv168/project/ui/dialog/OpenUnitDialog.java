package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Unit;

import javax.swing.*;

public class OpenUnitDialog extends EntityDialog<Unit> {

    private final JLabel name = new JLabel();
    private final JLabel conversionRate =  new JLabel();
    private final JLabel ingredientType =  new JLabel();

    private final Unit unit;

    public OpenUnitDialog(Unit unit) {
        this.unit = unit;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(unit.getName() + " (" + unit.getAbbreviation() + ")");
        conversionRate.setText("Conversion rate to base unit (g/ml/pcs): " + unit.getConversionRate());
        ingredientType.setText(String.valueOf(unit.getIngredientType()));

    }

    private void addFields() {
        panel.add(name);
        panel.add(conversionRate);
        panel.add(ingredientType);
    }

    @Override
    Unit getEntity() {
        return unit;
    }
}


