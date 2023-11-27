package cz.muni.fi.pv168.project.storage.sql;

import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.repository.Repository;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.dao.RecipeIngredientDao;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeIngredientEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.RecipeIngredientMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link Repository} for {@link Recipe} entity using SQL database.
 *
 * @author Vojtech Sassmann
 */
public class RecipeSqlRepository implements Repository<Recipe> {

    private final DataAccessObject<RecipeEntity> recipeDao;
    private final RecipeIngredientDao recipeIngredientDao;
    private final EntityMapper<RecipeEntity, Recipe> recipeMapper;
    private final RecipeIngredientMapper recipeIngredientMapper;

    public RecipeSqlRepository(
            DataAccessObject<RecipeEntity> recipeDao,
            RecipeIngredientDao recipeIngredientDao,
            EntityMapper<RecipeEntity, Recipe> recipeMapper,
            RecipeIngredientMapper recipeIngredientMapper) {
        this.recipeDao = recipeDao;
        this.recipeMapper = recipeMapper;
        this.recipeIngredientDao = recipeIngredientDao;
        this.recipeIngredientMapper = recipeIngredientMapper;
    }

    @Override
    public List<Recipe> findAll() {
        return recipeDao
                .findAll()
                .stream()
                .map(recipeMapper::mapToBusiness)
                .peek(recipe -> recipe.setIngredients(recipeIngredientMapper.mapToBusiness(recipeIngredientDao.findByRecipeGuid(recipe.getGuid()))))
                .toList();
    }

    @Override
    public void create(Recipe newEntity) {
        recipeDao.create(recipeMapper.mapNewEntityToDatabase(newEntity));
        ArrayList<RecipeIngredientEntity> recipeIngredients = RecipeIngredientMapper.getRecipeIngredientEntities(newEntity);
        for (RecipeIngredientEntity recipeIngredientEntity : recipeIngredients) {
            recipeIngredientDao.create(recipeIngredientEntity);
        }
    }

    @Override
    public void update(Recipe entity) {
        var existingRecipe = recipeDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Recipe not found, guid: " + entity.getGuid()));
        var updatedRecipe = recipeMapper.mapExistingEntityToDatabase(entity, existingRecipe.guid());

        recipeDao.update(updatedRecipe);

        ArrayList<RecipeIngredientEntity> recipeIngredients = RecipeIngredientMapper.getRecipeIngredientEntities(entity);
        for (RecipeIngredientEntity recipeIngredientEntity : recipeIngredients) {
            var existsRecipeIngredient = recipeIngredientDao.findByGuid(entity.getGuid());

            if (existsRecipeIngredient.isPresent()) {
                recipeIngredientDao.update(recipeIngredientEntity);
            } else {
                recipeIngredientDao.create(recipeIngredientEntity);
            }
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        recipeDao.deleteByGuid(guid);
        recipeIngredientDao.deleteByRecipeGuid(guid);
    }

    @Override
    public void deleteAll() {
        recipeDao.deleteAll();
        recipeIngredientDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String guid) {
        return recipeDao.existsByGuid(guid);
    }

    @Override
    public Optional<Recipe> findByGuid(String guid) {
        Optional<Recipe> recipe = recipeDao
                .findByGuid(guid)
                .map(recipeMapper::mapToBusiness);

        if (recipe.isPresent()) {
            Recipe recipeValue = recipe.get();
            recipeValue.setIngredients(recipeIngredientMapper.mapToBusiness(recipeIngredientDao.findByRecipeGuid(guid)));
        }

        return recipe;
    }
}
