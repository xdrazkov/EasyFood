package cz.muni.fi.pv168.project.service;

import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.service.crud.CrudService;

import java.util.Collection;

public abstract class GeneralDependencyChecker<T extends Entity> {
    // TODO dependency provider
    protected CrudService<Recipe> recipeCrudService;
    protected CrudService<Ingredient> ingredientCrudService;
    protected CrudService<Category> categoryCrudService;
    protected CrudService<Unit> unitCrudService;


    /**
     * @return collection of Entities, which are dependent on entity
     */
    public abstract Collection<Entity> getDependentEntities(T entity);
}
