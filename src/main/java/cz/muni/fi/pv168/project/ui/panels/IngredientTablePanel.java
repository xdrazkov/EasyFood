package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.ui.model.BasicTableModel;

import javax.swing.table.DefaultTableCellRenderer;
import java.util.function.Consumer;

public class IngredientTablePanel extends GeneralTablePanel<Ingredient> {
    public IngredientTablePanel(BasicTableModel<Ingredient> tableModel, Consumer<Integer> onSelectionChange) {
        super(tableModel, onSelectionChange);
        this.tablePanelType = TablePanelType.INGREDIENT;
    }

    @Override
    protected void setRenderer() {
        table.setDefaultRenderer(Float.class, new DefaultTableCellRenderer());
    }
}
