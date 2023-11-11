package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.data.TestDataGenerator;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.ui.action.*;
import cz.muni.fi.pv168.project.ui.filters.RecipeTableFilter;
import cz.muni.fi.pv168.project.ui.filters.components.FilterListModelBuilder;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterCategoryValues;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterIngredientValues;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.panels.*;
import cz.muni.fi.pv168.project.ui.rangeSlider.RangeSlider;
import cz.muni.fi.pv168.project.ui.rangeSlider.RecipeRangeSliderChangeListener;
import cz.muni.fi.pv168.project.ui.renderers.*;
import cz.muni.fi.pv168.project.ui.resources.Icons;
import cz.muni.fi.pv168.project.util.Either;
import cz.muni.fi.pv168.project.ui.filters.components.FilterComboboxBuilder;
import org.apache.commons.lang3.tuple.Pair;


import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.table.AbstractTableModel;
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

    // key is list of indices of tabs, where action is not allowed
    private final Map<GeneralAction, List<Integer>> forbiddenActionsInTabs = new HashMap<>();

    private final JTabbedPane tabbedPane;
    private final Border padding = new EmptyBorder(0, 5, 0, 5);

    public MainWindow() {
        frame = createFrame();
        frame.setMinimumSize(new Dimension(800, 800));
        frame.setIconImage(Icons.APP_ICON);

        // Generate test objects
        var testDataGenerator = new TestDataGenerator();
        var categories = testDataGenerator.createTestCategories(5);
        var ingredients = testDataGenerator.createTestIngredients(5);
        var units = testDataGenerator.createTestUnits(5);
        var recipes = testDataGenerator.createTestRecipes(20, categories, ingredients, units);

        // Create models
        RecipeTableModel recipeTableModel = new RecipeTableModel(recipes);
        IngredientTableModel ingredientTableModel = new IngredientTableModel(ingredients);
        CategoryTableModel categoryTableModel = new CategoryTableModel(categories);
        UnitTableModel unitTableModel = new UnitTableModel(units);
        List<AbstractTableModel> tableModels =
                List.of(recipeTableModel, ingredientTableModel, categoryTableModel, unitTableModel);

        // Create panels
        List<TablePanel> tablePanels = new ArrayList<>();
        for (var tableModel: tableModels) {
            tablePanels.add(new TablePanel(tableModel, this::changeActionsState));
        }

        var recipeTablePanel = tablePanels.get(TablePanelType.RECIPE.ordinal());
        var ingredientTablePanel = tablePanels.get(TablePanelType.INGREDIENT.ordinal());
        var categoryTablePanel = tablePanels.get(TablePanelType.CATEGORY.ordinal());
        var unitTablePanel = tablePanels.get(TablePanelType.UNIT.ordinal());

        // Add the panels to tabbed pane
        this.tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Recipes", recipeTablePanel);
        tabbedPane.addTab("Ingredients", ingredientTablePanel);
        tabbedPane.addTab("Categories", categoryTablePanel);
        tabbedPane.addTab("Units", unitTablePanel);
        tabbedPane.setBorder(padding);
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Set up actions for recipe table
        addAction = new AddAction(categories, ingredients, units, unitTableModel); // TODO pull somehow categories differently
        deleteAction = new DeleteAction(recipeTablePanel.getTable(), ingredientTablePanel.getTable(), categoryTablePanel.getTable(), unitTablePanel.getTable());
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
        tablePanels.forEach(
                r -> r.getTable().setComponentPopupMenu(createTablePopupMenu(r.getTablePanelType())));
        JLabel statusBar = createStatusBar();
        setStatusBarName(statusBar);

        // Add row sorters
        var recipeRowSorter = new TableRowSorter<>(recipeTableModel);
        var categoryRowSorter = new TableRowSorter<>(categoryTableModel);
        var ingredientRowSorter = new TableRowSorter<>(ingredientTableModel);
        var unitRowSorter = new TableRowSorter<>(unitTableModel);

        // set all row sorters
        recipeTablePanel.getTable().setRowSorter(recipeRowSorter);
        categoryTablePanel.getTable().setRowSorter(categoryRowSorter);
        ingredientTablePanel.getTable().setRowSorter(ingredientRowSorter);
        unitTablePanel.getTable().setRowSorter(unitRowSorter);

        // adding listener to change text of status bar when filtering rows
        recipeRowSorter.addRowSorterListener(e -> setStatusBarName(statusBar));

        var recipeTableFilter = new RecipeTableFilter(recipeRowSorter);
        var categoryFilter = createCategoryFilter(recipeTableFilter, categoryTableModel);
        var ingredientFilter = new JScrollPane(createIngredientFilter(recipeTableFilter, ingredientTableModel));

        var preparationTimeSlider =
                getRangeSlider(recipeTableModel, recipeTableFilter::filterPreparationTime,
                        Recipe::getTimeToPrepare, "Preparation time (min)");

        var nutritionalValuesSlider =
                getRangeSlider(recipeTableModel, recipeTableFilter::filterNutritionalValues,
                        Recipe::getNutritionalValue, "Nutritional values (kcal)");

        // paints rows in recipe and category tab by their category color
        var recipeRowColorRenderer = new RecipeCategoryRenderer(2);
        recipeTablePanel.getTable().setDefaultRenderer(Object.class, recipeRowColorRenderer);
        recipeTablePanel.getTable().setDefaultRenderer(Integer.class, recipeRowColorRenderer);
        categoryTablePanel.getTable().setDefaultRenderer(Object.class, new RecipeCategoryRenderer(0));

        JPanel toolbarPanel = new JPanel(new GridLayout(2, 1));
        JPanel preparationPanel = createSliderPanel(nutritionalValuesSlider);
        JPanel nutritionalPanel = createSliderPanel(preparationTimeSlider);
        var toolbar = createToolbar();
        var filtersToolbar = createToolbar(categoryFilter, ingredientFilter, preparationPanel, nutritionalPanel);
        toolbarPanel.add(toolbar);
        toolbarPanel.add(filtersToolbar);
        toolbarPanel.setBorder(padding);
        frame.add(toolbarPanel, BorderLayout.BEFORE_FIRST_LINE);

        var menubar = createMenuBar();
        frame.setJMenuBar(menubar);
        frame.pack();

        // maps starting table to all actions
        setCurrentTableToActions(getCurrentTableFromPanel(tabbedPane));
        tabbedPane.addChangeListener(new ChangeListener() {
            // when tab changes
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane tabPanel = (JTabbedPane) e.getSource();

                var currTable = getCurrentTableFromPanel(tabPanel);
                setCurrentTableToActions(currTable);
                setToDefaultActionEnablement(getCurrentTableIndex(tabPanel));

                // clear all row selections
                tablePanels.forEach(r -> r.getTable().clearSelection());

                int currTabIndex = tabPanel.getSelectedIndex();
                filtersToolbar.setVisible(currTabIndex == TablePanelType.RECIPE.ordinal()); // first is recipe

                setStatusBarName(statusBar);
            }
        });
    }

    private static JComboBox<Either<SpecialFilterCategoryValues, Category>> createCategoryFilter(
            RecipeTableFilter recipeTableFilter, CategoryTableModel categoryTableModel) {
        return FilterComboboxBuilder.create(SpecialFilterCategoryValues.class, categoryTableModel.getCategories().toArray(new Category[0]))
                .setSelectedItem(SpecialFilterCategoryValues.ALL)
                .setSpecialValuesRenderer(new SpecialFilterCategoryValuesRenderer())
                .setValuesRenderer(new CategoryRenderer())
                .setFilter(recipeTableFilter::filterCategory)
                .build();
    }

    private static JList<Either<SpecialFilterIngredientValues, Ingredient>> createIngredientFilter(
            RecipeTableFilter recipeTableFilter, IngredientTableModel ingredientTableModel) {
        ListModel<Ingredient> listModel = new AbstractListModel<>() {
            @Override
            public int getSize() {
                return ingredientTableModel.getIngredients().size();
            }

            @Override
            public Ingredient getElementAt(int index) {
                return ingredientTableModel.getIngredients().get(index);
            }
        };

        return FilterListModelBuilder.create(SpecialFilterIngredientValues.class, listModel)
                .setSelectedIndex(0)
                .setVisibleRowsCount(3)
                .setSpecialValuesRenderer(new SpecialFilterIngredientValuesRenderer())
                .setValuesRenderer(new IngredientRenderer())
                .setFilter(recipeTableFilter::filterIngredient)
                .build();
    }

    private static <T>RangeSlider getRangeSlider(RecipeTableModel recipeTableModel,
                                                 Consumer<Either<T, Pair<Integer, Integer>>> filterFunction,
                                                 Function<Recipe,Integer> mapperFunction,
                                                 String description) {
        List<Integer> values = recipeTableModel.getRecipes().stream()
                .map(mapperFunction)
                .toList();

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

        slider.addChangeListener(new RecipeRangeSliderChangeListener<>(filterFunction));

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

    private JPopupMenu createTablePopupMenu(TablePanelType tablePanelType) {
        var menu = new JPopupMenu();
        menu.add(addAction);
        menu.add(editAction);
        menu.add(deleteAction);
        menu.add(openAction);

        // only recipe has export possibility
        if (tablePanelType == TablePanelType.RECIPE) {
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
        var viewMenu = new JMenu("View");
        viewMenu.add(viewStatisticsAction);
        viewMenu.add(viewAboutAction);
        viewMenu.setMnemonic('v');
        menuBar.add(viewMenu);
        menuBar.setBorder(padding);
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
        statusPanel.setBorder(padding);
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
        List<Integer> forbidden = List.of(
                TablePanelType.INGREDIENT.ordinal(),
                TablePanelType.CATEGORY.ordinal(),
                TablePanelType.UNIT.ordinal());
        this.forbiddenActionsInTabs.put(exportAction, forbidden);
        this.forbiddenActionsInTabs.put(importAction, forbidden);
    }

    /**
     * checks if action is forbidden in current opened tab
     */
    private boolean isActionAllowed(GeneralAction action, int currentTabIndex) {
        return ! this.forbiddenActionsInTabs.get(action).contains(currentTabIndex);
    }

    private void setStatusBarName(JLabel statusBar) {
        String currTabTitle = this.tabbedPane.getTitleAt(getCurrentTableIndex(this.tabbedPane));
        JTable currTabPanel = getCurrentTableFromPanel(this.tabbedPane);
        int selected = currTabPanel.getRowCount();
        int all = currTabPanel.getModel().getRowCount();
        statusBar.setText(String.join(" ", "Showing records",
                Integer.toString(selected),
                "of",
                Integer.toString(all),
                currTabTitle
        ));
        statusBar.setBorder(new EmptyBorder(0, 10, 0, 10));
    }

    private static JPanel createSliderPanel(RangeSlider slider) {
        Button resetButton = new Button("Reset slider");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                slider.setValue(slider.getMinimum());
                slider.setUpperValue(slider.getMaximum());

            }
        });
        JPanel buttonTextPanel = new JPanel(new GridLayout(1, 2));
        buttonTextPanel.add(resetButton);
        buttonTextPanel.add(new JLabel(slider.getToolTipText(), SwingConstants.CENTER));

        JPanel finalPanel = new JPanel(new GridLayout(2, 1));
        finalPanel.add(slider);
        finalPanel.add(buttonTextPanel);

        finalPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        return finalPanel;
    }
}
