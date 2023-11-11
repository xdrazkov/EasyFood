package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterIngredientValues;

import javax.swing.*;
import java.awt.*;

public class SpecialFilterIngredientValuesRenderer extends AbstractRenderer<SpecialFilterIngredientValues> {

    public SpecialFilterIngredientValuesRenderer() {
        super(SpecialFilterIngredientValues.class);
    }

    protected void updateLabel(JLabel label, SpecialFilterIngredientValues value) {
        switch (value) {
            case ALL -> renderAll(label);
        }
    }

    private static void renderAll(JLabel label) {
        label.setText("(ALL)");
        label.setFont(label.getFont().deriveFont(Font.ITALIC));
        label.setForeground(Color.GRAY);
    }
}
