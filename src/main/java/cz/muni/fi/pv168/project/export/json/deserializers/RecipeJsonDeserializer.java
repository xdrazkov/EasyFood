package cz.muni.fi.pv168.project.export.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pv168.project.model.AmountInUnit;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;

import java.io.IOException;
import java.util.HashMap;

public class RecipeJsonDeserializer extends JsonDeserializer<Recipe> {

    @Override
    public Recipe deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonParser);

        String title = rootNode.get("title").asText();
        String description = rootNode.get("description").asText();
        int portionCount = rootNode.get("portionCount").asInt();
        String instructions = rootNode.get("instructions").asText();
        int timeToPrepare = rootNode.get("timeToPrepare").asInt();
        Category category = mapper.treeToValue(rootNode.get("category"), Category.class);

        HashMap<Ingredient, AmountInUnit> ingredients = new HashMap<>();

        JsonNode ingredientsNode = rootNode.get("ingredients");
        for (JsonNode ingredientAmountNode : ingredientsNode) {
            var ingredient = mapper.treeToValue(ingredientAmountNode.get("ingredient"), Ingredient.class);
            var amountInUnit = mapper.treeToValue(ingredientAmountNode.get("amountInUnit"), AmountInUnit.class);
            ingredient.setDefaultUnit(amountInUnit.getUnit());
            ingredients.put(ingredient, amountInUnit);
        }

        return new Recipe(title, description, portionCount, instructions, timeToPrepare, category, ingredients);
    }
}
