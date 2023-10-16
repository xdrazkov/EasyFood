package cz.muni.fi.pv168.project.model;

import java.time.LocalDate;
import java.util.List;

public class Recipe {
    private String title;
    private String description;
    private int portionCount;
    private String instructions;
    private int timeToPrepare; // in minutes; import java.util.concurrent.TimeUnit
    private Category category;
    private List<Ingredient> ingredientList;

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

    public Recipe(String title, String description, int portionCount, String instructions, int timeToPrepare, Category category, List<Ingredient> ingredientList) {
        this.title = title;
        this.description = description;
        this.portionCount = portionCount;
        this.instructions = instructions;
        this.timeToPrepare = timeToPrepare;
        this.category = category;
        this.ingredientList = ingredientList;
    }

    public Recipe(){}

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }
}
