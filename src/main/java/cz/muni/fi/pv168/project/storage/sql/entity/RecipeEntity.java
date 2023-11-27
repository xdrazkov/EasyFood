package cz.muni.fi.pv168.project.storage.sql.entity;

import java.util.Objects;

/**
 * Representation of Department entity in a SQL database.
 */
public record RecipeEntity(
        String guid,
        String title,
        String description,
        int portionCount,
        String instructions,
        int timeToPrepare,
        String category) {
    public RecipeEntity(String guid,
                        String title,
                        String description,
                        int portionCount,
                        String instructions,
                        int timeToPrepare,
                        String category) {
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.title = Objects.requireNonNull(title, "name must not be null");
        this.description = Objects.requireNonNull(description, "name must not be null");
        this.portionCount = portionCount;
        this.instructions = Objects.requireNonNull(instructions, "name must not be null");
        this.timeToPrepare = timeToPrepare;
        this.category = Objects.requireNonNull(category, "name must not be null");
    }
}
