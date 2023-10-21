package cz.muni.fi.pv168.project.ui.filters;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.ui.filters.matchers.*;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterCategoryValues;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterIngredientValues;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterNutritionalValueValues;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterPreparationTimeValues;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.util.Either;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.table.TableRowSorter;
import java.util.stream.Stream;

/**
 * Class holding all filters for the RecipeTable.
 */
public final class RecipeTableFilter {
    private final RecipeCompoundMatcher recipeCompoundMatcher;

    public RecipeTableFilter(TableRowSorter<RecipeTableModel> rowSorter) {
        recipeCompoundMatcher = new RecipeCompoundMatcher(rowSorter);
        rowSorter.setRowFilter(recipeCompoundMatcher);
    }

    public void filterCategory(Either<SpecialFilterCategoryValues, Category> selectedItem) {
        selectedItem.apply(
                l -> recipeCompoundMatcher.setCategoryMatcher(l.getMatcher()),
                r -> recipeCompoundMatcher.setCategoryMatcher(new CategoryMatcher(r))
        );
    }

    public void filterIngredient(Either<SpecialFilterIngredientValues, Ingredient> selectedItem) {
        selectedItem.apply(
                l -> recipeCompoundMatcher.setIngredientMatcher(l.getMatcher()),
                r -> recipeCompoundMatcher.setIngredientMatcher(new IngredientMatcher(r))
        );
    }

    public void filterPreparationTime(Either<SpecialFilterPreparationTimeValues, Pair<Integer, Integer>> selectedItem) {
        selectedItem.apply(
                l -> recipeCompoundMatcher.setPreparationTimeMatcher(l.getMatcher()),
                r -> recipeCompoundMatcher.setPreparationTimeMatcher(new PreparationTimeMatcher(r))
        );
    }

    public void filterNutritionalValues(Either<SpecialFilterNutritionalValueValues, Pair<Integer, Integer>> selectedItem) {
        selectedItem.apply(
                l -> recipeCompoundMatcher.setNutritionalValuesMatcher(l.getMatcher()),
                r -> recipeCompoundMatcher.setNutritionalValuesMatcher(new NutritionalValuesMatcher(r))
        );
    }

    private static class RecipeCompoundMatcher extends EntityMatcher<Recipe> {

        private final TableRowSorter<RecipeTableModel> rowSorter;
        private EntityMatcher<Recipe> categoryMatcher = EntityMatchers.all();

        private EntityMatcher<Recipe> ingredientMatcher = EntityMatchers.all();

        private EntityMatcher<Recipe> preparationTimeMatcher = EntityMatchers.all();

        private EntityMatcher<Recipe> nutritionalValuesMatcher = EntityMatchers.all();

        private RecipeCompoundMatcher(TableRowSorter<RecipeTableModel> rowSorter) {
            this.rowSorter = rowSorter;
        }

        private void setCategoryMatcher(EntityMatcher<Recipe> categoryMatcher) {
            this.categoryMatcher = categoryMatcher;
            rowSorter.sort();
        }

        private void setIngredientMatcher(EntityMatcher<Recipe> ingredientMatcher) {
            this.ingredientMatcher = ingredientMatcher;
            rowSorter.sort();
        }

        private void setPreparationTimeMatcher(EntityMatcher<Recipe> preparationTimeMatcher) {
            this.preparationTimeMatcher = preparationTimeMatcher;
            rowSorter.sort();
        }

        private void setNutritionalValuesMatcher(EntityMatcher<Recipe> nutritionalValuesMatcher) {
            this.nutritionalValuesMatcher = nutritionalValuesMatcher;
            rowSorter.sort();
        }

        @Override
        public boolean evaluate(Recipe recipe) {
            return Stream.of(categoryMatcher, ingredientMatcher, preparationTimeMatcher)
                    .allMatch(m -> m.evaluate(recipe)) ;
        }
    }
}