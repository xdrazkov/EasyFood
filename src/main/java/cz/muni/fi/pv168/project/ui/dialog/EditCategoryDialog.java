package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;

import javax.swing.*;
import java.awt.*;

public class EditCategoryDialog extends EntityDialog<Category> {
    private final JTextField name = new JTextField();
    private final JTextField color = new JTextField(); // TODO
    private final CategoryTableModel categoryTableModel;

    private final Category category;
    public EditCategoryDialog(Category category, CategoryTableModel categoryTableModel) {
        this.category = category;
        this.categoryTableModel = categoryTableModel;
        setValues();
        addFields();
    }

    private void setValues() {
        panel.setBackground(category.getColor());
        name.setText(category.getName());
    }

    private void addFields() {
        add("Name:", name);
    }

    @Override
    Category getEntity() {
        category.setName(name.getText());
        category.setColor(Color.BLACK); // TODO
        return category;
    }
}
