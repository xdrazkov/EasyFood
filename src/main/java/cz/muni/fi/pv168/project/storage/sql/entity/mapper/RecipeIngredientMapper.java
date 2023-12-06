package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.model.AmountInUnit;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.IngredientEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeIngredientEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.UnitEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeIngredientMapper {
    private final DataAccessObject<IngredientEntity> ingredientDao;
    private final DataAccessObject<UnitEntity> unitDao;
    private final EntityMapper<IngredientEntity, Ingredient> ingredientMapper;
    private final EntityMapper<UnitEntity, Unit> unitMapper;

    public RecipeIngredientMapper(
            DataAccessObject<IngredientEntity> ingredientDao,
            DataAccessObject<UnitEntity> unitDao,
            EntityMapper<IngredientEntity, Ingredient> ingredientMapper,
            EntityMapper<UnitEntity, Unit> unitMapper) {
        this.ingredientDao = ingredientDao;
        this.unitDao = unitDao;
        this.ingredientMapper = ingredientMapper;
        this.unitMapper = unitMapper;
    }

    public HashMap<Ingredient, AmountInUnit> mapToBusiness(List<RecipeIngredientEntity> dbRecipeIngredient) {
        HashMap<Ingredient, AmountInUnit> result = new HashMap<>();
        for (RecipeIngredientEntity recipeIngredientEntity : dbRecipeIngredient) {
            Ingredient ingredient = ingredientDao
                    .findByGuid(recipeIngredientEntity.ingredient())
                    .map(ingredientMapper::mapToBusiness)
                    .orElseThrow(() -> new DataStorageException("Ingredient not found, id: " +
                            recipeIngredientEntity.ingredient()));
            Unit unit = unitDao
                    .findByGuid(recipeIngredientEntity.unit())
                    .map(unitMapper::mapToBusiness)
                    .orElseThrow(() -> new DataStorageException("Unit not found, id: " +
                            recipeIngredientEntity.ingredient()));
            result.put(ingredient, new AmountInUnit(unit, recipeIngredientEntity.amount()));
        }
        return result;
    }

    public static ArrayList<RecipeIngredientEntity> getRecipeIngredientEntities(Recipe recipe) {
        ArrayList<RecipeIngredientEntity> result = new ArrayList<>();
        for (var entry : recipe.getIngredients().entrySet()) {
            Ingredient ingredient = entry.getKey();
            AmountInUnit amountInUnit = entry.getValue();
            result.add(new RecipeIngredientEntity(
                    recipe.getGuid(),
                    ingredient.getGuid(),
                    amountInUnit.getUnit().getGuid(),
                    amountInUnit.getAmount()));
        }
        return result;
    }
}
