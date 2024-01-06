package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.service.validation.Validator;

import javax.swing.*;
import java.awt.*;

public class AddCategoryDialog extends EntityDialog<Category> {
    private final JTextField name = new JTextField();
    private final JColorChooser color = new JColorChooser();

    public AddCategoryDialog(Validator<Category> entityValidator) {
        super(entityValidator);
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText("");
        color.setColor(Color.WHITE);
    }

    private void addFields() {
        add("Name:", name, THIN_HEIGHT);
        add(color, THIN_HEIGHT);
    }

    @Override
    Category getEntity() {
        return new Category(name.getText(), color.getColor());
    }
}
