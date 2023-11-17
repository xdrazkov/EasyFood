package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.IngredientType;
import cz.muni.fi.pv168.project.model.Unit;

import java.util.HashMap;
import java.util.List;

public class UnitTableModel extends BasicTableModel<Unit> {
    private final HashMap<IngredientType, Unit> baseUnitsMap = new HashMap<IngredientType, Unit>();
    public UnitTableModel(List<Unit> units) {
        super(units);
        setupBaseUnits();
    }

    public List<Column<Unit, ?>> makeColumns() {
        return List.of(
                Column.readonly("Name", String.class, Unit::getName),
                Column.readonly("Abbreviation", String.class, Unit::getAbbreviation),
                Column.readonly("Conversion Rate", Float.class, Unit::getConversionRate)
        );
    }

    public void setupBaseUnits(){
        Unit gram = new Unit("grams", "g", IngredientType.WEIGHABLE, 1);
        Unit milliliter = new Unit("milliliters", "ml", IngredientType.POURABLE, 1);
        Unit piece = new Unit("pieces", "pcs", IngredientType.COUNTABLE, 1);
        getObjects().add(gram);
        getObjects().add(milliliter);
        getObjects().add(piece);
        baseUnitsMap.put(IngredientType.WEIGHABLE, gram);
        baseUnitsMap.put(IngredientType.POURABLE, milliliter);
        baseUnitsMap.put(IngredientType.COUNTABLE, piece);
    }

    public HashMap<IngredientType, Unit> getBaseUnitsMap() {
        return baseUnitsMap;
    }
}
