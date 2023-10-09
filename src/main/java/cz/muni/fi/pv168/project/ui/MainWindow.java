package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.ui.action.AddAction;
import cz.muni.fi.pv168.project.ui.action.DeleteAction;
import cz.muni.fi.pv168.project.ui.action.EditAction;
import cz.muni.fi.pv168.project.ui.action.OpenAction;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow {
    private final JFrame frame;
    private final Action addAction;
    private final Action deleteAction;
    private final Action editAction;
    private final Action openAction;

    public MainWindow() {
        frame = createFrame();
        ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("Recipe 1", "Description for Recipe 1"));
        recipes.add(new Recipe("Recipe 2", "Description for Recipe 2"));
        var recipeTable = createRecipeTable(recipes);
        addAction = new AddAction(recipeTable);
        deleteAction = new DeleteAction(recipeTable);
        deleteAction.setEnabled(false);
        editAction = new EditAction(recipeTable);
        editAction.setEnabled(false);
        openAction = new OpenAction(recipeTable);
        openAction.setEnabled(false);
        recipeTable.setComponentPopupMenu(createRecipeTablePopupMenu());
        frame.add(new JScrollPane(recipeTable), BorderLayout.CENTER);
        frame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        frame.setJMenuBar(createMenuBar());
        frame.pack();
    }

    public void show() {
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        var frame = new JFrame("Recipes");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    private JTable createRecipeTable(List<Recipe> recipes) {
        var model = new RecipeTableModel(recipes);
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);

        return table;
    }

    private JPopupMenu createRecipeTablePopupMenu() {
        var menu = new JPopupMenu();
        menu.add(openAction);
        menu.add(editAction);
        menu.add(addAction);
        menu.add(deleteAction);
        return menu;
    }

    private JMenuBar createMenuBar() {
        var menuBar = new JMenuBar();
        var editMenu = new JMenu("Edit");
        editMenu.add(openAction);
        editMenu.add(editAction);
        editMenu.add(addAction);
        editMenu.add(deleteAction);
        editMenu.setMnemonic('e');
        menuBar.add(editMenu);
        return menuBar;
    }

    private JToolBar createToolbar() {
        var toolbar = new JToolBar();
        toolbar.add(openAction);
        toolbar.add(editAction);
        toolbar.add(addAction);
        toolbar.add(deleteAction);
        return toolbar;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        editAction.setEnabled(selectionModel.getSelectedItemsCount() == 1);
        deleteAction.setEnabled(selectionModel.getSelectedItemsCount() > 0);
        openAction.setEnabled(selectionModel.getSelectedItemsCount() == 1);
    }
}
