package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow {
    private final JFrame frame;

    public MainWindow() {
        frame = createFrame();
        ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("Recipe 1", "Description for Recipe 1"));
        recipes.add(new Recipe("Recipe 2", "Description for Recipe 2"));
        var recipeTable = createRecipeTable(recipes);
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
        return menu;
    }

    private JMenuBar createMenuBar() {
        var menuBar = new JMenuBar();
        return menuBar;
    }

    private JToolBar createToolbar() {
        var toolbar = new JToolBar();
        return toolbar;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
    }
}
