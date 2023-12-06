package cz.muni.fi.pv168.project.export.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import cz.muni.fi.pv168.project.export.json.seralizers.AmountInUnitJsonSerializer;
import cz.muni.fi.pv168.project.model.AmountInUnit;
import cz.muni.fi.pv168.project.service.export.DataManipulationException;
import cz.muni.fi.pv168.project.service.export.batch.Batch;
import cz.muni.fi.pv168.project.service.export.batch.BatchExporter;
import cz.muni.fi.pv168.project.service.export.format.Format;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class BatchJsonExporter implements BatchExporter {
    private static final Format FORMAT = new Format("JSON", List.of("json"));

    @Override
    public Format getFormat() {
        return FORMAT;
    }

    @Override
    public void exportBatch(Batch batch, String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module =
                    new SimpleModule("CustomCarSerializer", new Version(1, 0, 0, null, null, null));
            mapper.registerModule(module);

            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

            writer.writeValue(Files.newBufferedWriter(Path.of(filePath), StandardCharsets.UTF_8), batch.recipes());

        } catch (IOException e) {
            throw new DataManipulationException("Export not successful" + e);
        }
    }
}
