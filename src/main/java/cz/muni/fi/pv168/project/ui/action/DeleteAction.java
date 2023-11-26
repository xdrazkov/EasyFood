package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.model.BasicTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;

public final class DeleteAction extends GeneralAction {
    public DeleteAction() {
        super("Delete", Icons.DELETE_ICON);
        putValue(SHORT_DESCRIPTION, "Delete selected");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformedImpl(ActionEvent e) {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to delete selected item(s)?","Warning",dialogButton);
        if (dialogResult != JOptionPane.YES_OPTION){
            return;
        }

        JTable table = super.getTable();
        BasicTableModel model = (BasicTableModel) table.getModel();
        Consumer<Integer> deleteFunction = model::deleteRow;

        int[] selectedRows = table.getSelectedRows();
        int[] convertedRows = new int[selectedRows.length];
        for (int i = 0; i < selectedRows.length; i++) {
            convertedRows[i] = table.convertRowIndexToModel(selectedRows[i]);
            if (table.getModel() instanceof UnitTableModel unitTableModel) {
                String unitToDelete = (String) unitTableModel.getValueAt(convertedRows[i], 0);
                if (unitToDelete.equals("grams") || unitToDelete.equals("milliliters") || unitToDelete.equals("pieces")) {
                    JOptionPane.showMessageDialog(table, "Cannot delete base units", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        Arrays.stream(table.getSelectedRows())
        .map(table::convertRowIndexToModel)
        .boxed()
        .sorted(Comparator.reverseOrder())
        .forEach(deleteFunction);
    }

    @Override
    protected void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "Delete " + super.getCurrentTabName());
    }
}
