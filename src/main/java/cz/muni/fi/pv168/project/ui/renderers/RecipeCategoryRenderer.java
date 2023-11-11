package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RecipeCategoryRenderer extends DefaultTableCellRenderer {
    private final int CATEGORY_COL_INDEX;

    public RecipeCategoryRenderer(int categoryColIndex) {
        CATEGORY_COL_INDEX = categoryColIndex;
    }
    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        int modelRow = table.getRowSorter().convertRowIndexToModel(row);
        Category currentCategory = (Category) table.getModel().getValueAt(modelRow, CATEGORY_COL_INDEX);
        setBackground(currentCategory.getColor());
        return this;
    }
}
