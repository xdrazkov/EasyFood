package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Entity;
import cz.muni.fi.pv168.project.service.validation.ValidationException;
import cz.muni.fi.pv168.project.ui.FilterToolbar;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * provides actions to change their target context (table)
 */
public abstract class GeneralAction extends AbstractAction {
    private JTable table;
    private FilterToolbar filterToolbar;

    public GeneralAction(String name, Icon icon) {
        super(name, icon);
    }

    public void setTable(JTable table) {
        this.table = table;
        setShortDescription();
    }

    public void setFilterToolbar(FilterToolbar filterToolbar) {
        this.filterToolbar = filterToolbar;
    }

    protected String getCurrentTabName() {
        if (this.table.getModel() instanceof RecipeTableModel) {
            return "Recipe";
        }
        if (this.table.getModel() instanceof IngredientTableModel) {
            return "Ingredient";
        }
        if (this.table.getModel() instanceof UnitTableModel) {
            return "Unit";
        }
        if (this.table.getModel() instanceof Category) {
            return "Category";
        }
        return "";
    }

    @Override
    public final void actionPerformed(ActionEvent e) {
        try {
            actionPerformedImpl(e);
        } catch (ValidationException exception) {
            openErrorDialog(exception);
        }
        if (filterToolbar != null) {
            filterToolbar.updateFilters();
        }
    }

    /**
     * helper method for actionPerformed
     */
    public abstract void actionPerformedImpl(ActionEvent e);

    protected abstract void setShortDescription();

    public JTable getTable() {
        return this.table;
    }

    protected void openErrorDialog(ValidationException e) {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String validationError : e.getValidationErrors()) {
            listModel.addElement(validationError);
        }

        JList<String> errorList = new JList<>(listModel);

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(errorList));

        JOptionPane.showMessageDialog(null, panel,"Action not successful!\n", JOptionPane.ERROR_MESSAGE);
    }

}
