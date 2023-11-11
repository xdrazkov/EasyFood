package cz.muni.fi.pv168.project.ui.filters.matchers;

import cz.muni.fi.pv168.project.model.Recipe;

import java.util.Collection;

public class IngredientsCompoundMatcher extends EntityMatcher<Recipe>{


    private final Collection<EntityMatcher<Recipe>> ingredientsMatchers;

    public IngredientsCompoundMatcher(Collection<EntityMatcher<Recipe>> ingredientsMatchers) {
        this.ingredientsMatchers = ingredientsMatchers;
    }

    @Override
    public boolean evaluate(Recipe entity) {
        return ingredientsMatchers.stream()
                .anyMatch(matcher -> matcher.evaluate(entity));
    }
}
