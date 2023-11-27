package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.model.IngredientType;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.storage.sql.entity.UnitEntity;

/**
 * Mapper from the {@link UnitEntity} to {@link Unit}.
 */
public final class UnitMapper implements EntityMapper<UnitEntity, Unit> {

    @Override
    public Unit mapToBusiness(UnitEntity dbUnit) {
        return new Unit(
                dbUnit.guid(),
                dbUnit.name(),
                dbUnit.abbreviation(),
                IngredientType.valueOf(dbUnit.ingredientType()),
                dbUnit.conversionRate()
        );
    }

    @Override
    public UnitEntity mapNewEntityToDatabase(Unit entity) {
        return getUnitEntity(entity, null);
    }

    @Override
    public UnitEntity mapExistingEntityToDatabase(Unit entity, String guid) {
        return getUnitEntity(entity, guid);
    }

    private static UnitEntity getUnitEntity(Unit entity, String guid) {
        return new UnitEntity(
                entity.getGuid(),
                entity.getName(),
                entity.getAbbreviation(),
                entity.getIngredientType().name(),
                entity.getConversionRate()
        );
    }
}
