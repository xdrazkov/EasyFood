package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.storage.sql.entity.CategoryEntity;

/**
 * Mapper from the {@link CategoryEntity} to {@link Category}.
 */
public final class CategoryMapper implements EntityMapper<CategoryEntity, Category> {

    @Override
    public Category mapToBusiness(CategoryEntity dbCategory) {
        return new Category(
                dbCategory.guid(),
                dbCategory.name(),
                dbCategory.color()
        );
    }

    @Override
    public CategoryEntity mapNewEntityToDatabase(Category entity) {
        return getCategoryEntity(entity, null);
    }

    @Override
    public CategoryEntity mapExistingEntityToDatabase(Category entity, String guid) {
        return getCategoryEntity(entity, guid);
    }

    private static CategoryEntity getCategoryEntity(Category entity, String guid) {
        return new CategoryEntity(
                entity.getGuid(),
                entity.getName(),
                entity.getColorInInt()
                );
    }
}
