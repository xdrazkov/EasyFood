package cz.muni.fi.pv168.project.export.json.seralizers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cz.muni.fi.pv168.project.model.AmountInUnit;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import java.io.IOException;

public class AmountInUnitJsonSerializer extends JsonSerializer<AmountInUnit> {
    UnitTableModel unitTableModel;

    public AmountInUnitJsonSerializer(UnitTableModel unitTableModel) {
        this.unitTableModel = unitTableModel;
    }

    @Override
    public void serialize(
            AmountInUnit amountInUnit, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        AmountInUnit amountInBaseUnit = amountInUnit.convertToBaseUnit(unitTableModel);
        jsonGenerator.writeStringField("unit", amountInBaseUnit.getUnit().toString());
        jsonGenerator.writeNumberField("amount", amountInBaseUnit.getAmount());
        jsonGenerator.writeEndObject();
    }
}