package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.data.TestDataGenerator;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.TableRowSorter;

public class MainWindow {
    private final JFrame frame;
    private final GeneralAction addAction;
    private final GeneralAction deleteAction;
    private final GeneralAction editAction;
    private final GeneralAction openAction;
    private final GeneralAction importAction;
    private final GeneralAction exportAction;
    private final GeneralAction viewStatisticsAction;
    private final GeneralAction viewAboutAction;

    private final List<GeneralAction> actions;

    private static RecipeTableModel recipeTableModel;

    private static CategoryTableModel categoryTableModel;

    private static IngredientTableModel ingredientTableModel;

    private final UnitTableModel unitTableModel;

    private final Map<GeneralAction, List<Integer>> forbiddenActionsInTabs = new HashMap<>(); // key is list of indices of tabs, where action is not allowed

    private final JTabbedPane tabbedPane;

    public MainWindow() {
        frame = createFrame();

        // Generate test objects
        var testDataGenerator = new TestDataGenerator();
        var categories = testDataGenerator.createTestCategories(5);
        var ingredients = testDataGenerator.createTestIngredients(5);
        var units = testDataGenerator.createTestUnits(5);
        var recipes = testDataGenerator.createTestRecipes(10, categories, ingredients, units);

        // Create models
        recipeTableModel = new RecipeTableModel(recipes);
        ingredientTableModel = new IngredientTableModel(ingredients);
        categoryTableModel = new CategoryTableModel(categories);
        this.unitTableModel = new UnitTableModel(units);

        // Create panels
        var recipeTablePanel = new RecipeTablePanel(recipeTableModel, this::changeActionsState);
        var ingredientTablePanel = new IngredientTablePanel(ingredientTableModel, this::changeActionsState);
        var categoryTablePanel = new CategoryTablePanel(categoryTableModel, this::changeActionsState);
        var unitTablePanel = new UnitTablePanel(unitTableModel, this::changeActionsState);

        // Add the panels to tabbed pane
        this.tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Recipes", recipeTablePanel);
        tabbedPane.addTab("Ingredients", ingredientTablePanel);
        tabbedPane.addTab("Categories", categoryTablePanel);
        tabbedPane.addTab("Units", unitTablePanel);
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Set up actions for recipe table
        addAction = new AddAction(categories, ingredients, units, unitTableModel); // TODO pull somehow categories differently
        deleteAction = new DeleteAction();
        editAction = new EditAction(categories, ingredients, units, unitTableModel); // TODO pull somehow categories differently
        openAction = new OpenAction();
        importAction = new ImportAction();
        exportAction = new ExportAction();
        viewStatisticsAction = new ViewStatisticsAction(recipes, ingredients);
        viewAboutAction = new ViewAboutAction();
        this.actions = List.of(addAction, deleteAction, editAction, openAction, importAction, exportAction, viewAboutAction, viewStatisticsAction);
        setForbiddenActionsInTabs();
        setToDefaultActionEnablement(getCurrentTableIndex(tabbedPane));


        // Add popup menu, toolbar, menubar
        recipeTablePanel.getTable().setComponentPopupMenu(createTablePopupMenu(true));
        ingredientTablePanel.getTable().setComponentPopupMenu(createTablePopupMenu(false));
        categoryTablePanel.getTable().setComponentPopupMenu(createTablePopupMenu(false));
        unitTablePanel.getTable().setComponentPopupMenu(createTablePopupMenu(false));

        // ADD row sorters
        var rowSorter = new TableRowSorter<>(recipeTableModel);
        var recipeTableFilter = new RecipeTableFilter(rowSorter);
        recipeTablePanel.getTable().setRowSorter(rowSorter);

        var categoryFilter = createCategoryFilter(recipeTableFilter);
        var ingredientFilter = createIngredientFilter(recipeTableFilter);

        var preparationTimeSlider = createPreparationTimeSlider(recipeTableFilter);
        var nutritionalValuesSlider = createNutritionalValuesSlider(recipeTableFilter);
        frame.add(createToolbar(categoryFilter, ingredientFilter, preparationTimeSlider,
                nutritionalValuesSlider), BorderLayout.BEFORE_FIRST_LINE);

        var toolbar = createToolbar(categoryFilter, ingredientFilter);
        frame.add(toolbar, BorderLayout.BEFORE_FIRST_LINE);

        var menubar = createMenuBar();
        frame.setJMenuBar(menubar);
        frame.pack();

        setCurrentTableToActions(getCurrentTableFromPanel(tabbedPane)); // maps starting table to all actions
        tabbedPane.addChangeListener(new ChangeListener() {
            // when tab changes
            @Override
            public void stateChanged(ChangeEvent e) {
                var currTable = getCurrentTableFromPanel((JTabbedPane) e.getSource());
                setCurrentTableToActions(currTable);
                setToDefaultActionEnablement(getCurrentTableIndex(tabbedPane));

                // clear all row selections
                recipeTablePanel.getTable().clearSelection();
                ingredientTablePanel.getTable().clearSelection();
                unitTablePanel.getTable().clearSelection();
                categoryTablePanel.getTable().clearSelection();

                int currTabIndex = tabbedPane.getSelectedIndex();
                categoryFilter.setVisible(currTabIndex == 0); // first is recipe
                ingredientFilter.setVisible(currTabIndex == 0);
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

    private static JSlider createPreparationTimeSlider(RecipeTableFilter recipeTableFilter) {
        List<Integer> allTimes = recipeTableModel.getRecipes().stream()
                .map(Recipe::getTimeToPrepare)
                .toList();

        int minTime = allTimes.stream().mapToInt(Integer::intValue).min().orElse(0);
        int maxTime = allTimes.stream().mapToInt(Integer::intValue).max().orElse(60);

        JSlider preparationTimeSlider = new JSlider(JSlider.HORIZONTAL, minTime, maxTime, minTime);
        preparationTimeSlider.setMajorTickSpacing(10);
        preparationTimeSlider.setMinorTickSpacing(1);
        preparationTimeSlider.setPaintTicks(true);
        preparationTimeSlider.setPaintLabels(true);
        preparationTimeSlider.setToolTipText("Preparation time (min)");

        preparationTimeSlider.addChangeListener(e -> {
            Integer selectedTime = preparationTimeSlider.getValue();
            recipeTableFilter.filterPreparationTime(Either.right(selectedTime));
        });

        return preparationTimeSlider;
    }

    private static JSlider createNutritionalValuesSlider(RecipeTableFilter recipeTableFilter) {
        List<Integer> allNutritionalValues = recipeTableModel.getRecipes().stream()
                .map(Recipe::getNutritionalValue)
                .toList();

        int minValue = allNutritionalValues.stream().mapToInt(Integer::intValue).min().orElse(0);
        int maxValue = allNutritionalValues.stream().mapToInt(Integer::intValue).max().orElse(1000);

        JSlider preparationNutritionalValuesSlider = new JSlider(JSlider.HORIZONTAL, minValue, maxValue, minValue);
        preparationNutritionalValuesSlider.setMajorTickSpacing(10);
        preparationNutritionalValuesSlider.setMinorTickSpacing(1);
        preparationNutritionalValuesSlider.setPaintTicks(true);
        preparationNutritionalValuesSlider.setPaintLabels(true);
        preparationNutritionalValuesSlider.setToolTipText("Nutritional Values");

        preparationNutritionalValuesSlider.addChangeListener(e -> {
            Integer selectedTime = preparationNutritionalValuesSlider.getValue();
            recipeTableFilter.filterNutritionalValues(Either.right(selectedTime));
        });

        return preparationNutritionalValuesSlider;
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

    private JPopupMenu createTablePopupMenu(boolean isRecipe) {
        var menu = new JPopupMenu();
        menu.add(openAction);
        menu.add(editAction);
        menu.add(addAction);
        menu.add(deleteAction);

        if (isRecipe) { // only recipe has export possibility
            menu.addSeparator();
           	menu.add(importAction);
       	 	menu.add(exportAction);
        }

        return menu;
    }

    private JMenuBar createMenuBar() {
        var menuBar = new JMenuBar();
        var fileMenu = new JMenu("File");
        fileMenu.add(addAction);
        fileMenu.add(importAction);
        fileMenu.setMnemonic('f');
        menuBar.add(fileMenu);
        var vievMenu = new JMenu("View");
        vievMenu.add(viewStatisticsAction);
        vievMenu.add(viewAboutAction);
        vievMenu.setMnemonic('v');
        menuBar.add(vievMenu);
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
        int currentTabIndex = getCurrentTableIndex(this.tabbedPane);
        openAction.setEnabled(selectedItemsCount == 1 && isActionAllowed(openAction, currentTabIndex));
        editAction.setEnabled(selectedItemsCount == 1 && isActionAllowed(editAction, currentTabIndex));
        deleteAction.setEnabled(selectedItemsCount >= 1 && isActionAllowed(deleteAction, currentTabIndex));
        exportAction.setEnabled(selectedItemsCount >= 1 && isActionAllowed(exportAction, currentTabIndex));
    }

    private void setCurrentTableToActions(JTable table) {
        this.actions.forEach(x -> x.setTable(table));
    }

    private Integer getCurrentTableIndex(JTabbedPane tab) {
        return tab.getSelectedIndex();
    }
    private JTable getCurrentTableFromPanel(JTabbedPane tab) {
        JPanel panel = (JPanel) tab.getComponentAt(getCurrentTableIndex(tab));
        JScrollPane scrollPane = (JScrollPane) panel.getComponent(0);
        return (JTable) scrollPane.getViewport().getView();
    }

    private void setToDefaultActionEnablement(int currentTabIndex) {
        actions.forEach(x -> x.setEnabled(false)); // all actions are disabled

        addAction.setEnabled(isActionAllowed(addAction, currentTabIndex)); // except for add action
        importAction.setEnabled(isActionAllowed(importAction, currentTabIndex)); // and import
        viewStatisticsAction.setEnabled(true); //and views
        viewAboutAction.setEnabled(true);

    }

    /**
     * sets map, which can forbid, where actions cannot be used
     */
    private void setForbiddenActionsInTabs() {
        for (var action: this.actions) {
            this.forbiddenActionsInTabs.put(action, List.of());
        }
        this.forbiddenActionsInTabs.put(exportAction, List.of(1, 2, 3));
        this.forbiddenActionsInTabs.put(importAction, List.of(1, 2, 3));
    }

    /**
     * checks if action is forbidden in current opened tab
     */
    private boolean isActionAllowed(GeneralAction action, int currentTabIndex) {
        return ! this.forbiddenActionsInTabs.get(action).contains(currentTabIndex);
    }
}
