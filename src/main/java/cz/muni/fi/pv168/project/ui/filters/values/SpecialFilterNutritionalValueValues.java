package cz.muni.fi.pv168.project.ui.filters.values;

import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;

import java.util.Objects;

public class SpecialFilterNutritionalValueValues {
    private final EntityMatcher<Recipe> matcher;

    SpecialFilterNutritionalValueValues(EntityMatcher<Recipe> matcher) {
        this.matcher = Objects.requireNonNull(matcher, "matcher cannot be null");
    }

    public EntityMatcher<Recipe> getMatcher() {
        return matcher;
    }
}
