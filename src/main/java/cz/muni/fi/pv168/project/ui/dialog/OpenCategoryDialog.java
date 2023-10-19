package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;

import javax.swing.*;

public class OpenCategoryDialog extends EntityDialog<Category> {

    private final JLabel name = new JLabel();

    private final Category category;

    public OpenCategoryDialog(Category category) {
        this.category = category;
        setValues();
        addFields();
    }

    private void setValues() {
        panel.setBackground(category.getColor());
        name.setText(category.getName());
    }

    private void addFields() {
        panel.add(name);
    }

    @Override
    Category getEntity() {
        return category;
    }
}
