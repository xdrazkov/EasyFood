package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.service.validation.Validator;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import javax.swing.*;

public class EditCategoryDialog extends EntityDialog<Category> {
    private final JTextField name = new JTextField();
    private final JColorChooser color = new JColorChooser();

    private final Category category;
    public EditCategoryDialog(Category category, Validator<Category> categoryValidator) {
        super(categoryValidator);
        this.category = category;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(category.getName());
        color.setColor(category.getColor());
    }

    private void addFields() {
        add("Name:", name, THIN_HEIGHT);
        panel.add(color);
    }

    @Override
    Category getEntity() {
        if (CategoryTableModel.isDefaultCategory(category)) {
            EntityDialog.openErrorDialog("Cannot edit default category");
            return null;
        }

        category.setName(name.getText());
        category.setColor(color.getColor());
        return category;
    }
}
