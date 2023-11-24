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
import static cz.muni.fi.pv168.project.export.json.JsonFields.*;

public class RecipeJsonDeserializer extends JsonDeserializer<Recipe> {

    @Override
    public Recipe deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonParser);

        String title = rootNode.get(TITLE).asText();
        String description = rootNode.get(DESCRIPTION).asText();
        int portionCount = rootNode.get(PORTION_COUNT).asInt();
        String instructions = rootNode.get(INSTRUCTIONS).asText();
        int timeToPrepare = rootNode.get(TIME_TO_PREPARE).asInt();
        Category category = mapper.treeToValue(rootNode.get(CATEGORY), Category.class);

        HashMap<Ingredient, AmountInUnit> ingredients = new HashMap<>();

        JsonNode ingredientsNode = rootNode.get(INGREDIENTS);
        for (JsonNode ingredientAmountNode : ingredientsNode) {
            var ingredient = mapper.treeToValue(ingredientAmountNode.get(INGREDIENT), Ingredient.class);
            var amountInUnit = mapper.treeToValue(ingredientAmountNode.get(AMOUNT_IN_UNIT), AmountInUnit.class);
            ingredient.setDefaultUnit(amountInUnit.getUnit());
            ingredients.put(ingredient, amountInUnit);
        }

        return new Recipe(title, description, portionCount, instructions, timeToPrepare, category, ingredients);
    }
}
