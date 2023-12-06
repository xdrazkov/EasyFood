package cz.muni.fi.pv168.project.service;

import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.service.crud.CrudService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class UnitDependencyChecker extends GeneralDependencyChecker<Unit> {
    public UnitDependencyChecker(CrudService<Recipe> recipeCrudService, CrudService<Ingredient> ingredientCrudService) {
        this.recipeCrudService = recipeCrudService;
        this.ingredientCrudService = ingredientCrudService;
    }
    @Override
    public Collection<Entity> getDependentEntities(Unit entity) {
        Collection<Entity> dependentEntities = new ArrayList<>();
        var recipes = recipeCrudService.findAll();
        var ingredients = ingredientCrudService.findAll();

        for (var recipe: recipes) {
            for (Map.Entry<Ingredient,AmountInUnit> entry: recipe.getIngredients().entrySet()) {
                Unit unit = entry.getValue().getUnit();
                if (unit.equals(entity)) {
                    dependentEntities.add(recipe);
                }
            }
        }

        for (var ingredient: ingredients) {
            Unit unit = ingredient.getDefaultUnit();
            if (unit.equals(entity)) {
                dependentEntities.add(ingredient);
            }
        }
        return dependentEntities;

    }
}
