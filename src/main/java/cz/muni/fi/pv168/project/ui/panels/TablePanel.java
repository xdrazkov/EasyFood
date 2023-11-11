package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.function.Consumer;

public class TablePanel extends JPanel {
    private final JTable table;
    private final Consumer<Integer> onSelectionChange;
    private TablePanelType tablePanelType;

    public TablePanel(AbstractTableModel tableModel, Consumer<Integer> onSelectionChange) {
        setLayout(new BorderLayout());
        table = setUpTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
        setTablePanelType(tableModel);
        this.onSelectionChange = onSelectionChange;
    }

    public JTable getTable() {
        return table;
    }

    private JTable setUpTable(AbstractTableModel tableModel) {
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

    public void setTablePanelType(AbstractTableModel tableModel) {
        if (tableModel instanceof  RecipeTableModel) {
            this.tablePanelType = TablePanelType.RECIPE;
        } else if (tableModel instanceof IngredientTableModel) {
            this.tablePanelType = TablePanelType.INGREDIENT;
        } else if (tableModel instanceof CategoryTableModel) {
            this.tablePanelType = TablePanelType.CATEGORY;
        } else if (tableModel instanceof UnitTableModel) {
            this.tablePanelType = TablePanelType.UNIT;
        } else {
            System.out.println(tableModel + " invalid panel type");
        }
    }
}
