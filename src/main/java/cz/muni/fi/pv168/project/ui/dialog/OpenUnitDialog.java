package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import javax.swing.*;

public class OpenUnitDialog extends EntityDialog<Unit> {

    private final JLabel name = new JLabel();
    private final JLabel conversionRate =  new JLabel();
    private final JLabel ingredientType =  new JLabel();
    private final UnitTableModel unitTableModel;

    private final Unit unit;

    public OpenUnitDialog(Unit unit, UnitTableModel unitTableModel) {
        this.unit = unit;
        this.unitTableModel = unitTableModel; // TODO: implement through Repository/CRUDService
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(unit.getName() + " (" + unit.getAbbreviation() + ")");
        conversionRate.setText("Conversion rate: 1 " + unit.getAbbreviation() + " = " + unit.getConversionRate() + " " + unit.getIngredientType().getBaseUnit(unitTableModel).getAbbreviation());
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


