package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * DAO for {@link RecipeEntity} entity.
 */
public final class RecipeDao implements DataAccessObject<RecipeEntity> {

    private final Supplier<ConnectionHandler> connections;

    public RecipeDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public RecipeEntity create(RecipeEntity newRecipe) {
        var sql = "INSERT INTO Recipe (guid, title, description, portionCount, instructions, timeToPrepare, category) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newRecipe.guid());
            statement.setString(2, newRecipe.title());
            statement.setString(3, newRecipe.description());
            statement.setInt(4, newRecipe.portionCount());
            statement.setString(5, newRecipe.instructions());
            statement.setInt(6, newRecipe.timeToPrepare());
            statement.setString(7, newRecipe.category());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long recipeId;

                if (keyResultSet.next()) {
                    recipeId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newRecipe);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newRecipe);
                }

                return findByGuid(newRecipe.guid()).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newRecipe, ex);
        }
    }

    @Override
    public Collection<RecipeEntity> findAll() {
        var sql = """
                SELECT guid,
                       title,
                       description,
                       portionCount,
                       instructions,
                       timeToPrepare,
                       category
                FROM Recipe
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            List<RecipeEntity> recipes = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var recipe = recipeFromResultSet(resultSet);
                    recipes.add(recipe);
                }
            }

            return recipes;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all recipes", ex);
        }
    }

    @Override
    public Optional<RecipeEntity> findByGuid(String guid) {
        var sql = """
                SELECT guid,
                       title,
                       description,
                       portionCount,
                       instructions,
                       timeToPrepare,
                       category
                FROM Recipe
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(recipeFromResultSet(resultSet));
            } else {
                // recipe not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load recipe by guid: " + guid, ex);
        }
    }

    @Override
    public RecipeEntity update(RecipeEntity entity) {
        var sql = """
                UPDATE Recipe
                SET title = ?,
                    description = ?,
                    portionCount = ?,
                    instructions = ?,
                    timeToPrepare = ?,
                    category = ?
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.title());
            statement.setString(2, entity.description());
            statement.setInt(3, entity.portionCount());
            statement.setString(4, entity.instructions());
            statement.setInt(5, entity.timeToPrepare());
            statement.setString(6, entity.category());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Recipe not found, guid: " + entity.guid());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 recipe (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update recipe: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = """
                DELETE FROM Recipe
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Recipe not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 recipe (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete recipe, guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Recipe";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all recipes", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT name
                FROM Recipe
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
            throw new DataStorageException("Failed to check if recipe exists, guid: " + guid, ex);
        }
    }

    private static RecipeEntity recipeFromResultSet(ResultSet resultSet) throws SQLException {
        return new RecipeEntity(
                resultSet.getString("guid"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getInt("portionCount"),
                resultSet.getString("instructions"),
                resultSet.getInt("timeToPrepare"),
                resultSet.getString("category")
        );
    }
}
