package cz.muni.fi.pv168.project.export.json.seralizers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cz.muni.fi.pv168.project.model.Recipe;

import java.io.IOException;
import static cz.muni.fi.pv168.project.export.json.JsonFields.*;

public class RecipeJsonSerializer extends JsonSerializer<Recipe> {
    @Override
    public void serialize(Recipe recipe, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField(TITLE, recipe.getTitle());
        jsonGenerator.writeStringField(DESCRIPTION, recipe.getDescription());
        jsonGenerator.writeNumberField(PORTION_COUNT, recipe.getPortionCount());
        jsonGenerator.writeStringField(INSTRUCTIONS, recipe.getInstructions());
        jsonGenerator.writeNumberField(TIME_TO_PREPARE, recipe.getTimeToPrepare());
        jsonGenerator.writeNumberField(NUTRITIONAL_VALUE, recipe.getNutritionalValue());
        jsonGenerator.writeObjectField(CATEGORY, recipe.getCategory());

        jsonGenerator.writeFieldName(INGREDIENTS);
        jsonGenerator.writeStartArray();
        for (var entry: recipe.getIngredients().entrySet()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField(INGREDIENT, entry.getKey());
            jsonGenerator.writeObjectField(AMOUNT_IN_UNIT, entry.getValue());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();


        jsonGenerator.writeEndObject();
    }
}
