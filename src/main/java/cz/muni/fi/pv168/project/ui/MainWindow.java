package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.ui.action.*;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.ui.panels.*;
import cz.muni.fi.pv168.project.ui.resources.Icons;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.TableRowSorter;

import static cz.muni.fi.pv168.project.ui.panels.TablePanelType.*;

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
    private final GeneralAction authAction;

    private final List<GeneralAction> actions;

    // key is list of indices of tabs, where action is not allowed
    private final Map<GeneralAction, List<Integer>> forbiddenActionsInTabs = new HashMap<>();

    private final JTabbedPane tabbedPane;
    private final Border padding = new EmptyBorder(0, 10, 0, 10);

    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    public MainWindow(DependencyProvider dependencyProvider) {
        frame = createFrame();
        frame.setMinimumSize(new Dimension(1800, 800));
        frame.setIconImage(Icons.APP_ICON);

        var recipeCrudService = dependencyProvider.getRecipeCrudService();
        var ingredientCrudService = dependencyProvider.getIngredientCrudService();
        var categoryCrudService = dependencyProvider.getCategoryCrudService();
        var unitCrudService = dependencyProvider.getUnitCrudService();
        var exportService = dependencyProvider.getExportService();
        var importService = dependencyProvider.getImportService();

        // Create models
        RecipeTableModel recipeTableModel = new RecipeTableModel(dependencyProvider);
        IngredientTableModel ingredientTableModel = new IngredientTableModel(dependencyProvider);
        CategoryTableModel categoryTableModel = new CategoryTableModel(dependencyProvider);
        UnitTableModel unitTableModel = new UnitTableModel(dependencyProvider);
        List<BasicTableModel<? extends Entity>> tableModels =
                List.of(recipeTableModel, ingredientTableModel, categoryTableModel, unitTableModel);

        // DO NOT MOVE THIS BLOCK UPPER
//        var testDataGenerator = new TestDataGenerator();
//        testDataGenerator.getUnits().forEach(unitCrudService::create);
//        testDataGenerator.getCategories().forEach(categoryCrudService::create);
//        testDataGenerator.getIngredients().forEach(ingredientCrudService::create);
//        testDataGenerator.getRecipes().forEach(recipeCrudService::create);

        // Create panels
        var recipeTablePanel = new RecipeTablePanel(recipeTableModel, this::changeActionsState);
        var ingredientTablePanel = new IngredientTablePanel(ingredientTableModel, this::changeActionsState);
        var categoryTablePanel = new CategoryTablePanel(categoryTableModel, this::changeActionsState);
        var unitTablePanel = new UnitTablePanel(unitTableModel, this::changeActionsState);
        List<GeneralTablePanel<? extends Entity>> generalTablePanels = List.of(recipeTablePanel, ingredientTablePanel, categoryTablePanel, unitTablePanel);

        // Add the panels to tabbed pane
        this.tabbedPane = new JTabbedPane();
        generalTablePanels.forEach(panel -> tabbedPane.addTab(panel.getTablePanelType().getPluralName(), panel));
        tabbedPane.setBorder(padding);
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Add toolbar, menubar, status bar
        JLabel statusBar = createStatusBar();
        setStatusBarName(statusBar);

        // Add row sorters
        var recipeRowSorter = (TableRowSorter<RecipeTableModel>)  recipeTablePanel.getTable().getRowSorter();
        // adding listener to change text of status bar when filtering rows
        recipeRowSorter.addRowSorterListener(e -> setStatusBarName(statusBar));

        var filterToolBar = new FilterToolbar(dependencyProvider, recipeRowSorter);

        // Set up actions for recipe table
        addAction = new AddAction();
        deleteAction = new DeleteAction();
        editAction = new EditAction(dependencyProvider);
        openAction = new OpenAction();

        // create import/export actions
        exportAction = new ExportAction(recipeTablePanel, exportService);
        final Runnable importCallback = () -> {tableModels.forEach(BasicTableModel::refresh); filterToolBar.updateFilters(true);};
        importAction = new ImportAction(recipeTablePanel, importService,  importCallback);
        viewStatisticsAction = new ViewStatisticsAction(dependencyProvider);
        viewAboutAction = new ViewAboutAction();
        authAction = new AuthAction();
        this.actions = List.of(addAction, editAction, deleteAction, openAction, importAction, exportAction, viewAboutAction, viewStatisticsAction, authAction);
        actions.forEach(a -> a.setFilterToolbar(filterToolBar));
        setForbiddenActionsInTabs(); // TODO via GeneralAction
        setToDefaultActionEnablement(getCurrentTableIndex(tabbedPane));

        // Add popup menu
        for (GeneralTablePanel tablePanel : generalTablePanels) {
            tablePanel.getTable().setComponentPopupMenu(createTablePopupMenu(tablePanel.getTablePanelType()));
        }

        JPanel toolbarPanel = new JPanel(new GridLayout(2, 1));
        var toolbar = createToolbar();
        var filtersToolbar = filterToolBar.getFilterToolBar();
        toolbarPanel.add(toolbar);
        toolbarPanel.add(filtersToolbar);
        toolbarPanel.setBorder(padding);
        frame.add(toolbarPanel, BorderLayout.BEFORE_FIRST_LINE);

        var menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);
        frame.pack();

        // maps starting table to all actions
        setCurrentTableToActions(getCurrentTablePanelFromPanel(tabbedPane));
        tabbedPane.addChangeListener(new ChangeListener() {
            // when tab changes
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane tabPanel = (JTabbedPane) e.getSource();

                var currGeneralTable = getCurrentTablePanelFromPanel(tabPanel);
                setCurrentTableToActions(currGeneralTable);
                setToDefaultActionEnablement(getCurrentTableIndex(tabPanel));

                // clear all row selections
                generalTablePanels.forEach(r -> r.getTable().clearSelection());

                // filters visible only on recipe tab
                int currTabIndex = tabPanel.getSelectedIndex();
                tableModels.get(currTabIndex).refresh();
                filtersToolbar.setVisible(currTabIndex == RECIPE.ordinal());

                setStatusBarName(statusBar);

                filterToolBar.resetFilters();
            }
        });
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
        JPopupMenu menu = new JPopupMenu();
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
        toolbar.addSeparator(new Dimension(1000,10));
        toolbar.add(authAction);

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

        actions.forEach(GeneralAction::setShortDescription);
    }

    private <T extends Entity> void setCurrentTableToActions(GeneralTablePanel<T> generalTablePanel) {
        this.actions.forEach(x -> x.setGeneralTablePanel(generalTablePanel));
    }

    private Integer getCurrentTableIndex(JTabbedPane tab) {
        return tab.getSelectedIndex();
    }
    private JTable getCurrentTableFromPanel(JTabbedPane tab) {
        JPanel panel = (JPanel) tab.getComponentAt(getCurrentTableIndex(tab));
        JScrollPane scrollPane = (JScrollPane) panel.getComponent(0);
        return (JTable) scrollPane.getViewport().getView();
    }

    private <T extends Entity> GeneralTablePanel<T> getCurrentTablePanelFromPanel(JTabbedPane tab) {
        return (GeneralTablePanel<T>) tab.getComponentAt(getCurrentTableIndex(tab));
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
                INGREDIENT.ordinal(),
                CATEGORY.ordinal(),
                UNIT.ordinal());
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
}