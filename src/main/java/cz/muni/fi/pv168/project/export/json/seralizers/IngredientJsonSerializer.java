package cz.muni.fi.pv168.project.export.json.seralizers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cz.muni.fi.pv168.project.model.Ingredient;

import java.io.IOException;

public class IngredientJsonSerializer extends JsonSerializer<Ingredient> {
    @Override
    public void serialize(Ingredient ingredient, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("name", ingredient.getName());
        float caloriesPerBaseUnit = ingredient.getCaloriesPerUnit() / ingredient.getDefaultUnit().getConversionRate();
        jsonGenerator.writeNumberField("caloriesPerUnit", caloriesPerBaseUnit);

        jsonGenerator.writeEndObject();
    }
}
