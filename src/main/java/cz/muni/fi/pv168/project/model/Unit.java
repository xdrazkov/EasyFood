package cz.muni.fi.pv168.project.model;

public class Unit {
    private String name;
    private String abbreviation;
    private IngredientType ingredientType;
    private float conversionRate; // quantity * rate = quantity_base_unit

    public Unit(String name, String abbreviation, IngredientType ingredientType, float conversionRate) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.ingredientType = ingredientType;
        this.conversionRate = conversionRate;
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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public float getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(float conversionRate) {
        this.conversionRate = conversionRate;
    }

    @Override
    public String toString() {
        return name;
    }
}
