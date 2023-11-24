package cz.muni.fi.pv168.project.export.json.seralizers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cz.muni.fi.pv168.project.model.Recipe;

import java.io.IOException;

public class RecipeJsonSerializer extends JsonSerializer<Recipe> {
    @Override
    public void serialize(Recipe recipe, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("title", recipe.getTitle());
        jsonGenerator.writeStringField("description", recipe.getDescription());
        jsonGenerator.writeNumberField("portionCount", recipe.getPortionCount());
        jsonGenerator.writeStringField("instructions", recipe.getInstructions());
        jsonGenerator.writeNumberField("timeToPrepare", recipe.getTimeToPrepare());
        jsonGenerator.writeNumberField("nutritionalValue", recipe.getNutritionalValue());
        jsonGenerator.writeObjectField("category", recipe.getCategory());

        jsonGenerator.writeFieldName("ingredients");
        jsonGenerator.writeStartArray();
        for (var entry: recipe.getIngredients().entrySet()) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("ingredient", entry.getKey());
            jsonGenerator.writeObjectField("amountInUnit", entry.getValue());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();


        jsonGenerator.writeEndObject();
    }
}
