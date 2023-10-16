package cz.muni.fi.pv168.project.ui.model;

import javax.swing.table.TableModel;

public interface EntityTableModel<E> extends TableModel {

    /**
     * Gets the entity at a certain index.
     *
     * @param rowIndex The index of the requested entity
     * @throws IndexOutOfBoundsException in case the rowIndex is less than zero or greater or equal
     *                                   than number of items in the table
     * */
    E getEntity(int rowIndex);
}
