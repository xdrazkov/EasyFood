package cz.muni.fi.pv168.project.model;

public class Unit extends Entity {
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

    public Unit(String guid, String name, String abbreviation, IngredientType ingredientType, float conversionRate) {
        super(guid);
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

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Unit that)) {
            return false;
        }
        return this.name.equals(that.name) && this.ingredientType.equals(that.ingredientType);
    }
}
