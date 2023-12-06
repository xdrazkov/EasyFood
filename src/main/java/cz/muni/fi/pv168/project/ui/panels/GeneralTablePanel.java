package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.model.Entity;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.ui.renderers.RecipeCategoryRenderer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.function.Consumer;

public abstract class GeneralTablePanel<T extends Entity> extends JPanel {
    private final Consumer<Integer> onSelectionChange;
    protected TablePanelType tablePanelType;
    protected final JTable table;

    public GeneralTablePanel(BasicTableModel<T> tableModel, Consumer<Integer> onSelectionChange) {
        setLayout(new BorderLayout());
        table = setUpTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
        this.onSelectionChange = onSelectionChange;
        setRenderer();
        table.setRowSorter(new TableRowSorter<>(tableModel));
    }

    public JTable getTable() {
        return table;
    }

    private JTable setUpTable(BasicTableModel<T> tableModel) {
        var table = new JTable(tableModel);

        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);

        return table;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        var count = selectionModel.getSelectedItemsCount();
        if (onSelectionChange != null) {
            onSelectionChange.accept(count);
        }
    }

    public TablePanelType getTablePanelType() {
        return this.tablePanelType;
    }

    protected abstract void setRenderer();
}
