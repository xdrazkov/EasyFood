package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Entity;
import cz.muni.fi.pv168.project.service.validation.ValidationException;
import cz.muni.fi.pv168.project.ui.FilterToolbar;
import cz.muni.fi.pv168.project.ui.panels.GeneralTablePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * provides actions to change their target context (table)
 */
public abstract class GeneralAction extends AbstractAction {
    protected GeneralTablePanel<? extends Entity> generalTablePanel;
    private FilterToolbar filterToolbar;

    public GeneralAction(String name, Icon icon) {
        super(name, icon);
    }

    public void setGeneralTablePanel(GeneralTablePanel<? extends Entity> generalTablePanel) {
        this.generalTablePanel = generalTablePanel;
        setShortDescription();
    }

    public void setFilterToolbar(FilterToolbar filterToolbar) {
        this.filterToolbar = filterToolbar;
    }

    protected String getCurrentTabName() {
        int selectedCount = getTable().getSelectionModel().getSelectedItemsCount();
        if (selectedCount <= 1) {
            return generalTablePanel.getTablePanelType().getSingularName();
        }
        return generalTablePanel.getTablePanelType().getPluralName();
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
    protected abstract void actionPerformedImpl(ActionEvent e);

    public abstract void setShortDescription();

    public JTable getTable() {
        return this.generalTablePanel.getTable();
    }

    protected void openErrorDialog(ValidationException e) {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String validationError : e.getValidationErrors()) {
            listModel.addElement(validationError);
        }

        JList<String> errorList = new JList<>(listModel);

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(errorList));

        JOptionPane.showMessageDialog(null, panel,"Action not successful!", JOptionPane.ERROR_MESSAGE);
    }

}
