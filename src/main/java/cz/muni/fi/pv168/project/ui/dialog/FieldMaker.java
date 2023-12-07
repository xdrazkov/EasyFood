package cz.muni.fi.pv168.project.ui.dialog;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

public class FieldMaker extends JComponent {
    static public JFormattedTextField makeIntField() {
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        return new JFormattedTextField(formatter);
    }
    static public JFormattedTextField makeFloatField() {
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setMaximum(Double.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        return new JFormattedTextField(formatter);
    }

    static public int parseIntField(JFormattedTextField field) {
        return Integer.parseInt(field.getText().replaceAll(" ", ""));
    }
}
