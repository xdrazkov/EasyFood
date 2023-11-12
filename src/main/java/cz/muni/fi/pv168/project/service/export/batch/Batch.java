package cz.muni.fi.pv168.project.service.export.batch;

import cz.muni.fi.pv168.project.model.Recipe;

import java.util.Collection;

public record Batch(Collection<Recipe> recipes) {
}
