package cz.muni.fi.pv168.project.model;

public class Ingredient {
    private String name;
    private IngredientType ingredientType;
    private int caloriesPerUnit;

    public Ingredient(String name, IngredientType ingredientType, int caloriesPerUnit) {
        this.name = name;
        this.ingredientType = ingredientType;
        this.caloriesPerUnit = caloriesPerUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IngredientType getIngredientType() {
        return ingredientType;
    }

    public void setIngredientType(IngredientType ingredientType) {
        this.ingredientType = ingredientType;
    }

    public int getCaloriesPerUnit() {
        return caloriesPerUnit;
    }

    public void setCaloriesPerUnit(int caloriesPerUnit) {
        this.caloriesPerUnit = caloriesPerUnit;
    }
}
