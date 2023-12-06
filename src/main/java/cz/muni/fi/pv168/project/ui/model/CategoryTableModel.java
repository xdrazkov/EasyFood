package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.service.crud.CrudService;
import cz.muni.fi.pv168.project.ui.dialog.AddCategoryDialog;
import cz.muni.fi.pv168.project.ui.dialog.EditCategoryDialog;
import cz.muni.fi.pv168.project.ui.dialog.OpenCategoryDialog;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import javax.swing.*;
import java.util.List;

public class CategoryTableModel extends BasicTableModel<Category> {
    public CategoryTableModel(DependencyProvider dependencyProvider, CrudService<Category> categoryCrudService) {
        super(dependencyProvider, categoryCrudService);
    }

    public List<Column<Category, ?>> makeColumns() {
        return List.of(
                Column.readonly("Name", Category.class, Category::getItself)
        );
    }

    @Override
    public void performAddAction(JTable table, UnitTableModel unitTableModel, List<Category> categories, List<Ingredient> ingredients, List<Unit> units) {
        CategoryTableModel categoryTableModel = (CategoryTableModel) table.getModel();
        var dialog = new AddCategoryDialog();
        dialog.show(table, "Add Category").ifPresent(categoryTableModel::addRow);
    }

    @Override
    public void performEditAction(int[] selectedRows, JTable table, UnitTableModel unitTableModel, List<Category> categories, List<Ingredient> ingredients, List<Unit> units) {
        int modelRow = table.convertRowIndexToModel(selectedRows[0]);
        CategoryTableModel categoryTableModel = (CategoryTableModel) table.getModel();
        var category = categoryTableModel.getEntity(modelRow);
        var dialog = new EditCategoryDialog(category);
        dialog.show(table, "Edit Category").ifPresent(categoryTableModel::updateRow);
    }

    @Override
    public void performOpenAction(JTable table, int modelRow) {
        CategoryTableModel categoryTableModel = (CategoryTableModel) table.getModel();
        var category = categoryTableModel.getEntity(modelRow);
        var dialog = new OpenCategoryDialog(category);
        dialog.show(table, "Open Category").ifPresent(categoryTableModel::updateRow);
    }
}
