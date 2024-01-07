package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.IngredientType;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.dialog.AddCategoryDialog;
import cz.muni.fi.pv168.project.ui.dialog.EditCategoryDialog;
import cz.muni.fi.pv168.project.ui.dialog.OpenCategoryDialog;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CategoryTableModel extends BasicTableModel<Category> {

    private static Category defaultCategory = null;

    public CategoryTableModel(DependencyProvider dependencyProvider) {
        super(dependencyProvider, dependencyProvider.getCategoryValidator(), dependencyProvider.getCategoryCrudService());
        setupNoCategory();
    }

    public List<Column<Category, ?>> makeColumns() {
        return List.of(
                Column.readonly("Name", Category.class, (x -> x))
        );
    }

    public void setupNoCategory() {
        List<Category> categories = dependencyProvider.getCategoryCrudService().findAll();
        if (categories.isEmpty()) {
            Category newCategory = new Category("No Category", Color.LIGHT_GRAY);
            addRow(newCategory);
            defaultCategory = newCategory;
        }
    }

    public static boolean hasDefaultCategory(String name){
        return name.equals("No Category");
    }

    public static boolean isDefaultCategory(Category category){
        return category.equals(defaultCategory);
    }

    public static Category getDefaultCategory(){
        return defaultCategory;
    }

    @Override
    public void performAddAction(JTable table) {
        CategoryTableModel categoryTableModel = (CategoryTableModel) table.getModel();
        var dialog = new AddCategoryDialog(entityValidator);
        dialog.show(table, "Add Category").ifPresent(categoryTableModel::addRow);
    }

    @Override
    public void performEditAction(int[] selectedRows, JTable table) {
        int modelRow = table.convertRowIndexToModel(selectedRows[0]);
        CategoryTableModel categoryTableModel = (CategoryTableModel) table.getModel();
        var category = categoryTableModel.getEntity(modelRow);
        var dialog = new EditCategoryDialog(category.deepClone(), entityValidator);
        var optional = dialog.show(table, "Edit Category");
        setAndUpdate(optional, category);
    }

    @Override
    public void performOpenAction(JTable table, int modelRow) {
        CategoryTableModel categoryTableModel = (CategoryTableModel) table.getModel();
        var category = categoryTableModel.getEntity(modelRow);
        var dialog = new OpenCategoryDialog(category);
        dialog.show(table, "Open Category").ifPresent(categoryTableModel::updateRow);
    }
}
