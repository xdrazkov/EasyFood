package cz.muni.fi.pv168.project.model;

import java.util.List;

public class Ingredient extends Entity {
    private String name;
    private Unit defaultUnit;
    private int caloriesPerUnit;

    public Ingredient(String name, Unit defaultUnit, int caloriesPerUnit) {
        this.name = name;
        this.defaultUnit = defaultUnit;
        this.caloriesPerUnit = caloriesPerUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit getDefaultUnit() {
        return defaultUnit;
    }

    public void setDefaultUnit(Unit defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    public int getCaloriesPerUnit() {
        return caloriesPerUnit;
    }

    public int getTotalCalories(Unit anyUnit, int amount) { // TODO distinguish type: eg. WEIGHABLE per 100g, COUNTABLE per 1pc
        float countBase = anyUnit.getConversionRate() * amount;
        return (int) (countBase * caloriesPerUnit);
    }

    public int countInstances(List<Recipe> recipes){
        int count = 0;
        for(Recipe recipe : recipes){
            if(recipe.getIngredients().containsKey(this)){
                count++;
            }
        }
        return count;
    }

    public void setCaloriesPerUnit(int caloriesPerUnit) {
        this.caloriesPerUnit = caloriesPerUnit;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Ingredient theirs)) {
            return false;
        }
        return this.name.equals(theirs.name) && this.defaultUnit.equals(theirs.defaultUnit);
    }
}
