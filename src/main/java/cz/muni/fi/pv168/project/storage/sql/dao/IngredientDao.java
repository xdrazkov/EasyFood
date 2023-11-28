package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.project.storage.sql.entity.IngredientEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * DAO for {@link IngredientEntity} entity.
 */
public final class IngredientDao implements DataAccessObject<IngredientEntity> {

    private final Supplier<ConnectionHandler> connections;

    public IngredientDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public IngredientEntity create(IngredientEntity newIngredient) {
        var sql = "INSERT INTO Ingredient (guid, name, defaultUnit, caloriesPerUnit) VALUES (?, ?, ?, ?);";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newIngredient.guid());
            statement.setString(2, newIngredient.name());
            statement.setString(3, newIngredient.defaultUnit());
            statement.setFloat(4, newIngredient.caloriesPerUnit());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                String ingredientGuid;

                if (keyResultSet.next()) {
                    ingredientGuid = keyResultSet.getString(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newIngredient);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newIngredient);
                }

                return findByGuid(ingredientGuid).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newIngredient, ex);
        }
    }

    @Override
    public Collection<IngredientEntity> findAll() {
        var sql = """
                SELECT guid,
                       name,
                       defaultUnit,
                       caloriesPerUnit
                FROM Ingredient
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            List<IngredientEntity> ingredients = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var ingredient = ingredientFromResultSet(resultSet);
                    ingredients.add(ingredient);
                }
            }

            return ingredients;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all ingredients", ex);
        }
    }

    @Override
    public Optional<IngredientEntity> findByGuid(String guid) {
        var sql = """
                SELECT guid,
                       name,
                       defaultUnit,
                       caloriesPerUnit
                FROM Ingredient
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ingredientFromResultSet(resultSet));
            } else {
                // ingredient not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ingredient by guid: " + guid, ex);
        }
    }

    @Override
    public IngredientEntity update(IngredientEntity entity) {
        var sql = """
                UPDATE Ingredient
                SET name = ?,
                    defaultUnit = ?,
                    caloriesPerUnit = ?
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.name());
            statement.setString(2, entity.defaultUnit());
            statement.setFloat(3, entity.caloriesPerUnit());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Ingredient not found, guid: " + entity.guid());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 ingredient (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update ingredient: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = """
                DELETE FROM Ingredient
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Ingredient not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 ingredient (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete ingredient, guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Ingredient";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all ingredients", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT name
                FROM Ingredient
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
            throw new DataStorageException("Failed to check if ingredient exists, guid: " + guid, ex);
        }
    }

    private static IngredientEntity ingredientFromResultSet(ResultSet resultSet) throws SQLException {
        return new IngredientEntity(
                resultSet.getString("guid"),
                resultSet.getString("name"),
                resultSet.getString("defaultUnit"),
                resultSet.getFloat("caloriesPerUnit")
        );
    }
}
