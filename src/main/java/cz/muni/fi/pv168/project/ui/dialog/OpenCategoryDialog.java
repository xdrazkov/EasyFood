package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.ColoredCircle;

import javax.swing.*;
import java.awt.*;

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
        color.setText("Color -> ");
    }

    private void addFields() {
        add("",name, THIN_HEIGHT);
        var colorPanel = new JPanel();
        colorPanel.setLayout(new GridBagLayout());
        colorPanel.add(color);
        colorPanel.add(new ColoredCircle(category.getColor()));
        add("", colorPanel, THIN_HEIGHT);
    }

    @Override
    Category getEntity() {
        return category;
    }
}
