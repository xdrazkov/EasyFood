package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.service.crud.CrudService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

// TODO generic
public class CategoryTableModel extends AbstractTableModel {

    private List<Category> categories;
    private final CrudService<Category> categoryCrudService;

    private final List<Column<Category, ?>> columns = List.of(
            Column.readonly("Name", Category.class, Category::getItself)
    );

    public CategoryTableModel(CrudService<Category> categoryCrudService) {
        this.categories = new ArrayList<>(categoryCrudService.findAll());
        this.categoryCrudService = categoryCrudService;
    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var category = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(category);
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
        categories.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Category recipe) {
        int newRowIndex = categories.size();
        categories.add(recipe);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Category recipe) {
        int rowIndex = categories.indexOf(recipe);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public Category getEntity(int rowIndex) {
        return categories.get(rowIndex);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void refresh() {
        this.categories = new ArrayList<>(categoryCrudService.findAll());
        fireTableDataChanged();
    }
}
