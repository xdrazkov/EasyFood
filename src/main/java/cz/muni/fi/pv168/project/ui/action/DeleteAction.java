package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;

public final class DeleteAction extends GeneralAction {
    private final JTable recipeTable;
    private final JTable ingredientTable;
    private final JTable categoryTable;
    private final JTable unitTable;

    public DeleteAction(JTable recipeTable, JTable ingredientTable, JTable categoryTable, JTable unitTable) {
        super("Delete", Icons.DELETE_ICON);
        this.recipeTable = recipeTable;
        this.ingredientTable = ingredientTable;
        this.categoryTable = categoryTable;
        this.unitTable = unitTable;
        putValue(SHORT_DESCRIPTION, "Delete selected");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to delete selected recipe(s)?","Warning",dialogButton);
        if (dialogResult == JOptionPane.NO_OPTION){
            return;
        }

        JTable table = super.getTable();
        Consumer<Integer> deleteFunction;
        if (table.getModel() instanceof RecipeTableModel recipeTableModel) {
            deleteFunction = recipeTableModel::deleteRow;
        } else if (table.getModel() instanceof IngredientTableModel ingredientTableModel) {
            deleteFunction = ingredientTableModel::deleteRow;
        } else if (table.getModel() instanceof CategoryTableModel categoryTableModel) {
            deleteFunction = categoryTableModel::deleteRow;
        } else if (table.getModel() instanceof UnitTableModel unitTableModel) {
            deleteFunction = unitTableModel::deleteRow;
        } else {
            System.out.println("Editing different class " + table.getModel().getClass());
            return;
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
