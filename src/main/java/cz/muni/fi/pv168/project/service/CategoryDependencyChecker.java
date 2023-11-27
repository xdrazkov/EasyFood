package cz.muni.fi.pv168.project.service;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Entity;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.service.crud.CrudService;

import java.util.ArrayList;
import java.util.Collection;

public class CategoryDependencyChecker extends GeneralDependencyChecker<Category> {

    public CategoryDependencyChecker(CrudService<Recipe> recipeCrudService) {
        this.recipeCrudService = recipeCrudService;
    }
    @Override
    public Collection<Entity> getDependentEntities(Category entity) {
        Collection<Entity> dependentEntities = new ArrayList<>();
        var recipes = recipeCrudService.findAll();

        for (var recipe: recipes) {
            if (recipe.getCategory().equals(entity)) {
                dependentEntities.add(recipe);
            }
        }
        return dependentEntities;
    }
}
