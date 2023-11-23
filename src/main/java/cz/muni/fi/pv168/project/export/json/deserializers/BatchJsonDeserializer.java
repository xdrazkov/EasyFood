package cz.muni.fi.pv168.project.export.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.service.export.batch.Batch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class BatchJsonDeserializer extends JsonDeserializer<Batch> {

    @Override
    public Batch deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonParser);

        Collection<Recipe> recipes = new ArrayList<>();
        if (rootNode.isArray()) {
            for (JsonNode recipeNode : rootNode) {
                Recipe recipe = mapper.treeToValue(recipeNode, Recipe.class);
                recipes.add(recipe);
            }
        }

        return new Batch(recipes);
    }
}
