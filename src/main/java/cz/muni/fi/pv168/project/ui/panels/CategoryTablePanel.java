package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;
import java.util.function.Consumer;

public class CategoryTablePanel extends JPanel {

    private final JTable table;
    private final Consumer<Integer> onSelectionChange;

    public CategoryTablePanel(CategoryTableModel categoryTableModel, Consumer<Integer> onSelectionChange) {
        setLayout(new BorderLayout());
        table = setUpTable(categoryTableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        this.onSelectionChange = onSelectionChange;
    }

    public JTable getTable() {
        return table;
    }

    private JTable setUpTable(CategoryTableModel categoryTableModel) {
        var table = new JTable(categoryTableModel);

        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);

        return table;
    }

    private void rowSelectionChanged(ListSelectionEvent tableSelectionEvent) {
        var selectionModel = (ListSelectionModel) tableSelectionEvent.getSource();
        var count = selectionModel.getSelectedItemsCount();
        if (onSelectionChange != null) {
            onSelectionChange.accept(count);
        }
    }
}
