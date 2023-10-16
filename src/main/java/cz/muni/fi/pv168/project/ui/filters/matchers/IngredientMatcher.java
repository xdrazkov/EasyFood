package cz.muni.fi.pv168.project.ui.filters.matchers;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;

public class IngredientMatcher extends EntityMatcher<Recipe>{

    private final Ingredient ingredient;

    public IngredientMatcher(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public boolean evaluate(Recipe entity) {
        return entity.getIngredientList().contains(ingredient);
    }
}
