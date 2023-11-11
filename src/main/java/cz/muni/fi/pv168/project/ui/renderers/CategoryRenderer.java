package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;

import javax.swing.*;

public class CategoryRenderer extends AbstractRenderer<Category> {

    public CategoryRenderer() {
        super(Category.class);
    }

    @Override
    protected void updateLabel(JLabel label, Category category) {
        if (category != null) {
            label.setText(category.getName());
            //label.setForeground(category.getColor());
            //label.setBackground(category.getColor());
        }
    }
}
