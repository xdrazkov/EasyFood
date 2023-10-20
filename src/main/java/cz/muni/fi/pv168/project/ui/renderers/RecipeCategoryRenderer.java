package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RecipeCategoryRenderer extends DefaultTableCellRenderer {
    private static final int CATEGORY_INDEX = 2;
    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        int modelRow = table.getRowSorter().convertRowIndexToModel(row);
        Category currentCategory = (Category) table.getModel().getValueAt(modelRow, CATEGORY_INDEX); // index of category column, TODO this is magic variable
        setBackground(currentCategory.getColor());
        return this;
    }
}
