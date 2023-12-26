package cz.muni.fi.pv168.project.wiring;

import cz.muni.fi.pv168.project.export.json.BatchJsonExporter;
import cz.muni.fi.pv168.project.export.json.BatchJsonImporter;
import cz.muni.fi.pv168.project.export.pdf.BatchPdfExporter;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.model.UuidGuidProvider;
import cz.muni.fi.pv168.project.repository.Repository;
import cz.muni.fi.pv168.project.service.CategoryDependencyChecker;
import cz.muni.fi.pv168.project.service.IngredientDependencyChecker;
import cz.muni.fi.pv168.project.service.RecipeDependencyChecker;
import cz.muni.fi.pv168.project.service.UnitDependencyChecker;
import cz.muni.fi.pv168.project.service.crud.CrudService;
import cz.muni.fi.pv168.project.service.crud.GenericCrudService;
import cz.muni.fi.pv168.project.service.export.ExportService;
import cz.muni.fi.pv168.project.service.export.GenericExportService;
import cz.muni.fi.pv168.project.service.export.GenericImportService;
import cz.muni.fi.pv168.project.service.export.ImportService;
import cz.muni.fi.pv168.project.service.validation.*;
import cz.muni.fi.pv168.project.storage.sql.*;
import cz.muni.fi.pv168.project.storage.sql.dao.CategoryDao;
import cz.muni.fi.pv168.project.storage.sql.dao.IngredientDao;
import cz.muni.fi.pv168.project.storage.sql.dao.RecipeDao;
import cz.muni.fi.pv168.project.storage.sql.dao.RecipeIngredientDao;
import cz.muni.fi.pv168.project.storage.sql.dao.UnitDao;
import cz.muni.fi.pv168.project.storage.sql.db.*;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.CategoryMapper;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.IngredientMapper;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.RecipeIngredientMapper;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.RecipeMapper;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.UnitMapper;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

public class CommonDependencyProvider implements DependencyProvider {
    private final Repository<Unit> unitRepository;
    private final Repository<Recipe> recipeRepository;
    private final Repository<Ingredient> ingredientRepository;
    private final Repository<Category> categoryRepository;
    private final TransactionExecutor transactionExecutor;
    private final CrudService<Recipe> recipeCrudService;
    private final CrudService<Ingredient> ingredientCrudService;
    private final CrudService<Category> categoryCrudService;
    private final CrudService<Unit> unitCrudService;

    private final ImportService importService;
    private final ExportService exportService;

    private final DatabaseManager databaseManager;
    private final Validator<Recipe> recipeValidator;
    private final Validator<Ingredient> ingredientValidator;
    private final Validator<Category> categoryValidator;
    private final Validator<Unit> unitValidator;

    public CommonDependencyProvider(DatabaseManager databaseManager) {

        this.databaseManager = databaseManager;
        var transactionManager = new TransactionManagerImpl(databaseManager);

        recipeValidator = new RecipeValidator();
        ingredientValidator = new IngredientValidator();
        categoryValidator = new CategoryValidator();
        unitValidator = new UnitValidator();

        var guidProvider = new UuidGuidProvider();

        this.transactionExecutor = new TransactionExecutorImpl(transactionManager::beginTransaction);
        var transactionConnectionSupplier = new TransactionConnectionSupplier(transactionManager, databaseManager);


        UnitDao unitDao = new UnitDao(databaseManager::getConnectionHandler);
        UnitMapper unitMapper = new UnitMapper();
        unitRepository = new UnitSqlRepository(unitDao, unitMapper);
        unitCrudService = new GenericCrudService<>(unitRepository, unitValidator, guidProvider);

        IngredientDao ingredientDao = new IngredientDao(transactionConnectionSupplier);
        IngredientMapper ingredientMapper = new IngredientMapper(unitDao, unitMapper);
        ingredientRepository = new IngredientSqlRepository(ingredientDao, ingredientMapper);
        ingredientCrudService = new GenericCrudService<>(ingredientRepository, ingredientValidator, guidProvider);

        CategoryDao categoryDao = new CategoryDao(transactionConnectionSupplier);
        CategoryMapper categoryMapper = new CategoryMapper();
        categoryRepository = new CategorySqlRepository(categoryDao, categoryMapper);
        categoryCrudService = new GenericCrudService<>(categoryRepository, categoryValidator, guidProvider);

        RecipeIngredientDao recipeIngredientDao = new RecipeIngredientDao(transactionConnectionSupplier);
        RecipeIngredientMapper recipeIngredientMapper = new RecipeIngredientMapper(ingredientDao, unitDao, ingredientMapper, unitMapper);
        RecipeDao recipeDao = new RecipeDao(transactionConnectionSupplier);
        RecipeMapper recipeMapper = new RecipeMapper(categoryDao, categoryMapper);
        recipeRepository = new RecipeSqlRepository(recipeDao, recipeIngredientDao, recipeMapper, recipeIngredientMapper);
        recipeCrudService = new GenericCrudService<>(recipeRepository, recipeValidator, guidProvider);


        importService = new GenericImportService(recipeCrudService, ingredientCrudService, categoryCrudService,
                List.of(new BatchJsonImporter()), transactionExecutor);
        exportService = new GenericExportService(List.of(new BatchJsonExporter(), new BatchPdfExporter()), recipeCrudService);

        recipeCrudService.setGeneralDependencyChecker(new RecipeDependencyChecker());
        ingredientCrudService.setGeneralDependencyChecker(new IngredientDependencyChecker(recipeCrudService));
        categoryCrudService.setGeneralDependencyChecker(new CategoryDependencyChecker(recipeCrudService));
        unitCrudService.setGeneralDependencyChecker(new UnitDependencyChecker(recipeCrudService, ingredientCrudService));
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public TransactionManager getTransactionManager() {
        throw new NotImplementedException();
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

    @Override
    public TransactionExecutor getTransactionExecutor() {
        return transactionExecutor;
    }
    @Override
    public Validator<Recipe> getRecipeValidator() {
        return recipeValidator;
    }
    @Override
    public Validator<Ingredient> getIngredientValidator() {
        return ingredientValidator;
    }
    @Override
    public Validator<Category> getCategoryValidator() {
        return categoryValidator;
    }
    @Override
    public Validator<Unit> getUnitValidator() {
        return unitValidator;
    }
}
