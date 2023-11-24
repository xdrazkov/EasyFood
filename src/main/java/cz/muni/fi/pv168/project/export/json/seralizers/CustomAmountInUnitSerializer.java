package cz.muni.fi.pv168.project.export.json.seralizers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import cz.muni.fi.pv168.project.model.AmountInUnit;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import java.io.IOException;

public class CustomAmountInUnitSerializer extends StdSerializer<AmountInUnit> {
    UnitTableModel unitTableModel;

    public CustomAmountInUnitSerializer(UnitTableModel unitTableModel) {
        this(null, unitTableModel);
    }

    public CustomAmountInUnitSerializer(Class<AmountInUnit> t, UnitTableModel unitTableModel) {
        super(t);
        this.unitTableModel = unitTableModel;
    }

    // TODO
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