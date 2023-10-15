package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;

import javax.swing.*;
import java.util.List;

public class CategoryListModel  extends AbstractListModel<Category> {

    private final List<Category> Categories;

    public CategoryListModel(List<Category> Categories) {
        this.Categories = Categories;
    }

    @Override
    public int getSize() {
        return Categories.size();
    }

    @Override
    public Category getElementAt(int index) {
        return Categories.get(index);
    }
}
