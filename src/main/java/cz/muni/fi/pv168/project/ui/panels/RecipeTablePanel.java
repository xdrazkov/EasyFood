package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.renderers.PreparationTimeRenderer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;
import java.util.function.Consumer;

public class RecipeTablePanel extends JPanel {

    private final JTable table;
    private final Consumer<Integer> onSelectionChange;

    public RecipeTablePanel(RecipeTableModel recipeTableModel, Consumer<Integer> onSelectionChange) {
        setLayout(new BorderLayout());
        table = setUpTable(recipeTableModel);
        table.setDefaultRenderer(Recipe.class, new PreparationTimeRenderer());
        add(new JScrollPane(table), BorderLayout.CENTER);

        this.onSelectionChange = onSelectionChange;
    }

    public JTable getTable() {
        return table;
    }

    private JTable setUpTable(RecipeTableModel recipeTableModel) {
        var table = new JTable(recipeTableModel);

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
}
