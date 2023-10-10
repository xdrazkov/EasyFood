package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.ui.action.AddAction;
import cz.muni.fi.pv168.project.ui.action.DeleteAction;
import cz.muni.fi.pv168.project.ui.action.EditAction;
import cz.muni.fi.pv168.project.ui.action.OpenAction;
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

    public MainWindow() {
        frame = createFrame();

        // Generate test objects
        ArrayList<Recipe> recipes = new ArrayList<>();
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category("Category 1", Category.DEFAULT_COLOR));
        categories.add(new Category("Category 2", Category.DEFAULT_COLOR));
        categories.add(new Category("Category 3", Category.DEFAULT_COLOR));
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Ingredient 1"));
        ingredients.add(new Ingredient("Ingredient 2"));
        ingredients.add(new Ingredient("Ingredient 3"));
        recipes.add(new Recipe("Recipe 1", "Description for Recipe 1", 2, "", 2, categories.get(0), ingredients));
        recipes.add(new Recipe("Recipe 2", "Description for Recipe 2", 2, "", 2, categories.get(1), ingredients));
        recipes.add(new Recipe("Recipe 3", "Description for Recipe 3", 2, "", 2, categories.get(2), ingredients));

        ArrayList<Unit> units = new ArrayList<>();
        units.add(new Unit("Unit 1", IngredientType.COUNTABLE));
        units.add(new Unit("Unit 2", IngredientType.POURABLE));
        units.add(new Unit("Unit 3", IngredientType.WEIGHABLE));

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
        return menu;
    }

    private JMenuBar createMenuBar() {
        var menuBar = new JMenuBar();
        var editMenu = new JMenu("Edit");
        editMenu.add(openAction);
        editMenu.add(editAction);
        editMenu.add(addAction);
        editMenu.add(deleteAction);
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
        return toolbar;
    }

    private void changeActionsState(int selectedItemsCount) {
        openAction.setEnabled(selectedItemsCount == 1);
        editAction.setEnabled(selectedItemsCount == 1);
        deleteAction.setEnabled(selectedItemsCount >= 1);
    }
}
