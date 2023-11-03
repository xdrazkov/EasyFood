package cz.muni.fi.pv168.project.ui.filters.matchers;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;

import java.util.Collection;
import java.util.List;

public class IngredientsMatcher extends EntityMatcher<Recipe>{
    private final Collection<Ingredient> selectedIngredients;

    public IngredientsMatcher(Ingredient selectedIngredient) {
        this.selectedIngredients = List.of(selectedIngredient);
    }

    public IngredientsMatcher(Collection<Ingredient> selectedIngredients) {
        this.selectedIngredients = selectedIngredients;
    }

    @Override
    public boolean evaluate(Recipe recipe) {

        for (Ingredient ingredient : selectedIngredients.toArray(new Ingredient[0])) {
            if (recipe.getIngredients().containsKey(ingredient)) {
                return true;
            }
        }
        return false;
    }
}
