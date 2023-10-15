package cz.muni.fi.pv168.project.ui.action;

import javax.swing.*;

/**
 * provides actions to change their target context (table)
 */
public abstract class GeneralAction extends AbstractAction {
    private JTable table;

    public GeneralAction(String name, Icon icon) {
        super(name, icon);
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public JTable getTable() {
        return this.table;
    }

}
