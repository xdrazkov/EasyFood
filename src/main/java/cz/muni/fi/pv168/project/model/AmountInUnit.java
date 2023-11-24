package cz.muni.fi.pv168.project.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.muni.fi.pv168.project.export.json.deserializers.AmountInUnitJsonDeserializer;
import cz.muni.fi.pv168.project.export.json.seralizers.AmountInUnitJsonSerializer;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

@JsonSerialize(using = AmountInUnitJsonSerializer.class)
@JsonDeserialize(using = AmountInUnitJsonDeserializer.class)
public class AmountInUnit {
    private Unit unit;
    private int amount;

    public AmountInUnit(Unit unit, int amount) {
        this.unit = unit;
        this.amount = amount;
    }

    public AmountInUnit convertToBaseUnit() {
        return new AmountInUnit(unit.getIngredientType().getBaseUnit(), (int) (amount * unit.getConversionRate()));
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
