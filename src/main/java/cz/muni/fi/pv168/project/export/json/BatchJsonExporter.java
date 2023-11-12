package cz.muni.fi.pv168.project.export.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import cz.muni.fi.pv168.project.service.export.DataManipulationException;
import cz.muni.fi.pv168.project.service.export.batch.Batch;
import cz.muni.fi.pv168.project.service.export.batch.BatchExporter;
import cz.muni.fi.pv168.project.service.export.format.Format;

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
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

            writer.writeValue(Files.newBufferedWriter(Path.of(filePath), StandardCharsets.UTF_8), batch.recipes());

        } catch (IOException e) {
            throw new DataManipulationException("Export not successful" + e);
        }
    }
}
