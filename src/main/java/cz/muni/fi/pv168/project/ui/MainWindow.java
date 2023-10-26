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
import cz.muni.fi.pv168.project.ui.rangeSlider.RangeSlider;
import cz.muni.fi.pv168.project.ui.renderers.*;
import cz.muni.fi.pv168.project.util.Either;
import cz.muni.fi.pv168.project.ui.filters.components.FilterComboboxBuilder;
import org.apache.commons.lang3.tuple.Pair;


import javax.swing.*;
import javax.swing.border.BevelBorder;
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
        var recipes = testDataGenerator.createTestRecipes(20, categories, ingredients, units);

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
        this.actions = List.of(addAction, editAction, deleteAction, openAction, importAction, exportAction, viewAboutAction, viewStatisticsAction);
        setForbiddenActionsInTabs();
        setToDefaultActionEnablement(getCurrentTableIndex(tabbedPane));


        // Add popup menu, toolbar, menubar, status bar
        recipeTablePanel.getTable().setComponentPopupMenu(createTablePopupMenu(true));
        ingredientTablePanel.getTable().setComponentPopupMenu(createTablePopupMenu(false));
        categoryTablePanel.getTable().setComponentPopupMenu(createTablePopupMenu(false));
        unitTablePanel.getTable().setComponentPopupMenu(createTablePopupMenu(false));
        JLabel statusBarLabel = createStatusBar();
        statusBarLabel.setText("   Showing x of y recipes");

        // ADD row sorters
        var recipeRowSorter = new TableRowSorter<>(recipeTableModel);
        var categoryRowSorter = new TableRowSorter<>(categoryTableModel);
        var ingredientRowSorter = new TableRowSorter<>(ingredientTableModel);
        var unitRowSorter = new TableRowSorter<>(unitTableModel);
        recipeTablePanel.getTable().setRowSorter(recipeRowSorter);
        categoryTablePanel.getTable().setRowSorter(categoryRowSorter);
        ingredientTablePanel.getTable().setRowSorter(ingredientRowSorter);
        unitTablePanel.getTable().setRowSorter(unitRowSorter);

        var recipeTableFilter = new RecipeTableFilter(recipeRowSorter);
        var categoryFilter = createCategoryFilter(recipeTableFilter);
        var ingredientFilter = createIngredientFilter(recipeTableFilter);

        var preparationTimeSlider = createPreparationTimeSlider(recipeTableFilter);
        var nutritionalValuesSlider = createNutritionalValuesSlider(recipeTableFilter);

        // paints rows in recipe and category tab by their category color
        var recipeRowColorRenderer = new RecipeCategoryRenderer(2);
        recipeTablePanel.getTable().setDefaultRenderer(Object.class, recipeRowColorRenderer);
        recipeTablePanel.getTable().setDefaultRenderer(Integer.class, recipeRowColorRenderer);
        categoryTablePanel.getTable().setDefaultRenderer(Object.class, new RecipeCategoryRenderer(0));

        JPanel toolbarPanel = new JPanel(new GridLayout(2, 1));
        var toolbar = createToolbar();
        var filtersToolbar = createToolbar(categoryFilter, ingredientFilter, preparationTimeSlider, nutritionalValuesSlider);
        toolbarPanel.add(toolbar);
        toolbarPanel.add(filtersToolbar);
        frame.add(toolbarPanel, BorderLayout.BEFORE_FIRST_LINE);

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
                preparationTimeSlider.setVisible(currTabIndex == 0);
                nutritionalValuesSlider.setVisible(currTabIndex == 0);

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
        RangeSlider preparationTimeSlider = getRangeSlider(allTimes, "Preparation time (min)");

        preparationTimeSlider.addChangeListener(e -> {
            Integer selectedLowerTime = preparationTimeSlider.getValue();
            Integer selectedUpperTime = preparationTimeSlider.getUpperValue();
            recipeTableFilter.filterPreparationTime(Either.right(Pair.of(selectedLowerTime, selectedUpperTime)));
        });

        return preparationTimeSlider;
    }

    private static JSlider createNutritionalValuesSlider(RecipeTableFilter recipeTableFilter) {
        List<Integer> allNutritionalValues = recipeTableModel.getRecipes().stream()
                .map(Recipe::getNutritionalValue)
                .toList();
        RangeSlider preparationNutritionalValuesSlider = getRangeSlider(allNutritionalValues ,"Nutritional Values");

        preparationNutritionalValuesSlider.addChangeListener(e -> {
            Integer selectedLowerVal = preparationNutritionalValuesSlider.getValue();
            Integer selectedUpperVal = preparationNutritionalValuesSlider.getUpperValue();
            recipeTableFilter.filterNutritionalValues(Either.right(Pair.of(selectedLowerVal, selectedUpperVal)));
        });

        return preparationNutritionalValuesSlider;
    }
    private static RangeSlider getRangeSlider(List<Integer> values, String description) {
        int minValue = values.stream().mapToInt(Integer::intValue).min().orElse(0);
        int maxValue = values.stream().mapToInt(Integer::intValue).max().orElse(0);

        RangeSlider slider = new RangeSlider(minValue, maxValue);

        slider.setMajorTickSpacing((maxValue - minValue) / 10);
        slider.setMinorTickSpacing((maxValue - minValue) / 20);

        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.setToolTipText(description);
        slider.setValue(minValue);
        slider.setUpperValue(maxValue);

        return slider;
    }

    public void show() {
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // fullscreen
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        var frame = new JFrame("Easy Food");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    private JPopupMenu createTablePopupMenu(boolean isRecipe) {
        var menu = new JPopupMenu();
        menu.add(addAction);
        menu.add(editAction);
        menu.add(deleteAction);
        menu.add(openAction);

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

    private JToolBar createToolbar() {
        var toolbar = new JToolBar();
        toolbar.add(addAction);
        toolbar.add(editAction);
        toolbar.add(deleteAction);
        toolbar.add(openAction);
        toolbar.addSeparator();
        toolbar.add(importAction);
        toolbar.add(exportAction);
        toolbar.addSeparator();

        return toolbar;
    }

    private JToolBar createToolbar(Component... components) {
        var toolbar = new JToolBar();
        for (var component : components) {
            toolbar.add(component);
        }

        return toolbar;
    }

    private JLabel createStatusBar() {
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        frame.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 35));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        JLabel statusLabel = new JLabel("");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        return statusLabel;
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
