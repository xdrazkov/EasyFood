package cz.muni.fi.pv168.project.service;

import cz.muni.fi.pv168.project.model.*;
import cz.muni.fi.pv168.project.service.crud.CrudService;

import java.util.Collection;
import java.util.Collections;

public class RecipeDependencyChecker extends GeneralDependencyChecker<Recipe> {
    public RecipeDependencyChecker(){
    }
    @Override
    public Collection<Entity> getDependentEntities(Recipe entity) {
        return Collections.emptyList();
    }
}
