package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.data.TestDataGenerator;
import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.ui.action.*;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.panels.CategoryTablePanel;
import cz.muni.fi.pv168.project.ui.panels.IngredientTablePanel;
import cz.muni.fi.pv168.project.ui.panels.RecipeTablePanel;
import cz.muni.fi.pv168.project.ui.panels.UnitTablePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainWindow {
    private final JFrame frame;
    private final Action addAction;
    private final Action deleteAction;
    private final Action editAction;
    private final Action openAction;

    private final Action importAction;

    private final Action exportAction;

    public MainWindow() {
        frame = createFrame();

        // Generate test objects

        var testDataGenerator = new TestDataGenerator();
        var categories = testDataGenerator.createTestCategories(5);
        var ingredients = testDataGenerator.createTestIngredients(5);
        var units = testDataGenerator.createTestUnits(5);
        var recipes = testDataGenerator.createTestRecipes(10, categories, ingredients, units);

        // Create models
        var recipeTableModel = new RecipeTableModel(recipes);
        var ingredientTableModel = new IngredientTableModel(ingredients);
        var categoryTableModel = new CategoryTableModel(categories);
        var unitTableModel = new UnitTableModel(units);

        // Create panels
        var recipeTablePanel = new RecipeTablePanel(recipeTableModel, this::changeActionsState);
        var ingredientTablePanel = new IngredientTablePanel(ingredientTableModel, this::changeActionsState);
        var categoryTablePanel = new CategoryTablePanel(categoryTableModel, this::changeActionsState);
        var unitTablePanel = new UnitTablePanel(unitTableModel, this::changeActionsState);


        // Set up actions for recipe table
        addAction = new AddAction(recipeTablePanel.getTable());
        deleteAction = new DeleteAction(recipeTablePanel.getTable());
        deleteAction.setEnabled(false);
        editAction = new EditAction(recipeTablePanel.getTable());
        editAction.setEnabled(false);
        openAction = new OpenAction(recipeTablePanel.getTable());
        openAction.setEnabled(false);
        importAction = new ImportAction(recipeTablePanel.getTable());
        exportAction = new ExportAction(recipeTablePanel.getTable());
        exportAction.setEnabled(false);

        // Add the panels to tabbed pane
        var tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Recipes", recipeTablePanel);
        tabbedPane.addTab("Ingredients", ingredientTablePanel);
        tabbedPane.addTab("Categories", categoryTablePanel);
        tabbedPane.addTab("Units", unitTablePanel);
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Add popup menu, toolbar, menubar
        recipeTablePanel.getTable().setComponentPopupMenu(createRecipeTablePopupMenu());
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


    private JPopupMenu createRecipeTablePopupMenu() {
        var menu = new JPopupMenu();
        menu.add(openAction);
        menu.add(editAction);
        menu.add(addAction);
        menu.add(deleteAction);
        menu.addSeparator();
        menu.add(exportAction);
        return menu;
    }

    private JMenuBar createMenuBar() {
        var menuBar = new JMenuBar();
        var editMenu = new JMenu("Edit");
        editMenu.add(openAction);
        editMenu.add(editAction);
        editMenu.add(addAction);
        editMenu.add(deleteAction);
        editMenu.addSeparator();
        editMenu.add(importAction);
        editMenu.add(exportAction);
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
        toolbar.addSeparator();
        toolbar.add(importAction);
        toolbar.add(exportAction);
        return toolbar;
    }

    private void changeActionsState(int selectedItemsCount) {
        openAction.setEnabled(selectedItemsCount == 1);
        editAction.setEnabled(selectedItemsCount == 1);
        deleteAction.setEnabled(selectedItemsCount >= 1);
        exportAction.setEnabled(selectedItemsCount >= 1);
    }
}
