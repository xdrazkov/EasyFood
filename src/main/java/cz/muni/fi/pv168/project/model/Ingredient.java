package cz.muni.fi.pv168.project.model;

public class Ingredient {
    private String name;
    private Unit baseUnit;
    private int caloriesPerUnit;

    public Ingredient(String name, Unit baseUnit, int caloriesPerUnit) {
        this.name = name;
        this.baseUnit = baseUnit;
        this.caloriesPerUnit = caloriesPerUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(Unit baseUnit) {
        this.baseUnit = baseUnit;
    }

    public int getCaloriesPerUnit() {
        return caloriesPerUnit;
    }

    public int getTotalCalories(Unit anyUnit, int amount) { // TODO distinguish type: eg. WEIGHABLE per 100g, COUNTABLE per 1pc
        float countBase = anyUnit.getConversionRate() * amount;
        return (int) (countBase * caloriesPerUnit);
    }

    public void setCaloriesPerUnit(int caloriesPerUnit) {
        this.caloriesPerUnit = caloriesPerUnit;
    }

    @Override
    public String toString() {
        return name;
    }
}
