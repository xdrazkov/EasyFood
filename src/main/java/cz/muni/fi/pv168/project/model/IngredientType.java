package cz.muni.fi.pv168.project.model;

import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import java.util.List;

public enum IngredientType {
    POURABLE, // ml
    WEIGHABLE, // g
    COUNTABLE; // pcs

    public Unit getBaseUnit(){
        return UnitTableModel.getBaseUnit(this);
    }

    @Override
    public String toString() {
        switch(this) {
            case POURABLE:
                return "Ⓟ";
            case WEIGHABLE:
                return "Ⓦ";
            case COUNTABLE:
                return "Ⓒ";
            default:
                return "○";
        }
    }
}
