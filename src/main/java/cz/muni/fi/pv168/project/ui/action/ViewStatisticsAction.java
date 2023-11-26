package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.service.crud.CrudService;
import cz.muni.fi.pv168.project.ui.dialog.ViewStatisticsDialog;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ViewStatisticsAction extends GeneralAction {
    private final CrudService<Ingredient> ingredientCrudService;
    private final CrudService<Recipe> recipeCrudService;

    public ViewStatisticsAction(CrudService<Ingredient> ingredientCrudService, CrudService<Recipe> recipeCrudService) {
        super("Statistics", Icons.STATISTICS_ICON);
        this.ingredientCrudService = ingredientCrudService;
        this.recipeCrudService = recipeCrudService;
        putValue(SHORT_DESCRIPTION, "Statistics");
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));
    }

    @Override
    public void actionPerformedImpl(ActionEvent e) {
        JTable table = super.getTable();
        var dialog = new ViewStatisticsDialog(recipeCrudService.findAll(), ingredientCrudService.findAll());
        dialog.show(table, "View Statistics");
    }

    @Override
    protected void setShortDescription() {
        putValue(SHORT_DESCRIPTION, "View Statistics");
    }
}
