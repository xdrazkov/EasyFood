package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.IngredientType;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.service.crud.CrudService;
import cz.muni.fi.pv168.project.ui.dialog.AddUnitDialog;
import cz.muni.fi.pv168.project.ui.dialog.EditUnitDialog;
import cz.muni.fi.pv168.project.ui.dialog.OpenUnitDialog;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

public class UnitTableModel extends BasicTableModel<Unit> {
    private static final HashMap<IngredientType, Unit> baseUnitsMap = new HashMap<>();
    private final CrudService<Unit> crudService;
    public UnitTableModel(DependencyProvider dependencyProvider, CrudService<Unit> crudService) {
        super(dependencyProvider, crudService);
        this.crudService = crudService;
        setupBaseUnits();
    }

    public List<Column<Unit, ?>> makeColumns() {
        return List.of(
                Column.readonly("Name", String.class, Unit::getName),
                Column.readonly("Abbreviation", String.class, Unit::getAbbreviation),
                Column.readonly("Conversion Rate", Float.class, Unit::getConversionRate)
        );
    }

    public void setupBaseUnits(){
        Unit gram = new Unit("grams", "g", IngredientType.WEIGHABLE, 1);
        Unit milliliter = new Unit("milliliters", "ml", IngredientType.POURABLE, 1);
        Unit piece = new Unit("pieces", "pcs", IngredientType.COUNTABLE, 1);

        List<Unit> units = crudService.findAll();
        if (units.isEmpty()) {
            addRow(gram);
            addRow(milliliter);
            addRow(piece);
        }

        baseUnitsMap.put(IngredientType.WEIGHABLE, gram);
        baseUnitsMap.put(IngredientType.POURABLE, milliliter);
        baseUnitsMap.put(IngredientType.COUNTABLE, piece);
    }

    public HashMap<IngredientType, Unit> getBaseUnitsMap() {
        return baseUnitsMap;
    }

    @Override
    public void performAddAction(JTable table, UnitTableModel unitTableModel, List<Category> categories, List<Ingredient> ingredients, List<Unit> units) {
        UnitTableModel unitTable = (UnitTableModel) table.getModel();
        var dialog = new AddUnitDialog(table);
        dialog.show(table, "Edit Unit").ifPresent(unitTable::addRow);
    }

    @Override
    public void performEditAction(int[] selectedRows, JTable table, UnitTableModel unitTableModel, List<Category> categories, List<Ingredient> ingredients, List<Unit> units) {
        int modelRow = table.convertRowIndexToModel(selectedRows[0]);
        var unit = unitTableModel.getEntity(modelRow);
        var dialog = new EditUnitDialog(unit, table);
        dialog.show(table, "Edit Unit").ifPresent(unitTableModel::updateRow);
    }

    @Override
    public void performOpenAction(JTable table, int modelRow) {
        UnitTableModel unitTableModel = (UnitTableModel) table.getModel();
        var unit = unitTableModel.getEntity(modelRow);
        var dialog = new OpenUnitDialog(unit);
        dialog.show(table, "Open Unit").ifPresent(unitTableModel::updateRow);
    }

    public static Unit getBaseUnit(IngredientType ingredientType) {
        return baseUnitsMap.get(ingredientType);
    }
}
