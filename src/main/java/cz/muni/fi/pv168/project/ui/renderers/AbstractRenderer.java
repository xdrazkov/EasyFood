package cz.muni.fi.pv168.project.ui.renderers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public abstract class AbstractRenderer<T> implements ListCellRenderer<T>, TableCellRenderer {

    private final Class<T> elementType;
    private final DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
    private final DefaultListCellRenderer listCellRenderer = new DefaultListCellRenderer();

    protected AbstractRenderer(Class<T> elementType) {
        this.elementType = elementType;
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        // reset foreground color to default
        tableCellRenderer.setForeground(null);
        var label = (JLabel) tableCellRenderer.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        updateLabel(label, elementType.cast(value));
        return label;
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends T> list, T value,
            int index, boolean isSelected, boolean cellHasFocus) {

        var label = (JLabel) listCellRenderer.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        updateLabel(label, value);
        return label;
    }

    protected abstract void updateLabel(JLabel label, T value);
}
