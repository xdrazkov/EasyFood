package cz.muni.fi.pv168.project.storage.sql.db;

import java.sql.SQLException;
import java.util.Objects;

public final class TransactionManagerImpl implements TransactionManager {

    private final DatabaseManager databaseManager;
    private Transaction transaction;

    public TransactionManagerImpl(DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
    }

    @Override
    public synchronized Transaction beginTransaction() {
        if (hasActiveTransaction()) {
            throw new TransactionException("Transaction already started");
        }
        try {
            transaction = new TransactionImpl(databaseManager.getConnectionHandler().use());
        } catch (SQLException e) {
            throw new TransactionException("Unable to start transaction", e);
        }

        return transaction;
    }

    @Override
    public synchronized ConnectionHandler getConnectionHandler() {
        return transaction.connection();
    }

    @Override
    public synchronized boolean hasActiveTransaction() {
        return transaction != null && !transaction.isClosed();
    }
}

