package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.CategoryEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeEntity;

import java.util.HashMap;


/**
 * Mapper from the {@link RecipeEntity} to {@link Recipe}.
 */
public final class RecipeMapper implements EntityMapper<RecipeEntity, Recipe> {

    private final DataAccessObject<CategoryEntity> categoryDao;
    private final EntityMapper<CategoryEntity, Category> categoryMapper;

    public RecipeMapper(
            DataAccessObject<CategoryEntity> categoryDao,
            EntityMapper<CategoryEntity, Category> categoryMapper) {
        this.categoryDao = categoryDao;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Recipe mapToBusiness(RecipeEntity entity) {
        var category = categoryDao
                .findByGuid(entity.category())
                .map(categoryMapper::mapToBusiness)
                .orElseThrow(() -> new DataStorageException("Category not found, id: " +
                        entity.guid()));

        return new Recipe(
                entity.title(),
                entity.description(),
                entity.portionCount(),
                entity.instructions(),
                entity.timeToPrepare(),
                category,
                new HashMap<>() // TODO: Fix
        );
    }

    // TODO Fix?
    @Override
    public RecipeEntity mapNewEntityToDatabase(Recipe entity) {
        return getRecipeEntity(entity, null);
    }

    // TODO Fix?
    @Override
    public RecipeEntity mapExistingEntityToDatabase(Recipe entity, String guid) {
        return getRecipeEntity(entity, guid);
    }

    private static RecipeEntity getRecipeEntity(Recipe entity, String guid) {
        return new RecipeEntity(
                guid,
                entity.getTitle(),
                entity.getDescription(),
                entity.getPortionCount(),
                entity.getInstructions(),
                entity.getTimeToPrepare(),
                entity.getCategory().getGuid()
        );
    }
}
