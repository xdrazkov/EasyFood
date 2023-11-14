package cz.muni.fi.pv168.project.export.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pv168.project.service.export.DataManipulationException;
import cz.muni.fi.pv168.project.service.export.batch.Batch;
import cz.muni.fi.pv168.project.service.export.batch.BatchImporter;
import cz.muni.fi.pv168.project.service.export.format.Format;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BatchJsonImporter implements BatchImporter {

    private static final Format FORMAT = new Format("JSON", List.of("json"));

    @Override
    public Format getFormat() {
        return FORMAT;
    }

    @Override
    public Batch importBatch(String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Batch batch = mapper.readValue(new File(filePath), Batch.class);
            return batch;
        } catch (IOException e) {
            throw new DataManipulationException("Unable to read JSON file", e);
        }
    }
}
