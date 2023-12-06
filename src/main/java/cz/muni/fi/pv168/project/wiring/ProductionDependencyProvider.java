package cz.muni.fi.pv168.project.wiring;

import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;

public class ProductionDependencyProvider extends CommonDependencyProvider {


    public ProductionDependencyProvider() {
        super(createDatabaseManager());
    }

    private static DatabaseManager createDatabaseManager() {
        DatabaseManager databaseManager = DatabaseManager.createProductionInstance();
        databaseManager.initSchema();

        return databaseManager;
    }

}
