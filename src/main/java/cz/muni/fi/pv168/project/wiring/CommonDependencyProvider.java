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

public class CommonDependencyProvider implements DependencyProvider {
    private final Repository<Unit> unitRepository = null;
    private final Repository<Recipe> recipeRepository = null;
    private final Repository<Ingredient> ingredientRepository = null;
    private final Repository<Category> categoryRepository = null;

    private final CrudService<Recipe> recipeCrudService = null;
    private final CrudService<Ingredient> ingredientCrudService = null;
    private final CrudService<Category> categoryCrudService = null;
    private final CrudService<Unit> unitCrudService = null;

    private final ImportService importService = null;
    private final ExportService exportService = null;

    private final DatabaseManager databaseManager;

    public CommonDependencyProvider(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

        // TODO finish
//        var departmentMapper = new DepartmentMapper();
//        var departmentDao = new DepartmentDao(databaseManager::getConnectionHandler);
//
//        var employeeMapper = new EmployeeMapper(departmentDao, departmentMapper);
//
//        this.departments = new DepartmentSqlRepository(
//                departmentDao,
//                departmentMapper
//        );
//        this.employees = new EmployeeSqlRepository(
//                new EmployeeDao(databaseManager::getConnectionHandler),
//                employeeMapper
//        );
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public Repository<Unit> getUnitRepository() {
        return unitRepository;
    }

    @Override
    public Repository<Recipe> getRecipeRepository() {
        return recipeRepository;
    }

    @Override
    public Repository<Ingredient> getIngredientRepository() {
        return ingredientRepository;
    }

    @Override
    public Repository<Category> getCategoryRepository() {
        return categoryRepository;
    }

    @Override
    public CrudService<Recipe> getRecipeCrudService() {
        return recipeCrudService;
    }

    @Override
    public CrudService<Ingredient> getIngredientCrudService() {
        return ingredientCrudService;
    }

    @Override
    public CrudService<Category> getCategoryCrudService() {
        return categoryCrudService;
    }

    @Override
    public CrudService<Unit> getUnitCrudService() {
        return unitCrudService;
    }

    @Override
    public ImportService getImportService() {
        return importService;
    }

    @Override
    public ExportService getExportService() {
        return exportService;
    }
}
