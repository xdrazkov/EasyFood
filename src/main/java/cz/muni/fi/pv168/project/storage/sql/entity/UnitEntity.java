package cz.muni.fi.pv168.project.storage.sql.entity;

import cz.muni.fi.pv168.project.model.IngredientType;

import java.util.Objects;

/**
 * Representation of Department entity in a SQL database.
 */
public record UnitEntity(String guid, String name, String abbreviation, String ingredientType, float conversionRate) {
    public UnitEntity(String guid, String name, String abbreviation, String ingredientType, float conversionRate) {
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.abbreviation = Objects.requireNonNull(abbreviation, "name must not be null");
        this.ingredientType = Objects.requireNonNull(ingredientType, "name must not be null");
        this.conversionRate = conversionRate;
    }
}
