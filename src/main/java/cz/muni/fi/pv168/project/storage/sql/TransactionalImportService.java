package cz.muni.fi.pv168.project.storage.sql;

import cz.muni.fi.pv168.project.service.export.ImportService;
import cz.muni.fi.pv168.project.service.export.format.Format;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionExecutor;
import cz.muni.fi.pv168.project.ui.action.ImportStrategy;

import java.util.Collection;

public class TransactionalImportService implements ImportService {

    private final ImportService importService;

    private final TransactionExecutor transactionExecutor;

    public TransactionalImportService(ImportService importService, TransactionExecutor transactionExecutor) {
        this.importService = importService;
        this.transactionExecutor = transactionExecutor;
    }

    @Override
    public void importData(String filePath) {
        transactionExecutor.executeInTransaction(() -> importService.importData(filePath));
    }

    @Override
    public void importData(String filePath, ImportStrategy importStrategy) {
        transactionExecutor.executeInTransaction(() -> importService.importData(filePath, importStrategy));
    }

//    @Override
//    public void importData(String filePath, ImportStrategy importStrategy) {
//
//    }

    @Override
    public Collection<Format> getFormats() {
        return importService.getFormats();
    }
}
