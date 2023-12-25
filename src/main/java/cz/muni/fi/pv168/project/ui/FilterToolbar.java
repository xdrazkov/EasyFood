package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.service.crud.CrudService;
import cz.muni.fi.pv168.project.ui.filters.RecipeTableFilter;
import cz.muni.fi.pv168.project.ui.filters.components.FilterComboboxBuilder;
import cz.muni.fi.pv168.project.ui.filters.components.FilterListModelBuilder;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterCategoryValues;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterIngredientValues;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.rangeSlider.RangeSlider;
import cz.muni.fi.pv168.project.ui.rangeSlider.RecipeRangeSliderChangeListener;
import cz.muni.fi.pv168.project.ui.renderers.CategoryRenderer;
import cz.muni.fi.pv168.project.ui.renderers.IngredientRenderer;
import cz.muni.fi.pv168.project.ui.renderers.SpecialFilterCategoryValuesRenderer;
import cz.muni.fi.pv168.project.ui.renderers.SpecialFilterIngredientValuesRenderer;
import cz.muni.fi.pv168.project.util.Either;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;


public class FilterToolbar {

    private final CrudService<Recipe> recipeCrudService;
    private final CrudService<Ingredient> ingredientCrudService;
    private final CrudService<Category> categoryCrudService;
    private final CrudService<Unit> unitCrudService;
    private final TableRowSorter<RecipeTableModel> recipeRowSorter;

    private RangeSlider preparationTimeSlider;
    private RangeSlider nutritionalValuesSlider;

    private final RecipeTableFilter recipeTableFilter;

    private JComboBox<Either<SpecialFilterCategoryValues, Category>> categoryFilter;
    private JList<Either<SpecialFilterIngredientValues, Ingredient>> ingredientFilter;

    private final Button resetButton;

    private final JToolBar filterToolBar = new JToolBar();
    public FilterToolbar(CrudService<Recipe> recipeCrudService, CrudService<Ingredient> ingredientCrudService, CrudService<Category> categoryCrudService, CrudService<Unit> unitCrudService, TableRowSorter<RecipeTableModel> recipeRowSorter){
        this.recipeCrudService = recipeCrudService;
        this.ingredientCrudService = ingredientCrudService;
        this.categoryCrudService = categoryCrudService;
        this.unitCrudService = unitCrudService;
        this.recipeRowSorter = recipeRowSorter;

        this.recipeTableFilter = new RecipeTableFilter(recipeRowSorter);

        this.preparationTimeSlider = createRangeSlider(recipeTableFilter::filterPreparationTime,
                Recipe::getTimeToPrepare, "Preparation time (min)");
        this.nutritionalValuesSlider =  createRangeSlider(recipeTableFilter::filterNutritionalValues,
                        Recipe::getNutritionalValue, "Nutritional values (kcal)");

        this.categoryFilter = createCategoryFilter();
        this.ingredientFilter = createIngredientFilter();
        this.resetButton = createResetButton();
        addFiltersToToolbar();
    }

    private <T> RangeSlider createRangeSlider(Consumer<Either<T, Pair<Integer, Integer>>> filterFunction,
                                              Function<Recipe, Integer> mapperFunction,
                                              String description) {
        RangeSlider rangeSlider = new RangeSlider();
        updateSliderRange(rangeSlider, mapperFunction);

        int minValue = rangeSlider.getMinimum();
        int maxValue = rangeSlider.getMaximum();

        rangeSlider.setMajorTickSpacing((maxValue - minValue) / 5);
        rangeSlider.setMinorTickSpacing((maxValue - minValue) / 10);

        rangeSlider.setPaintTicks(true);
        rangeSlider.setPaintLabels(true);

        rangeSlider.setToolTipText(description);
        rangeSlider.setKnobsToDefaultPosition();

        rangeSlider.addChangeListener(new RecipeRangeSliderChangeListener<>(filterFunction));

        return rangeSlider;
    }

    /**
     * updates contents of all filters in recipe tab whenever any action is applied
     */
    public void updateFilters(boolean isRecipes) {
//        resetFilters();
//        updateSliderRange(preparationTimeSlider, Recipe::getTimeToPrepare);
//        updateSliderRange(nutritionalValuesSlider, Recipe::getNutritionalValue);

        this.preparationTimeSlider = createRangeSlider(recipeTableFilter::filterPreparationTime,
                Recipe::getTimeToPrepare, "Preparation time (min)");
        this.nutritionalValuesSlider =  createRangeSlider(recipeTableFilter::filterNutritionalValues,
                Recipe::getNutritionalValue, "Nutritional values (kcal)");
        categoryFilter = createCategoryFilter();
        ingredientFilter = createIngredientFilter();
        resetFilters();

        filterToolBar.removeAll();
        addFiltersToToolbar();
        // DO NOT ASK ANY QUESTIONS
        filterToolBar.setVisible(false);
        if (isRecipes) {
            filterToolBar.setVisible(true);
        }

    }

    public void resetFilters() {
        categoryFilter.setSelectedIndex(0);
        ingredientFilter.setSelectedIndex(0);

        nutritionalValuesSlider.setKnobsToDefaultPosition();
        preparationTimeSlider.setKnobsToDefaultPosition();
    }

    private JComboBox<Either<SpecialFilterCategoryValues, Category>> createCategoryFilter() {
        return FilterComboboxBuilder.create(SpecialFilterCategoryValues.class, categoryCrudService.findAll().toArray(new Category[0]))
                .setSelectedItem(SpecialFilterCategoryValues.ALL)
                .setSpecialValuesRenderer(new SpecialFilterCategoryValuesRenderer())
                .setValuesRenderer(new CategoryRenderer())
                .setFilter(recipeTableFilter::filterCategory)
                .build();
    }

    private JList<Either<SpecialFilterIngredientValues, Ingredient>> createIngredientFilter() {
        ListModel<Ingredient> listModel = new AbstractListModel<>() {
            @Override
            public int getSize() {
                return ingredientCrudService.findAll().size();
            }

            @Override
            public Ingredient getElementAt(int index) {
                return ingredientCrudService.findAll().get(index);
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

    private Button createResetButton() {
        var resetButton = new Button("Reset all filters");
        resetButton.addActionListener(e -> resetFilters());
        return resetButton;
    }

    private void addFiltersToToolbar() {
        // DO NOT CHANGE ORDER OF COMMANDS
        filterToolBar.add(categoryFilter);
        filterToolBar.add(new JScrollPane(ingredientFilter));
        filterToolBar.add(createSliderPanel(preparationTimeSlider));
        filterToolBar.add(createSliderPanel(nutritionalValuesSlider));
        filterToolBar.add(resetButton);
    }

    private JPanel createSliderPanel(RangeSlider slider) {
        Button resetButton = new Button("Reset slider");
        resetButton.addActionListener(e -> slider.setKnobsToDefaultPosition());
        JPanel buttonTextPanel = new JPanel(new GridLayout(1, 2));
        buttonTextPanel.add(resetButton);
        buttonTextPanel.add(new JLabel(slider.getToolTipText(), SwingConstants.CENTER));

        JPanel finalPanel = new JPanel(new GridLayout(2, 1));
        finalPanel.add(slider);
        finalPanel.add(buttonTextPanel);

        finalPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        return finalPanel;
    }

    private void updateSliderRange(RangeSlider rangeSlider, Function<Recipe, Integer> mapperFunction) {
        List<Integer> values = recipeCrudService.findAll().stream()
                .map(mapperFunction)
                .toList();

        int minValue = values.stream().mapToInt(Integer::intValue).min().orElse(0);
        int maxValue = values.stream().mapToInt(Integer::intValue).max().orElse(0);
        rangeSlider.setMinimum(minValue);
        rangeSlider.setMaximum(maxValue);
    }

    public RangeSlider getPreparationTimeSlider() {
        return preparationTimeSlider;
    }

    public RangeSlider getNutritionalValuesSlider() {
        return nutritionalValuesSlider;
    }

    public JToolBar getFilterToolBar() {
        return filterToolBar;
    }
}
