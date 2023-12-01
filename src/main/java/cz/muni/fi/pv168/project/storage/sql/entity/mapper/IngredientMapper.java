package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.IngredientEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.UnitEntity;


/**
 * Mapper from the {@link IngredientEntity} to {@link Ingredient}.
 */
public final class IngredientMapper implements EntityMapper<IngredientEntity, Ingredient> {

    private final DataAccessObject<UnitEntity> unitDao;
    private final EntityMapper<UnitEntity, Unit> unitMapper;

    public IngredientMapper(
            DataAccessObject<UnitEntity> unitDao,
            EntityMapper<UnitEntity, Unit> unitMapper) {
        this.unitDao = unitDao;
        this.unitMapper = unitMapper;
    }

    @Override
    public Ingredient mapToBusiness(IngredientEntity entity) {
        var unit = unitDao
                .findByGuid(entity.defaultUnit())
                .map(unitMapper::mapToBusiness)
                .orElseThrow(() -> new DataStorageException("Unit not found, id: " +
                        entity.guid()));

        return new Ingredient(
                entity.guid(),
                entity.name(),
                unit,
                (int) entity.caloriesPerUnit()
        );
    }

    // TODO Fix?
    @Override
    public IngredientEntity mapNewEntityToDatabase(Ingredient entity) {
        return getIngredientEntity(entity, entity.getGuid());
    }

    // TODO Fix?
    @Override
    public IngredientEntity mapExistingEntityToDatabase(Ingredient entity, String guid) {
        return getIngredientEntity(entity, guid);
    }

    private static IngredientEntity getIngredientEntity(Ingredient entity, String guid) {
        return new IngredientEntity(
                guid,
                entity.getName(),
                entity.getDefaultUnit().getGuid(),
                entity.getCaloriesPerUnit()
        );
    }
}
