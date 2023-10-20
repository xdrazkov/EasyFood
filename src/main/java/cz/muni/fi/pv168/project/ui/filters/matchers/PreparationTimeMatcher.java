package cz.muni.fi.pv168.project.ui.filters.matchers;

import cz.muni.fi.pv168.project.model.Recipe;

public class PreparationTimeMatcher extends EntityMatcher<Recipe>{

    private final int preparationTime;

    public PreparationTimeMatcher(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    @Override
    public boolean evaluate(Recipe entity) {
        return entity.getTimeToPrepare() <= preparationTime;
    }
}
