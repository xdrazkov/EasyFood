package cz.muni.fi.pv168.project.export.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pv168.project.model.Category;

import java.awt.*;
import java.io.IOException;

public class CategoryJsonDeserializer extends JsonDeserializer<Category> {

    @Override
    public Category deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonParser);

        String name = rootNode.get("name").asText();

        JsonNode colorNode = rootNode.get("color");
        Color color = parseColor(colorNode);

        return new Category(name, color);
    }

    private Color parseColor(JsonNode colorNode) {
        int red = colorNode.get(0).asInt();
        int green = colorNode.get(1).asInt();
        int blue = colorNode.get(2).asInt();

        return new Color(red, green, blue);
    }
}