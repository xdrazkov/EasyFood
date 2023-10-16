package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.data.TestDataGenerator;
import cz.muni.fi.pv168.project.model.*;
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
import cz.muni.fi.pv168.project.ui.renderers.SpecialFilterCategoryValuesRenderer;
import cz.muni.fi.pv168.project.ui.renderers.SpecialFilterIngredientValuesRenderer;
import cz.muni.fi.pv168.project.util.Either;
import cz.muni.fi.pv168.project.ui.filters.components.FilterComboboxBuilder;


import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class MainWindow {
    private final JFrame frame;
    private final Action addAction;
    private final Action deleteAction;
    private final Action editAction;
    private final Action openAction;

    private final Action importAction;

    private final Action exportAction;

    private final RecipeTableModel recipeTableModel;

    private final CategoryTableModel categoryTableModel;

    private final IngredientTableModel ingredientTableModel;

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
        this.ingredientTableModel = new IngredientTableModel(ingredients);
        this.categoryTableModel = new CategoryTableModel(categories);
        this.unitTableModel = new UnitTableModel(units);

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
        var rowSorter = new TableRowSorter<>(recipeTableModel);
        var recipeTableFilter = new RecipeTableFilter(rowSorter);
        recipeTablePanel.getTable().setRowSorter(rowSorter);

        var categoryFilter = createCategoryFilter(recipeTableFilter);
        var ingredientFilter = createIngredientFilter(recipeTableFilter);

        frame.add(createToolbar(categoryFilter, ingredientFilter), BorderLayout.BEFORE_FIRST_LINE);
//        frame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        frame.setJMenuBar(createMenuBar());

        frame.pack();
    }

    private static JComboBox<Either<SpecialFilterCategoryValues, Category>> createCategoryFilter(
            RecipeTableFilter recipeTableFilter) {
        return FilterComboboxBuilder.create(SpecialFilterCategoryValues.class, this.categoryTableModel.get)
                .setSelectedItem(SpecialFilterCategoryValues.ALL)
                .setSpecialValuesRenderer(new SpecialFilterCategoryValuesRenderer())
//                .setValuesRenderer(new GenderRenderer())
                .setFilter(recipeTableFilter::filterCategory)
                .build();
    }

    private static JComboBox<Either<SpecialFilterIngredientValues, Ingredient>> createIngredientFilter(
            RecipeTableFilter recipeTableFilter) {
        return FilterComboboxBuilder.create(SpecialFilterIngredientValues.class, IngredientTableModel)
                .setSelectedItem(SpecialFilterIngredientValues.ALL)
                .setSpecialValuesRenderer(new SpecialFilterIngredientValuesRenderer())
//                .setValuesRenderer(new DepartmentRenderer())
                .setFilter(recipeTableFilter::filterIngredient)
                .build();
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
}
