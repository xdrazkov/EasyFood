package cz.muni.fi.pv168.project.export.json.seralizers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cz.muni.fi.pv168.project.model.AmountInUnit;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import java.io.IOException;
import static cz.muni.fi.pv168.project.export.json.JsonFields.*;

public class AmountInUnitJsonSerializer extends JsonSerializer<AmountInUnit> {
    @Override
    public void serialize(
            AmountInUnit amountInUnit, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        AmountInUnit amountInBaseUnit = amountInUnit.convertToBaseUnit();
        jsonGenerator.writeStringField(UNIT, amountInBaseUnit.getUnit().toString());
        jsonGenerator.writeNumberField(AMOUNT, amountInBaseUnit.getAmount());
        jsonGenerator.writeEndObject();
    }
}