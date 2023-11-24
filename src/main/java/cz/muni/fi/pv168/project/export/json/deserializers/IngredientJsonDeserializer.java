package cz.muni.fi.pv168.project.export.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pv168.project.model.Ingredient;
import static cz.muni.fi.pv168.project.export.json.JsonFields.*;


import java.io.IOException;

public class IngredientJsonDeserializer extends JsonDeserializer<Ingredient> {

    @Override
    public Ingredient deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonParser);

        String name = rootNode.get(INGREDIENT_NAME).asText();
        float caloriesPerUnit = (float) rootNode.get(CALORIES_PER_UNIT).asDouble();

        var ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setCaloriesPerUnit(caloriesPerUnit);
        // default units are set in RecipeJsonDeserializer
        return ingredient;
    }
}