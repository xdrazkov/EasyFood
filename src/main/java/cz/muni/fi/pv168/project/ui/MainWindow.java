package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.data.TestDataGenerator;
import cz.muni.fi.pv168.project.export.json.BatchJsonExporter;
import cz.muni.fi.pv168.project.export.json.BatchJsonImporter;
import cz.muni.fi.pv168.project.export.pdf.BatchPdfExporter;
import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.service.CategoryDependencyChecker;
import cz.muni.fi.pv168.project.service.IngredientDependencyChecker;
import cz.muni.fi.pv168.project.service.RecipeDependencyChecker;
import cz.muni.fi.pv168.project.service.UnitDependencyChecker;
import cz.muni.fi.pv168.project.service.crud.GenericCrudService;
import cz.muni.fi.pv168.project.service.export.GenericExportService;
import cz.muni.fi.pv168.project.service.export.GenericImportService;
import cz.muni.fi.pv168.project.service.validation.CategoryValidator;
import cz.muni.fi.pv168.project.service.validation.IngredientValidator;
import cz.muni.fi.pv168.project.service.validation.RecipeValidator;
import cz.muni.fi.pv168.project.service.validation.UnitValidator;
import cz.muni.fi.pv168.project.storage.memory.InMemoryRepository;
import cz.muni.fi.pv168.project.storage.sql.TransactionalImportService;
import cz.muni.fi.pv168.project.ui.action.*;
import cz.muni.fi.pv168.project.ui.filters.RecipeTableFilter;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterCategoryValues;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.ui.panels.*;
import cz.muni.fi.pv168.project.ui.renderers.*;
import cz.muni.fi.pv168.project.ui.resources.Icons;
import cz.muni.fi.pv168.project.util.Either;
import cz.muni.fi.pv168.project.ui.filters.components.FilterComboboxBuilder;
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
    private final List<GeneralAction> actions;

    // key is list of indices of tabs, where action is not allowed
    private final Map<GeneralAction, List<Integer>> forbiddenActionsInTabs = new HashMap<>();

    private final JTabbedPane tabbedPane;
    private final Border padding = new EmptyBorder(0, 10, 0, 10);

    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    public MainWindow(DependencyProvider dependencyProvider) {
        frame = createFrame();
        frame.setMinimumSize(new Dimension(1400, 800));
        frame.setIconImage(Icons.APP_ICON);

        var recipeCrudService = dependencyProvider.getRecipeCrudService();
        var ingredientCrudService = dependencyProvider.getIngredientCrudService();
        var categoryCrudService = dependencyProvider.getCategoryCrudService();
        var unitCrudService = dependencyProvider.getUnitCrudService();

        recipeCrudService.setGeneralDependencyChecker(new RecipeDependencyChecker());
        ingredientCrudService.setGeneralDependencyChecker(new IngredientDependencyChecker(recipeCrudService));
        categoryCrudService.setGeneralDependencyChecker(new CategoryDependencyChecker(recipeCrudService));
        unitCrudService.setGeneralDependencyChecker(new UnitDependencyChecker(recipeCrudService, ingredientCrudService));

        // Create models
        RecipeTableModel recipeTableModel = new RecipeTableModel(recipeCrudService);
        IngredientTableModel ingredientTableModel = new IngredientTableModel(ingredientCrudService);
        CategoryTableModel categoryTableModel = new CategoryTableModel(categoryCrudService);
        UnitTableModel unitTableModel = new UnitTableModel(unitCrudService);
        List<BasicTableModel<? extends Entity>> tableModels =
                List.of(recipeTableModel, ingredientTableModel, categoryTableModel, unitTableModel);

        // Create panels
        var recipeTablePanel = new RecipeTablePanel(recipeTableModel, this::changeActionsState);
        var ingredientTablePanel = new IngredientTablePanel(ingredientTableModel, this::changeActionsState);;
        var categoryTablePanel = new CategoryTablePanel(categoryTableModel, this::changeActionsState);
        var unitTablePanel = new UnitTablePanel(unitTableModel, this::changeActionsState);
        List<GeneralTablePanel<? extends Entity>> generalTablePanels = List.of(recipeTablePanel, ingredientTablePanel, categoryTablePanel, unitTablePanel);

        // Add the panels to tabbed pane
        this.tabbedPane = new JTabbedPane();
        generalTablePanels.forEach(panel -> tabbedPane.addTab(panel.getTablePanelType().getPluralName(), panel));
        tabbedPane.setBorder(padding);
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Set up actions for recipe table
        addAction = new AddAction(unitTableModel, ingredientCrudService, categoryCrudService, unitCrudService);
        deleteAction = new DeleteAction();
        editAction = new EditAction(unitTableModel, ingredientCrudService, categoryCrudService, unitCrudService);
        openAction = new OpenAction();

        // Add row sorters
        var recipeRowSorter = (TableRowSorter<RecipeTableModel>)  recipeTablePanel.getTable().getRowSorter();

        // TODO exporters by DAO
        var exportService = new GenericExportService(recipeRowSorter ,recipeTablePanel, List.of(new BatchJsonExporter(), new BatchPdfExporter()));
        var importService = new GenericImportService(recipeCrudService, ingredientCrudService, categoryCrudService, List.of(new BatchJsonImporter()));

        var transactionalImportService = new TransactionalImportService(importService, dependencyProvider.getTransactionExecutor());

        // create import/export actions
        exportAction = new ExportAction(recipeTablePanel, exportService);
        importAction = new ImportAction(recipeTablePanel, transactionalImportService, () -> tableModels.forEach(BasicTableModel::refresh));

        viewStatisticsAction = new ViewStatisticsAction(ingredientCrudService, recipeCrudService);
        viewAboutAction = new ViewAboutAction();
        this.actions = List.of(addAction, editAction, deleteAction, openAction, importAction, exportAction, viewAboutAction, viewStatisticsAction);
        setForbiddenActionsInTabs();
        setToDefaultActionEnablement(getCurrentTableIndex(tabbedPane));


        // Add popup menu, toolbar, menubar, status bar
        generalTablePanels.forEach(
                r -> r.getTable().setComponentPopupMenu(createTablePopupMenu(r.getTablePanelType())));
        JLabel statusBar = createStatusBar();
        setStatusBarName(statusBar);

        // adding listener to change text of status bar when filtering rows
        recipeRowSorter.addRowSorterListener(e -> setStatusBarName(statusBar));

        var filterToolBar = new FilterToolbar(recipeCrudService, ingredientCrudService, categoryCrudService, unitCrudService, recipeRowSorter);
        actions.forEach(a -> a.setFilterToolbar(filterToolBar));

        JPanel toolbarPanel = new JPanel(new GridLayout(2, 1));
        var toolbar = createToolbar();
        var filtersToolbar = filterToolBar.getFilterToolBar();
        toolbarPanel.add(toolbar);
        toolbarPanel.add(filtersToolbar);
        toolbarPanel.setBorder(padding);
        frame.add(toolbarPanel, BorderLayout.BEFORE_FIRST_LINE);

        var menubar = createMenuBar();
        frame.setJMenuBar(menubar);
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
                filtersToolbar.setVisible(currTabIndex == RECIPE.ordinal());

                setStatusBarName(statusBar);

                filterToolBar.resetFilters();
            }
        });
    }

    private static JComboBox<Either<SpecialFilterCategoryValues, Category>> createCategoryFilter(
            RecipeTableFilter recipeTableFilter, CategoryTableModel categoryTableModel) {
        return FilterComboboxBuilder.create(SpecialFilterCategoryValues.class, categoryTableModel.getEntities().toArray(new Category[0]))
                .setSelectedItem(SpecialFilterCategoryValues.ALL)
                .setSpecialValuesRenderer(new SpecialFilterCategoryValuesRenderer())
                .setValuesRenderer(new CategoryRenderer())
                .setFilter(recipeTableFilter::filterCategory)
                .build();
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