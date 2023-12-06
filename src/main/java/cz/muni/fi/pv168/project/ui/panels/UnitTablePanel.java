package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.ui.model.BasicTableModel;

import javax.swing.table.DefaultTableCellRenderer;
import java.util.function.Consumer;

public class UnitTablePanel extends GeneralTablePanel<Unit> {

    public UnitTablePanel(BasicTableModel<Unit> tableModel, Consumer<Integer> onSelectionChange) {
        super(tableModel, onSelectionChange);
        this.tablePanelType = TablePanelType.UNIT;
    }

    @Override
    protected void setRenderer() {
        table.setDefaultRenderer(Float.class, new DefaultTableCellRenderer());
    }
}
