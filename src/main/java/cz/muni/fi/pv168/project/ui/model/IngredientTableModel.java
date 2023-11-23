package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.service.crud.CrudService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class IngredientTableModel extends AbstractTableModel {

    private List<Ingredient> ingredients;
    private final CrudService<Ingredient> ingredientCrudService;

    private final List<Column<Ingredient, ?>> columns = List.of(
            Column.readonly("Name", String.class, Ingredient::getName),
            Column.readonly("Default unit", Unit.class, Ingredient::getDefaultUnit),
            Column.readonly("Nutritional value (kcal)", Integer.class, Ingredient::getCaloriesPerUnit)
    );

    public IngredientTableModel(CrudService<Ingredient> ingredientCrudService) {
        this.ingredientCrudService = ingredientCrudService;
        this.ingredients = new ArrayList<>(ingredientCrudService.findAll());
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public int getRowCount() {
        return ingredients.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var ingredient = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(ingredient);
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
        ingredients.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Ingredient recipe) {
        int newRowIndex = ingredients.size();
        ingredients.add(recipe);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Ingredient recipe) {
        int rowIndex = ingredients.indexOf(recipe);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public Ingredient getEntity(int rowIndex) {
        return ingredients.get(rowIndex);
    }

    public void refresh() {
        this.ingredients = new ArrayList<>(ingredientCrudService.findAll());
        fireTableDataChanged();
    }
}
