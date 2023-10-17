package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.data.TestDataGenerator;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.ui.action.*;
import cz.muni.fi.pv168.project.ui.filters.RecipeTableFilter;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterCategoryValues;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterIngredientValues;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.panels.CategoryTablePanel;
import cz.muni.fi.pv168.project.ui.panels.IngredientTablePanel;
import cz.muni.fi.pv168.project.ui.panels.RecipeTablePanel;
import cz.muni.fi.pv168.project.ui.panels.UnitTablePanel;
import cz.muni.fi.pv168.project.ui.renderers.CategoryRenderer;
import cz.muni.fi.pv168.project.ui.renderers.IngredientRenderer;
import cz.muni.fi.pv168.project.ui.renderers.SpecialFilterCategoryValuesRenderer;
import cz.muni.fi.pv168.project.ui.renderers.SpecialFilterIngredientValuesRenderer;
import cz.muni.fi.pv168.project.util.Either;
import cz.muni.fi.pv168.project.ui.filters.components.FilterComboboxBuilder;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;
import javax.swing.table.TableRowSorter;

public class MainWindow {
    private final JFrame frame;
    private final GeneralAction addAction;
    private final GeneralAction deleteAction;
    private final GeneralAction editAction;
    private final GeneralAction openAction;

    private final GeneralAction importAction;

    private final GeneralAction exportAction;
    private final List<GeneralAction> actions;

    private final RecipeTableModel recipeTableModel;

    private static CategoryTableModel categoryTableModel;

    private static IngredientTableModel ingredientTableModel;

    private final UnitTableModel unitTableModel;


    public MainWindow() {
        frame = createFrame();

        // Generate test objects
        var testDataGenerator = new TestDataGenerator();
        var categories = testDataGenerator.createTestCategories(5);
        var ingredients = testDataGenerator.createTestIngredients(5);
        var units = testDataGenerator.createTestUnits(5);
        var recipes = testDataGenerator.createTestRecipes(10, categories, ingredients, units);

        // Create models
        this.recipeTableModel = new RecipeTableModel(recipes);
        ingredientTableModel = new IngredientTableModel(ingredients);
        categoryTableModel = new CategoryTableModel(categories);
        this.unitTableModel = new UnitTableModel(units);

        // Create panels
        var recipeTablePanel = new RecipeTablePanel(recipeTableModel, this::changeActionsState);
        var ingredientTablePanel = new IngredientTablePanel(ingredientTableModel, this::changeActionsState);
        var categoryTablePanel = new CategoryTablePanel(categoryTableModel, this::changeActionsState);
        var unitTablePanel = new UnitTablePanel(unitTableModel, this::changeActionsState);

        // Set up actions for recipe table
        addAction = new AddAction(categories, unitTableModel); // TODO pull somehow categories differently
        deleteAction = new DeleteAction();
        editAction = new EditAction(categories, ingredients, units); // TODO pull somehow categories differently
        openAction = new OpenAction();
        importAction = new ImportAction();
        exportAction = new ExportAction();
        this.actions = List.of(addAction, deleteAction, editAction, openAction, importAction, exportAction);
        setToDefaultActionEnablement();

        // Add the panels to tabbed pane
        var tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Recipes", recipeTablePanel);
        tabbedPane.addTab("Ingredients", ingredientTablePanel);
        tabbedPane.addTab("Categories", categoryTablePanel);
        tabbedPane.addTab("Units", unitTablePanel);
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Add popup menu, toolbar, menubar
        recipeTablePanel.getTable().setComponentPopupMenu(createTablePopupMenu());
        var rowSorter = new TableRowSorter<>(recipeTableModel);
        var recipeTableFilter = new RecipeTableFilter(rowSorter);
        recipeTablePanel.getTable().setRowSorter(rowSorter);

        var categoryFilter = createCategoryFilter(recipeTableFilter);
        var ingredientFilter = createIngredientFilter(recipeTableFilter);
        frame.add(createToolbar(categoryFilter, ingredientFilter), BorderLayout.BEFORE_FIRST_LINE);

        frame.setJMenuBar(createMenuBar());
        frame.pack();

        setCurrentTableToActions(getCurrentTableFromPanel(tabbedPane)); // maps starting table to all actions

        tabbedPane.addChangeListener(new ChangeListener() {
            // when tab changes
            @Override
            public void stateChanged(ChangeEvent e) {
                var currTable = getCurrentTableFromPanel((JTabbedPane) e.getSource());
                setCurrentTableToActions(currTable);
                setToDefaultActionEnablement();

                // clear all row selections
                recipeTablePanel.getTable().clearSelection();
                ingredientTablePanel.getTable().clearSelection();
                unitTablePanel.getTable().clearSelection();
                categoryTablePanel.getTable().clearSelection();

                categoryFilter.setVisible(tabbedPane.getSelectedIndex() == 0); // first is recipe
                ingredientFilter.setVisible(tabbedPane.getSelectedIndex() == 0);
            }
        });
    }

    private static JComboBox<Either<SpecialFilterCategoryValues, Category>> createCategoryFilter(
            RecipeTableFilter recipeTableFilter) {
        return FilterComboboxBuilder.create(SpecialFilterCategoryValues.class, categoryTableModel.getCategories().toArray(new Category[0]))
                .setSelectedItem(SpecialFilterCategoryValues.ALL)
                .setSpecialValuesRenderer(new SpecialFilterCategoryValuesRenderer())
                .setValuesRenderer(new CategoryRenderer())
                .setFilter(recipeTableFilter::filterCategory)
                .build();
    }

    private static JComboBox<Either<SpecialFilterIngredientValues, Ingredient>> createIngredientFilter(
            RecipeTableFilter recipeTableFilter) {
        return FilterComboboxBuilder.create(SpecialFilterIngredientValues.class, ingredientTableModel.getIngredients().toArray(new Ingredient[0]))
                .setSelectedItem(SpecialFilterIngredientValues.ALL)
                .setSpecialValuesRenderer(new SpecialFilterIngredientValuesRenderer())
                .setValuesRenderer(new IngredientRenderer())
                .setFilter(recipeTableFilter::filterIngredient)
                .build();
    }

    public void show() {
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen
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

    private JToolBar createToolbar(Component... components) {
        var toolbar = new JToolBar();
        toolbar.add(openAction);
        toolbar.add(editAction);
        toolbar.add(addAction);
        toolbar.add(deleteAction);
        toolbar.addSeparator();
        toolbar.add(importAction);
        toolbar.add(exportAction);
        toolbar.addSeparator();

        for (var component : components) {
            toolbar.add(component);
        }

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

    private void setToDefaultActionEnablement() {
        actions.forEach(x -> x.setEnabled(false)); // all actions are disabled
        addAction.setEnabled(true); // except for add action
        importAction.setEnabled(true); // and import
    }
}
