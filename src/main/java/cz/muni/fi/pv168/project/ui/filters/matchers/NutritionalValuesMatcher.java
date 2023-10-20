package cz.muni.fi.pv168.project.ui.filters.matchers;

import cz.muni.fi.pv168.project.model.Recipe;

public class NutritionalValuesMatcher extends EntityMatcher<Recipe>{

    private final int nutritionalValue;

    public NutritionalValuesMatcher(int nutritionalValue) {
        this.nutritionalValue = nutritionalValue;
    }

    @Override
    public boolean evaluate(Recipe entity) {
        return entity.getNutritionalValue() <= nutritionalValue;
    }
}