package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.data.TestDataGenerator;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;

public class MainWindow {
    private final JFrame frame;
    private final GeneralAction addAction;
    private final GeneralAction deleteAction;
    private final GeneralAction editAction;
    private final GeneralAction openAction;

    private final GeneralAction importAction;

    private final GeneralAction exportAction;
    private List<GeneralAction> actions;

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
        addAction = new AddAction();
        deleteAction = new DeleteAction();
        deleteAction.setEnabled(false);
        editAction = new EditAction(categories); // TODO pull somehow categories differently
        editAction.setEnabled(false);
        openAction = new OpenAction();
        openAction.setEnabled(false);
        importAction = new ImportAction();
        exportAction = new ExportAction();
        exportAction.setEnabled(false);
        this.actions = List.of(addAction, deleteAction, editAction, openAction, importAction, exportAction);

        // Add the panels to tabbed pane
        var tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Recipes", recipeTablePanel);
        tabbedPane.addTab("Ingredients", ingredientTablePanel);
        tabbedPane.addTab("Categories", categoryTablePanel);
        tabbedPane.addTab("Units", unitTablePanel);
        frame.add(tabbedPane, BorderLayout.CENTER);
        setCurrentTableToActions(recipeTablePanel.getTable()); // maps initial table to all actions

        // Add popup menu, toolbar, menubar
        recipeTablePanel.getTable().setComponentPopupMenu(createTablePopupMenu());
        frame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        frame.setJMenuBar(createMenuBar());
        frame.pack();

        tabbedPane.addChangeListener(new ChangeListener() {
            // when tab changes
            @Override
            public void stateChanged(ChangeEvent e) {
                var currTable = getCurrentTableFromPanel((JTabbedPane) e.getSource());
                setCurrentTableToActions(currTable);

                // clear all row selections
                recipeTablePanel.getTable().clearSelection();
                ingredientTablePanel.getTable().clearSelection();
                unitTablePanel.getTable().clearSelection();
                categoryTablePanel.getTable().clearSelection();

                actions.forEach(x -> x.setEnabled(false)); // all actions are disabled
                addAction.setEnabled(true); // except for add action
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        var frame = new JFrame("Recipes");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }


    private JPopupMenu createTablePopupMenu() {
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

    private void setCurrentTableToActions(JTable table) {
        this.actions.forEach(x -> x.setTable(table));
    }

    private JTable getCurrentTableFromPanel(JTabbedPane tab) {
        int currentTab = tab.getSelectedIndex();
        JPanel panel = (JPanel) tab.getComponentAt(currentTab);
        JScrollPane scrollPane = (JScrollPane) panel.getComponent(0);
        return (JTable) scrollPane.getViewport().getView();
    }
}
