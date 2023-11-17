package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Recipe;

import java.util.List;

public class RecipeTableModel extends BasicTableModel<Recipe> {
    public RecipeTableModel(List<Recipe> recipes) {
        super(recipes);
    }

    public List<Column<Recipe, ?>> makeColumns() {
        return List.of(
                Column.readonly("Title", String.class, Recipe::getTitle),
                Column.readonly("Description", String.class, Recipe::getDescription),
                Column.readonly("Category", Category.class, Recipe::getCategory),
                Column.readonly("Preparation time (min)", Integer.class, Recipe::getTimeToPrepare),
                Column.readonly("Nutritional value (kcal)", Integer.class, Recipe::getNutritionalValue)
        );
    }
}
