package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class BasicTableModel<E> extends AbstractTableModel implements EntityTableModel<E> {
    private final List<E> objects;
    private List<Column<E, ?>> columns = List.of();

    public BasicTableModel(List<E> recipes) {
        this.objects = new ArrayList<>(recipes);
        setColumns();
    }

    private void setColumns() {
        this.columns = makeColumns();
    }

    public abstract List<Column<E, ?>> makeColumns();

    public abstract void performAddAction(JTable table, UnitTableModel unitTableModel, List<Category> categories, List<Ingredient> ingredients, List<Unit> units);

    public abstract void performEditAction(int[] selectedRows, JTable table, UnitTableModel unitTableModel, List<Category> categories, List<Ingredient> ingredients, List<Unit> units);

    public abstract void performOpenAction(JTable table, int modelRow);

    public List<E> getObjects() {
        return objects;
    }

    @Override
    public int getRowCount() {
        return objects.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var recipe = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(recipe);
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

    public void deleteRow(int rowIndex) {
        objects.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(E recipe) {
        int newRowIndex = objects.size();
        objects.add(recipe);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(E recipe) {
        int rowIndex = objects.indexOf(recipe);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public E getEntity(int rowIndex) {
        return objects.get(rowIndex);
    }
}
