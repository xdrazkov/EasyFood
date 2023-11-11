package cz.muni.fi.pv168.project.model;

import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import java.util.List;

public enum IngredientType {
    POURABLE, // ml
    WEIGHABLE, // g
    COUNTABLE; // pcs

    public Unit getBaseUnit(UnitTableModel unitTableModel){
        return unitTableModel.getBaseUnitsMap().get(this);
    }
}
