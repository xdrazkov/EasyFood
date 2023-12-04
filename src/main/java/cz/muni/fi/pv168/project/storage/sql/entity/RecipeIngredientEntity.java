package cz.muni.fi.pv168.project.storage.sql.entity;

import java.util.Objects;

/**
 * Representation of RecipeIngredient entity in a SQL database.
 */
public record RecipeIngredientEntity(String recipe, String ingredient, String unit, int amount) {
    public RecipeIngredientEntity(String recipe, String ingredient, String unit, int amount) {
        this.recipe = Objects.requireNonNull(recipe, "name must not be null");
        this.ingredient = Objects.requireNonNull(ingredient, "name must not be null");
        this.unit = Objects.requireNonNull(unit, "name must not be null");
        this.amount = amount;
    }
}
