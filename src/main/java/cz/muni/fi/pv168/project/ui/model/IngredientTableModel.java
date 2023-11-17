package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;

import java.util.List;

public class IngredientTableModel extends BasicTableModel<Ingredient> {
    public IngredientTableModel(List<Ingredient> ingredients) {
        super(ingredients);
    }

    public List<Column<Ingredient, ?>> makeColumns() {
        return List.of(
                Column.readonly("Name", String.class, Ingredient::getName),
                Column.readonly("Default unit", Unit.class, Ingredient::getDefaultUnit),
                Column.readonly("Nutritional value (kcal)", Integer.class, Ingredient::getCaloriesPerUnit)
        );
    }
}
