package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;

import javax.swing.*;

public class OpenCategoryDialog extends EntityDialog<Category> {

    private final JLabel name = new JLabel();
    private final JLabel color = new JLabel();

    private final Category category;

    public OpenCategoryDialog(Category category) {
        this.category = category;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(category.getName());
        color.setText("This is the Color of this category.");
        color.setForeground(category.getColor());
    }

    private void addFields() {
        panel.add(name);
        panel.add(color);
    }

    @Override
    Category getEntity() {
        return category;
    }
}
