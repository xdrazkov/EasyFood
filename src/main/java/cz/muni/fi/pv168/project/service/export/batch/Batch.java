package cz.muni.fi.pv168.project.service.export.batch;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import cz.muni.fi.pv168.project.export.json.deserializers.BatchJsonDeserializer;
import cz.muni.fi.pv168.project.model.Recipe;

import java.util.Collection;

@JsonDeserialize(using = BatchJsonDeserializer.class)
public record Batch(Collection<Recipe> recipes) {
}
