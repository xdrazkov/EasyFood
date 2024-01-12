package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeIngredientEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * DAO for {@link RecipeIngredientEntity} entity.
 */
public final class RecipeIngredientDao implements DataAccessObject<RecipeIngredientEntity> {

    private final Supplier<ConnectionHandler> connections;

    public RecipeIngredientDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public RecipeIngredientEntity create(RecipeIngredientEntity newRecipeIngredient) {
        var sql = "INSERT INTO RecipeIngredient (recipe, ingredient, unit, amount) VALUES (?, ?, ?, ?);";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newRecipeIngredient.recipe());
            statement.setString(2, newRecipeIngredient.ingredient());
            statement.setString(3, newRecipeIngredient.unit());
            statement.setInt(4, newRecipeIngredient.amount());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long recipeIngredientId;

                if (keyResultSet.next()) {
                    recipeIngredientId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newRecipeIngredient);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newRecipeIngredient);
                }

                return findById(recipeIngredientId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newRecipeIngredient, ex);
        }
    }

    @Override
    public Collection<RecipeIngredientEntity> findAll() {
        var sql = """
                SELECT recipe,
                       ingredient,
                       unit,
                       amount
                FROM RecipeIngredient
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            List<RecipeIngredientEntity> recipeIngredients = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var recipeIngredient = recipeIngredientFromResultSet(resultSet);
                    recipeIngredients.add(recipeIngredient);
                }
            }

            return recipeIngredients;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all recipeIngredients", ex);
        }
    }

    @Override
    public Optional<RecipeIngredientEntity> findByGuid(String guid) {
        return Optional.empty();
    }

    public List<RecipeIngredientEntity> findByRecipeGuid(String guid) {
        ArrayList<RecipeIngredientEntity> result = new ArrayList<>();
        var sql = """
                SELECT recipe,
                       ingredient,
                       unit,
                       amount
                FROM RecipeIngredient
                WHERE recipe = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var recipeIngredient = recipeIngredientFromResultSet(resultSet);
                result.add(recipeIngredient);
            }
            return result;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load recipeIngredient by recipe guid: " + guid, ex);
        }
    }

    public Optional<RecipeIngredientEntity> findById(long id) {
        var sql = """
                SELECT recipe,
                       ingredient,
                       unit,
                       amount
                FROM RecipeIngredient
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, id);

            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(recipeIngredientFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }

        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load recipeIngredient by id: " + id, ex);
        }
    }

    @Override
    public RecipeIngredientEntity update(RecipeIngredientEntity entity) {
        var sql = """
                UPDATE RecipeIngredient
                SET unit = ?,
                    amount
                WHERE recipe = ? AND ingredient = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.unit());
            statement.setInt(2, entity.amount());
            statement.setString(3, entity.ingredient());
            statement.setString(4, entity.recipe());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("RecipeIngredient not found, guid: " + entity.recipe() + " - " + entity.ingredient());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 recipeIngredient (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update recipeIngredient: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        return;
    }

    public void deleteByRecipeGuid(String guid) {
        var sql = """
                DELETE FROM RecipeIngredient
                WHERE recipe = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete recipeIngredient, guid: " + guid, ex);
        }
    }

    public void deleteByRecipeGuidIngredientName(String guid, String ingredient) {
        var sql = """
                DELETE FROM RecipeIngredient
                WHERE recipe = ? AND ingredient = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            statement.setString(2, ingredient);
            int rowsUpdated = statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete recipeIngredient, guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM RecipeIngredient";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all recipeIngredients", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        return false;
    }

    public boolean existsByRecipeIngredient(String recipe, String ingredient) {
        var sql = """
                SELECT name
                FROM RecipeIngredient
                WHERE recipe = ? AND ingredient = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, recipe);
            statement.setString(2, ingredient);
            try (var resultSet = statement.executeQuery()) {
                return resultSet.next();
            } catch (SQLException ex) {
                return false;
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if recipeIngredient exists", ex);
        }
    }

    private static RecipeIngredientEntity recipeIngredientFromResultSet(ResultSet resultSet) throws SQLException {
        return new RecipeIngredientEntity(
                resultSet.getString("recipe"),
                resultSet.getString("ingredient"),
                resultSet.getString("unit"),
                resultSet.getInt("amount")
        );
    }
}
