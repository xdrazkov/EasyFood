package cz.muni.fi.pv168.project.ui.filters.matchers;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import org.apache.commons.lang3.tuple.Pair;

public class PreparationTimeMatcher extends EntityMatcher<Recipe>{

    private final int preparationTimeLower;
    private final int preparationTimeUpper;

    public PreparationTimeMatcher(Pair<Integer, Integer> bounds) {
        this.preparationTimeLower = bounds.getLeft();
        this.preparationTimeUpper = bounds.getRight();
    }

    @Override
    public boolean evaluate(Recipe entity) {
        return this.preparationTimeLower <= entity.getTimeToPrepare() &&
                entity.getTimeToPrepare() <= preparationTimeUpper;
    }
}
