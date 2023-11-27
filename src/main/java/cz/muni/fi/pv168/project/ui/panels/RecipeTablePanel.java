package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.renderers.RecipeCategoryRenderer;

import javax.swing.table.AbstractTableModel;
import java.util.function.Consumer;

public class RecipeTablePanel extends GeneralTablePanel<Recipe>{
    public RecipeTablePanel(RecipeTableModel tableModel, Consumer<Integer> onSelectionChange) {
        super(tableModel, onSelectionChange);
        this.tablePanelType = TablePanelType.RECIPE;
    }

    @Override
    protected void setRenderer() {
        var recipeRowColorRenderer = new RecipeCategoryRenderer(2);
        table.setDefaultRenderer(Object.class, recipeRowColorRenderer);
        table.setDefaultRenderer(Integer.class, recipeRowColorRenderer);
    }
}
