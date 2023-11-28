package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.project.storage.sql.entity.CategoryEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * DAO for {@link CategoryEntity} entity.
 */
public final class CategoryDao implements DataAccessObject<CategoryEntity> {

    private final Supplier<ConnectionHandler> connections;

    public CategoryDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public CategoryEntity create(CategoryEntity newCategory) {
        var sql = "INSERT INTO Category (guid, name, color) VALUES (?, ?, ?);";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newCategory.guid());
            statement.setString(2, newCategory.name());
            statement.setInt(3, newCategory.color());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                String categoryGuid;

                if (keyResultSet.next()) {
                    categoryGuid = keyResultSet.getString(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newCategory);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newCategory);
                }

                return findByGuid(categoryGuid).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newCategory, ex);
        }
    }

    @Override
    public Collection<CategoryEntity> findAll() {
        var sql = """
                SELECT guid,
                       name,
                       color
                FROM Category
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            List<CategoryEntity> categories = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var category = categoryFromResultSet(resultSet);
                    categories.add(category);
                }
            }

            return categories;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all categories", ex);
        }
    }

    @Override
    public Optional<CategoryEntity> findByGuid(String guid) {
        var sql = """
                SELECT guid,
                       name,
                       color
                FROM Category
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(categoryFromResultSet(resultSet));
            } else {
                // category not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load category by guid: " + guid, ex);
        }
    }

    @Override
    public CategoryEntity update(CategoryEntity entity) {
        var sql = """
                UPDATE Category
                SET name = ?,
                    color = ?
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.name());
            statement.setInt(2, entity.color());
            statement.setString(3, entity.guid());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Category not found, guid: " + entity.guid());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 category (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update category: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = """
                DELETE FROM Category
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Category not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 category (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete category, guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Category";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all categories", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT name
                FROM Category
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
            throw new DataStorageException("Failed to check if category exists, guid: " + guid, ex);
        }
    }

    private static CategoryEntity categoryFromResultSet(ResultSet resultSet) throws SQLException {
        return new CategoryEntity(
                resultSet.getString("guid"),
                resultSet.getString("name"),
                resultSet.getInt("color")
                );
    }
}
