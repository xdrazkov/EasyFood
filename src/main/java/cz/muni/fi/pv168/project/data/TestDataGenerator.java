package cz.muni.fi.pv168.project.data;

import com.formdev.flatlaf.util.HSLColor;
import cz.muni.fi.pv168.project.model.*;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TestDataGenerator {
    private static final UuidGuidProvider guidProvider = new UuidGuidProvider();

    private final Random random = new Random(2L);
    private final List<Unit> units = List.of(
            new Unit("Pound", "lb", IngredientType.WEIGHABLE, 453.5F),
            new Unit("Kilogram", "kg", IngredientType.WEIGHABLE, 1000),
            new Unit("Liter", "l", IngredientType.POURABLE, 1000),
            new Unit("Cup", "cp", IngredientType.POURABLE, 237.58F),
            new Unit("Dozen", "dz", IngredientType.COUNTABLE, 12),
            new Unit("Pair", "pr", IngredientType.COUNTABLE, 2)
    );

    private final List<Category> categories = List.of(
            new Category("Soups", getRandomColor()),
            new Category("Lunch", getRandomColor()),
            new Category("Side dishes", getRandomColor()),
            new Category("Bread recipes", getRandomColor()),
            new Category("Appetizers and snacks", getRandomColor())
    );
    private final List<Ingredient> ingredients = List.of(
            new Ingredient("Flour", units.get(0), random.nextInt(2)+ 1),
            new Ingredient("Water", units.get(2), random.nextInt(2)+ 1),
            new Ingredient("Sugar", units.get(3), random.nextInt(2)+ 1),
            new Ingredient("Salt", units.get(0), random.nextInt(2)+ 1),
            new Ingredient("Eggs", units.get(4), random.nextInt(2)+ 1),
            new Ingredient("Yeast", units.get(1), random.nextInt(2)+ 1),
            new Ingredient("Butter", units.get(0), random.nextInt(2)+ 1),
            new Ingredient("Milk", units.get(2), random.nextInt(2)+ 1),
            new Ingredient("Tomatoes", units.get(0), random.nextInt(2)+ 1),
            new Ingredient("Cheese", units.get(0), random.nextInt(2)+ 1),
            new Ingredient("Chicken", units.get(0), random.nextInt(2)+ 1),
            new Ingredient("Onions", units.get(0), random.nextInt(2)+ 1)
    );

    // Create a list of recipes
    List<Recipe> recipes = List.of(
            new Recipe("Homemade Bread", "Delicious bread", 1,
                    "Mix ingredients and bake until golden brown. Serve warm.", 120, categories.get(3),
                    createIngredientList(ingredients, units)),
            new Recipe("Vegetable Soup", "Healthy and hearty", 4,
                    "Chop, boil, and season. Perfect for a cozy evening.", 30, categories.get(0),
                    createIngredientList(ingredients, units)),
            new Recipe("Chocolate Cake", "Rich and decadent", 8,
                    "Combine ingredients, bake, and enjoy this indulgent treat.", 60, categories.get(4),
                    createIngredientList(ingredients, units)),
            new Recipe("Chicken Parmesan", "Classic Italian dish", 4,
                    "Bread and fry chicken, layer with sauce and cheese, bake to perfection.", 45, categories.get(2),
                    createIngredientList(ingredients, units)),
            new Recipe("Fluffy Omelette", "Quick and easy", 2,
                    "Whisk eggs, pour into hot pan, add ingredients, flip, and serve. Breakfast made simple.", 15, categories.get(1),
                    createIngredientList(ingredients, units)),
            new Recipe("Pasta Salad", "Refreshing side", 6,
                    "Boil pasta, mix with veggies, and toss in dressing. Perfect for picnics and barbecues.", 20, categories.get(2),
                    createIngredientList(ingredients, units)),
            new Recipe("Cheese and Tomato Sandwich", "Simple and tasty", 1,
                    "Layer cheese and tomatoes between slices of bread. A classic sandwich for any occasion.", 10, categories.get(1),
                    createIngredientList(ingredients, units)),
            new Recipe("Fruit Smoothie", "Healthy and vibrant", 2,
                    "Blend fruits and yogurt until smooth. A refreshing and nutritious way to start the day.", 5, categories.get(4),
                    createIngredientList(ingredients, units)),
            // Add more recipes to reach a total of 15
            new Recipe("Grilled Chicken Salad", "Light and flavorful", 2,
                    "Grill chicken, toss with fresh veggies, and drizzle with vinaigrette. Perfect for a quick lunch.", 25, categories.get(0),
                    createIngredientList(ingredients, units)),
            new Recipe("Spaghetti Bolognese", "Classic Italian pasta", 4,
                    "Saute onions and garlic, brown beef, add tomatoes, and simmer. Serve over spaghetti.", 40, categories.get(2),
                    createIngredientList(ingredients, units)),
            new Recipe("Blueberry Pancakes", "Fluffy breakfast delight", 4,
                    "Mix batter, add blueberries, and cook until golden. Serve with maple syrup.", 30, categories.get(1),
                    createIngredientList(ingredients, units)),
            new Recipe("Mango Salsa", "Sweet and spicy", 6,
                    "Dice mango, tomato, onion, and cilantro. Mix with lime juice and chili. Serve with chips.", 15, categories.get(4),
                    createIngredientList(ingredients, units)),
            new Recipe("Garlic Butter Shrimp", "Succulent seafood", 3,
                    "Saute shrimp in garlic butter, sprinkle with parsley, and serve over pasta or rice.", 20, categories.get(0),
                    createIngredientList(ingredients, units))
    );
    private Color getRandomColor() {
        // HSL color range for generating pastel colors
        float h = random.nextFloat(0, 360);
        float s = random.nextFloat(0.3f * 100, 0.7f * 100);
        float l = random.nextFloat(0.7f * 100, 0.9f * 100);
        return new HSLColor(h, s, l).getRGB();
    }

    private static HashMap<Ingredient, Pair<Unit, Integer>> createIngredientList(List<Ingredient> ingredients, List<Unit> units) {
        HashMap<Ingredient, Pair<Unit, Integer>> ingredientList = new HashMap<>();
        Random random = new Random();

        for (Ingredient ingredient : ingredients) {
            // For simplicity, assume a random unit and amount for each ingredient
            Unit randomUnit = units.get(random.nextInt(units.size()));
            int randomAmount = random.nextInt(100) + 1; // Random amount between 1 and 100
            ingredientList.put(ingredient, Pair.of(randomUnit, randomAmount));
        }

        return ingredientList;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
