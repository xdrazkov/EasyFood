package cz.muni.fi.pv168.project.ui.filters.matchers;

import cz.muni.fi.pv168.project.model.Recipe;
import org.apache.commons.lang3.tuple.Pair;

public class NutritionalValuesMatcher extends EntityMatcher<Recipe>{

    private final int nutritionalValueLower;
    private final int nutritionalValueUpper;

    public NutritionalValuesMatcher(Pair<Integer, Integer> bounds) {
        this.nutritionalValueLower = bounds.getLeft();
        this.nutritionalValueUpper = bounds.getRight();
    }

    @Override
    public boolean evaluate(Recipe entity) {
        return this.nutritionalValueLower <= entity.getNutritionalValue() &&
                entity.getNutritionalValue() <= this.nutritionalValueUpper;
    }
}