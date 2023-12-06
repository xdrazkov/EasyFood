package cz.muni.fi.pv168.project.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.muni.fi.pv168.project.export.json.deserializers.RecipeJsonDeserializer;
import cz.muni.fi.pv168.project.export.json.seralizers.RecipeJsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonSerialize(using = RecipeJsonSerializer.class)
@JsonDeserialize(using = RecipeJsonDeserializer.class)
public class Recipe extends Entity {
    private String title;
    private String description;
    private int portionCount;
    private String instructions;
    private int timeToPrepare; // in minutes; import java.util.concurrent.TimeUnit
    private Category category;
    private HashMap<Ingredient, AmountInUnit> ingredients;

    public Recipe(){}
    public Recipe(String guid,
                  String title,
                  String description,
                  int portionCount,
                  String instructions,
                  int timeToPrepare,
                  Category category,
                  HashMap<Ingredient, AmountInUnit> ingredientList) {
        super(guid);
        init(title, description, portionCount, instructions, timeToPrepare, category, ingredientList);
    }

    public Recipe(String title,
                  String description,
                  int portionCount,
                  String instructions,
                  int timeToPrepare,
                  Category category,
                  HashMap<Ingredient, AmountInUnit> ingredientList) {
        init(title, description, portionCount, instructions, timeToPrepare, category, ingredientList);
    }

    private void init(String title,
                      String description,
                      int portionCount,
                      String instructions,
                      int timeToPrepare,
                      Category category,
                      HashMap<Ingredient, AmountInUnit> ingredientList) {
        setTitle(title);
        setDescription(description);
        setPortionCount(portionCount);
        setInstructions(instructions);
        setTimeToPrepare(timeToPrepare);
        setCategory(category);
        setIngredients(ingredientList);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPortionCount() {
        return portionCount;
    }

    public void setPortionCount(int portionCount) {
        this.portionCount = portionCount;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getTimeToPrepare() {
        return timeToPrepare;
    }

    public void setTimeToPrepare(int timeToPrepare) {
        this.timeToPrepare = timeToPrepare;
    }

    public int getNutritionalValue() {
        return calculateNutritionalValue();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public HashMap<Ingredient, AmountInUnit> getIngredients() {
        return ingredients;
    }

    public void setIngredients(HashMap<Ingredient, AmountInUnit> ingredients) {
        this.ingredients = ingredients;
    }
    private int calculateNutritionalValue() {
        int nutritionalValue = 0;
        for (Map.Entry<Ingredient, AmountInUnit> entry: ingredients.entrySet()) {
            Unit unit = entry.getValue().getUnit();
            int amount = entry.getValue().getAmount();
            Ingredient ingredient = entry.getKey();
            nutritionalValue += ingredient.getTotalCalories(unit, amount);
        }

        return nutritionalValue;
    }

    public void addIngredient(Ingredient ingredient, Unit unit, int amount) {
        this.ingredients.putIfAbsent(ingredient, new AmountInUnit(unit, amount));
    }

    /**
     * @return true if ingredient found
     */
    public boolean deleteIngredient(Ingredient ingredient) {
        var deletedRecord = this.ingredients.remove(ingredient);
        return deletedRecord != null;
    }
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Recipe that)) {
            return false;
        }
        return this.title.equals(that.title);
    }

    @Override
    public String toString(){
        return this.title;
    }
}
