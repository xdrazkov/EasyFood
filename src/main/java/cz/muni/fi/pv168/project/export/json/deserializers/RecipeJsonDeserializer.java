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
        int nutritionalValue = rootNode.get("nutritionalValue").asInt();
        Category category = mapper.treeToValue(rootNode.get("category"), Category.class);

        HashMap<Ingredient, AmountInUnit> ingredients = new HashMap<>();
        if (rootNode.has("ingredients")) {
            JsonNode ingredientsNode = rootNode.get("ingredients");
            if (ingredientsNode.isObject()) {
                for (JsonNode ingredientNode : ingredientsNode) {
                    var ingredient = new Ingredient();
                    ingredient.setName(ingredientsNode.fieldNames().next());
                    AmountInUnit amountInUnit = mapper.treeToValue(ingredientNode, AmountInUnit.class);
                    // TODO calories per unit
                    ingredient.setCaloriesPerUnit(0);
                    ingredient.setDefaultUnit(amountInUnit.getUnit());
                    ingredients.put(ingredient, amountInUnit);
                }
            }
        }

        // TODO constructor
        var recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setPortionCount(portionCount);
        recipe.setInstructions(instructions);
        recipe.setCategory(category);
        recipe.setNutritionalValue(nutritionalValue); // TODO unnecessary
        recipe.setIngredients(ingredients);

        return recipe;
    }
}
