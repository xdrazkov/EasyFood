package cz.muni.fi.pv168.project.storage.sql.entity;

import java.util.Objects;

/**
 * Representation of Department entity in a SQL database.
 */
public record IngredientEntity(String guid, String name, String defaultUnit, float caloriesPerUnit) {
    public IngredientEntity(String guid, String name, String defaultUnit, float caloriesPerUnit) {
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.defaultUnit = Objects.requireNonNull(defaultUnit, "name must not be null");
        this.caloriesPerUnit = caloriesPerUnit;
    }
}
