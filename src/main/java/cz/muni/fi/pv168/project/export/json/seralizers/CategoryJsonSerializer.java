package cz.muni.fi.pv168.project.export.json.seralizers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cz.muni.fi.pv168.project.model.Category;

import java.awt.*;
import java.io.IOException;
import static cz.muni.fi.pv168.project.export.json.JsonFields.*;

public class CategoryJsonSerializer extends JsonSerializer<Category> {
    @Override
    public void serialize(Category category, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(CATEGORY_NAME, category.getName());

        Color color = category.getColor();
        jsonGenerator.writeArrayFieldStart(CATEGORY_COLOR);
        jsonGenerator.writeNumber(color.getRed());
        jsonGenerator.writeNumber(color.getGreen());
        jsonGenerator.writeNumber(color.getBlue());
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}
