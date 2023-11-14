package cz.muni.fi.pv168.project.service.export;

import cz.muni.fi.pv168.project.service.export.batch.BatchOperationException;
import cz.muni.fi.pv168.project.service.export.format.Format;

import java.util.Collection;

/**
 * Generic mechanism, allowing to export data to a file.
 */
public interface ExportService {

    /**
     * Exports data to a file.
     *
     * @param filePath absolute path of the export file (to be created or overwritten)
     *
     * @throws BatchOperationException if the export cannot be done
     */
    void exportData(String filePath);

    /**
     * Gets all available formats for export.
     */
    Collection<Format> getFormats();
}