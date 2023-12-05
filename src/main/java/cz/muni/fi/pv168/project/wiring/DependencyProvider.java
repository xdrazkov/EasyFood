package cz.muni.fi.pv168.project.wiring;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.repository.Repository;
import cz.muni.fi.pv168.project.service.crud.CrudService;
import cz.muni.fi.pv168.project.service.export.ExportService;
import cz.muni.fi.pv168.project.service.export.ImportService;
import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionExecutor;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionManager;

public interface DependencyProvider {
     DatabaseManager getDatabaseManager();

    // TODO TransactionManager provider
     TransactionManager getTransactionManager();

    // TODO Validator<> provider
    Repository<Unit> getUnitRepository();
    Repository<Recipe> getRecipeRepository();
    Repository<Ingredient> getIngredientRepository();
    Repository<Category> getCategoryRepository();

    CrudService<Recipe> getRecipeCrudService();
    CrudService<Ingredient> getIngredientCrudService();
    CrudService<Category> getCategoryCrudService();
    CrudService<Unit> getUnitCrudService();

    ImportService getImportService();
    ExportService getExportService();

    TransactionExecutor getTransactionExecutor();
}
