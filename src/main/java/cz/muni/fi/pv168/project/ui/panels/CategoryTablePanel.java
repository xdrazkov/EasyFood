package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.model.BasicTableModel;
import cz.muni.fi.pv168.project.ui.renderers.RecipeCategoryRenderer;

import java.util.function.Consumer;

public class CategoryTablePanel extends GeneralTablePanel<Category> {
    public CategoryTablePanel(BasicTableModel<Category> tableModel, Consumer<Integer> onSelectionChange) {
        super(tableModel, onSelectionChange);
        this.tablePanelType = TablePanelType.CATEGORY;
    }

    @Override
    protected void setRenderer() {
        table.setDefaultRenderer(Object.class, new RecipeCategoryRenderer(0));
    }
}
