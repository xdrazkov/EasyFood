package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.IngredientType;
import cz.muni.fi.pv168.project.model.Unit;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UnitTableModel extends AbstractTableModel {

    private final List<Unit> units;
    private final HashMap<IngredientType, Unit> baseUnitsMap = new HashMap<IngredientType, Unit>();

    private final List<Column<Unit, ?>> columns = List.of(
            Column.readonly("Name", String.class, Unit::getName),
            Column.readonly("Abbreviation", String.class, Unit::getAbbreviation)
    );

    public UnitTableModel(List<Unit> units) {
        this.units = new ArrayList<>(units);
        setupBaseUnits();
    }

    @Override
    public int getRowCount() {
        return units.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var unit = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(unit);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnType();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).isEditable();
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        var recipe = getEntity(rowIndex);
        columns.get(columnIndex).setValue(value, recipe);
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void deleteRow(int rowIndex) {
        units.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Unit recipe) {
        int newRowIndex = units.size();
        units.add(recipe);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Unit recipe) {
        int rowIndex = units.indexOf(recipe);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public Unit getEntity(int rowIndex) {
        return units.get(rowIndex);
    }

    public void setupBaseUnits(){
        Unit gram = new Unit("grams", "g", IngredientType.WEIGHABLE, 1);
        Unit milliliter = new Unit("milliliters", "ml", IngredientType.POURABLE, 1);
        Unit piece = new Unit("pieces", "pcs", IngredientType.COUNTABLE, 1);
        units.add(gram);
        units.add(milliliter);
        units.add(piece);
        baseUnitsMap.put(IngredientType.WEIGHABLE, gram);
        baseUnitsMap.put(IngredientType.POURABLE, milliliter);
        baseUnitsMap.put(IngredientType.COUNTABLE, piece);
    }

    public HashMap<IngredientType, Unit> getBaseUnitsMap() {
        return baseUnitsMap;
    }
}
