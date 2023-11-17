package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;
import java.util.List;

public class CategoryTableModel extends BasicTableModel<Category> {
    public CategoryTableModel(List<Category> recipes) {
        super(recipes);
    }

    public List<Column<Category, ?>> makeColumns() {
        return List.of(
                Column.readonly("Name", Category.class, Category::getItself)
        );
    }
}
