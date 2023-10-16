package cz.muni.fi.pv168.project.ui.filters.matchers;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Recipe;

import java.util.Objects;

public class CategoryMatcher extends EntityMatcher<Recipe>{

    private final Category category;

    public CategoryMatcher(Category category) {
        this.category = category;
    }

    @Override
    public boolean evaluate(Recipe entity) {
        return  Objects.equals(entity.getCategory(), category);
    }
}
