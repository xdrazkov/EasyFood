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
import java.util.Objects;

public class UnitTableModel extends BasicTableModel<Unit> {
    private static final HashMap<IngredientType, Unit> baseUnitsMap = new HashMap<>();
    public UnitTableModel(DependencyProvider dependencyProvider) {
        super(dependencyProvider, dependencyProvider.getUnitCrudService());
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
        Unit gram = null;
        Unit milliliter = null;
        Unit piece = null;
        List<Unit> units = dependencyProvider.getUnitCrudService().findAll();
        for (Unit unit : units) {
            if (Objects.equals(unit.getName(), "grams")) {
                gram = unit;
            } else if (Objects.equals(unit.getName(), "milliliters")) {
                milliliter = unit;
            } else if (Objects.equals(unit.getName(), "pieces")) {
                piece = unit;
            }
        }

        if (units.isEmpty()) {
            gram = new Unit("grams", "g", IngredientType.WEIGHABLE, 1);
            milliliter = new Unit("milliliters", "ml", IngredientType.POURABLE, 1);
            piece = new Unit("pieces", "pcs", IngredientType.COUNTABLE, 1);
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
