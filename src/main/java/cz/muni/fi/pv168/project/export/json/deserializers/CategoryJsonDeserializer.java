package cz.muni.fi.pv168.project.export.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;

import java.awt.*;
import java.io.IOException;

import static cz.muni.fi.pv168.project.export.json.JsonFields.*;

public class CategoryJsonDeserializer extends JsonDeserializer<Category> {

    @Override
    public Category deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonParser);

        String name = rootNode.get(CATEGORY_NAME).asText();
        JsonNode colorNode = rootNode.get(CATEGORY_COLOR);
        Color color = parseColor(colorNode);
        Category category = new Category(name, color);
        if (CategoryTableModel.isDefaultCategory(category)) {
            return CategoryTableModel.getDefaultCategory();
        }
        return category;
    }

    private Color parseColor(JsonNode colorNode) {
        int red = colorNode.get(0).asInt();
        int green = colorNode.get(1).asInt();
        int blue = colorNode.get(2).asInt();

        return new Color(red, green, blue);
    }
}