package cz.muni.fi.pv168.project.export.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pv168.project.model.AmountInUnit;
import cz.muni.fi.pv168.project.model.IngredientType;

import static cz.muni.fi.pv168.project.export.json.JsonFields.*;

import java.io.IOException;

public class AmountInUnitJsonDeserializer extends JsonDeserializer<AmountInUnit> {

    @Override
    public AmountInUnit deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonParser);

        String unitName = rootNode.get(UNIT).asText();
        int amount = rootNode.get(AMOUNT).asInt();

        IngredientType ingredientType = switch (unitName) {
            // TODO string as constant
            case "pieces" -> IngredientType.COUNTABLE;
            case "grams" -> IngredientType.WEIGHABLE;
            case "milliliters" -> IngredientType.POURABLE;
            // TODO own exception
            default -> throw new IOException(unitName + " unknown ingredient name");
        };

        return new AmountInUnit(ingredientType.getBaseUnit(), amount);
    }
}
