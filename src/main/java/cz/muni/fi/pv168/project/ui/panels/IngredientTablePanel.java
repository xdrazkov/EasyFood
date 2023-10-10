package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;
import java.util.function.Consumer;

public class IngredientTablePanel extends JPanel {

    private final JTable table;
    private final Consumer<Integer> onSelectionChange;

    public IngredientTablePanel(IngredientTableModel ingredientTableModel, Consumer<Integer> onSelectionChange) {
        setLayout(new BorderLayout());
        table = setUpTable(ingredientTableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        this.onSelectionChange = onSelectionChange;
    }

    public JTable getTable() {
        return table;
    }

    private JTable setUpTable(IngredientTableModel ingredientTableModel) {
        var table = new JTable(ingredientTableModel);

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
