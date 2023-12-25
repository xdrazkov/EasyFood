package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Entity;
import cz.muni.fi.pv168.project.ui.FilterToolbar;
import cz.muni.fi.pv168.project.ui.model.BasicTableModel;
import cz.muni.fi.pv168.project.ui.panels.GeneralTablePanel;
import cz.muni.fi.pv168.project.ui.panels.TablePanelType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

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
    public abstract void actionPerformed(ActionEvent e);

    protected void refresh() {
        if (filterToolbar != null) {
            filterToolbar.updateFilters(generalTablePanel.getTablePanelType() == TablePanelType.RECIPE);
        }
    }
    public abstract void setShortDescription();

    public JTable getTable() {
        return this.generalTablePanel.getTable();
    }

    protected <T extends Entity> Collection<T> getSelectedEntities() {
        BasicTableModel<T> model = (BasicTableModel <T>) getTable().getModel();
        return Arrays.stream(generalTablePanel.getTable().getSelectedRows())
                .mapToObj(row -> model.getEntity(getTable().getRowSorter().convertRowIndexToModel(row)))
                .collect(Collectors.toList());

    }
}
