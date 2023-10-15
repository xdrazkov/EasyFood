package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.dialog.EditRecipeDialog;
import cz.muni.fi.pv168.project.ui.dialog.OpenRecipeDialog;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class OpenAction extends AbstractAction {

    private final JTable recipeTable;

    public OpenAction(JTable recipeTable) {
        super("Open", Icons.OPEN_ICON);
        this.recipeTable = recipeTable;
        putValue(SHORT_DESCRIPTION, "Opens recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_O);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl O"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = recipeTable.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }
        if (recipeTable.isEditing()) {
            recipeTable.getCellEditor().cancelCellEditing();
        }
        var recipeTableModel = (RecipeTableModel) recipeTable.getModel();
        int modelRow = recipeTable.convertRowIndexToModel(selectedRows[0]);
        var recipe = recipeTableModel.getEntity(modelRow);
        var dialog = new OpenRecipeDialog(recipe);
        dialog.show(recipeTable, "Open Recipe");
    }
}
