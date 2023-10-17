package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.dialog.AddRecipeDialog;
import cz.muni.fi.pv168.project.ui.dialog.EditRecipeDialog;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public final class AddAction extends GeneralAction {

    private final List<Category> categories;

    public AddAction(List<Category> categories) {
        super("Add", Icons.ADD_ICON);
        this.categories = categories;
        putValue(SHORT_DESCRIPTION, "Adds new recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable table = super.getTable();
        if (table.isEditing()) {
            table.getCellEditor().cancelCellEditing();
        }

        if (table.getModel() instanceof RecipeTableModel recipeTableModel) {
            var dialog = new AddRecipeDialog(categories);
            dialog.show(table, "Add Recipe").ifPresent(recipeTableModel::addRow);
        } else {
            System.out.println("Editing different class " + table.getModel().getClass());
        }
    }
}
