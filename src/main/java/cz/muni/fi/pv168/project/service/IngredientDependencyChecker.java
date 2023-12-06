package cz.muni.fi.pv168.project.service;

import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.service.crud.CrudService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class IngredientDependencyChecker extends GeneralDependencyChecker<Ingredient> {
    public IngredientDependencyChecker(CrudService<Recipe> recipeCrudService){
        this.recipeCrudService = recipeCrudService;
    }
    @Override
    public Collection<Entity> getDependentEntities(Ingredient entity) {
        Collection<Entity> dependentEntities = new ArrayList<>();
        var recipes = recipeCrudService.findAll();

        for (var recipe: recipes) {
            for (Map.Entry<Ingredient, AmountInUnit> entry: recipe.getIngredients().entrySet()) {
                if (entry.getKey().equals(entity)) {
                    dependentEntities.add(recipe);
                    break;
                }
            }
        }
        return dependentEntities;
    }
}
