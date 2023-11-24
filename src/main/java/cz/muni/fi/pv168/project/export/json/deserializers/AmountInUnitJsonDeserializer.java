package cz.muni.fi.pv168.project.export.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pv168.project.model.AmountInUnit;
import cz.muni.fi.pv168.project.model.IngredientType;
import cz.muni.fi.pv168.project.model.Unit;

import java.io.IOException;

public class AmountInUnitJsonDeserializer extends JsonDeserializer<AmountInUnit> {

    @Override
    public AmountInUnit deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonParser);

        String unitName = rootNode.get("unit").asText();
        int amount = rootNode.get("amount").asInt();

        IngredientType ingredientType = switch (unitName) {
            // TODO string as constant
            case "pieces" -> IngredientType.COUNTABLE;
            case "grams" -> IngredientType.WEIGHABLE;
            case "milliliters" -> IngredientType.POURABLE;
            // TODO own exception
            default -> throw new IOException(unitName + " unknown ingredient name");
        };

        var amountInUnit = new AmountInUnit(new Unit(unitName, unitName, ingredientType,1), amount);

        // TODO static default units
        return amountInUnit;
    }
}
