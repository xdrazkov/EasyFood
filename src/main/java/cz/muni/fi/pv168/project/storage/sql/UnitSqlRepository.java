package cz.muni.fi.pv168.project.storage.sql;

import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.repository.Repository;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.UnitEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link Repository} for {@link Unit} entity using SQL database.
 *
 * @author Vojtech Sassmann
 */
public class UnitSqlRepository implements Repository<Unit> {

    private final DataAccessObject<UnitEntity> unitDao;
    private final EntityMapper<UnitEntity, Unit> unitMapper;

    public UnitSqlRepository(
            DataAccessObject<UnitEntity> unitDao,
            EntityMapper<UnitEntity, Unit> unitMapper) {
        this.unitDao = unitDao;
        this.unitMapper = unitMapper;
    }

    @Override
    public List<Unit> findAll() {
        return unitDao
                .findAll()
                .stream()
                .map(unitMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(Unit newEntity) {
        unitDao.create(unitMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(Unit entity) {
        var existingUnit = unitDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Unit not found, guid: " + entity.getGuid()));
        var updatedUnit = unitMapper.mapExistingEntityToDatabase(entity, existingUnit.guid());

        unitDao.update(updatedUnit);
    }

    @Override
    public void deleteByGuid(String guid) {
        unitDao.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        unitDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String guid) {
        return unitDao.existsByGuid(guid);
    }

    @Override
    public Optional<Unit> findByGuid(String guid) {
        return unitDao
                .findByGuid(guid)
                .map(unitMapper::mapToBusiness);
    }
}
