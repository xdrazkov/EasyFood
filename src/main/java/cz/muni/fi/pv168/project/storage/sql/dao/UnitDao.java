package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.project.storage.sql.entity.UnitEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * DAO for {@link UnitEntity} entity.
 */
public final class UnitDao implements DataAccessObject<UnitEntity> {

    private final Supplier<ConnectionHandler> connections;

    public UnitDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public UnitEntity create(UnitEntity newUnit) {
        var sql = "INSERT INTO Unit (guid, name, abbreviation, ingredientType, conversionRate) VALUES (?, ?, ?, ?, ?);";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newUnit.guid());
            statement.setString(2, newUnit.name());
            statement.setString(3, newUnit.abbreviation());
            statement.setString(4, newUnit.ingredientType());
            statement.setFloat(5, newUnit.conversionRate());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                String unitGuid;

                if (keyResultSet.next()) {
                    unitGuid = keyResultSet.getString(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newUnit);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newUnit);
                }

                return findByGuid(unitGuid).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newUnit, ex);
        }
    }

    @Override
    public Collection<UnitEntity> findAll() {
        var sql = """
                SELECT guid,
                       name,
                       abbreviation,
                       ingredientType,
                       conversionRate
                FROM Unit
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            List<UnitEntity> units = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var unit = unitFromResultSet(resultSet);
                    units.add(unit);
                }
            }

            return units;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all units", ex);
        }
    }

    @Override
    public Optional<UnitEntity> findByGuid(String guid) {
        var sql = """
                SELECT guid,
                       name,
                       abbreviation,
                       ingredientType,
                       conversionRate
                FROM Unit
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(unitFromResultSet(resultSet));
            } else {
                // unit not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load unit by guid: " + guid, ex);
        }
    }

    @Override
    public UnitEntity update(UnitEntity entity) {
        var sql = """
                UPDATE Unit
                SET name = ?,
                    abbreviation = ?,
                    ingredientType = ?,
                    conversionRate = ?
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.abbreviation());
            statement.setString(3, entity.ingredientType());
            statement.setFloat(4, entity.conversionRate());
            statement.setString(5, entity.guid());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Unit not found, guid: " + entity.guid());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 unit (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update unit: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = """
                DELETE FROM Unit
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Unit not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 unit (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete unit, guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Unit";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all units", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT name
                FROM Unit
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            try (var resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if unit exists, guid: " + guid, ex);
        }
    }

    private static UnitEntity unitFromResultSet(ResultSet resultSet) throws SQLException {
        return new UnitEntity(
                resultSet.getString("guid"),
                resultSet.getString("name"),
                resultSet.getString("abbreviation"),
                resultSet.getString("ingredientType"),
                resultSet.getFloat("conversionRate")
        );
    }
}
