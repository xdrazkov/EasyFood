package cz.muni.fi.pv168.project.data;

import com.formdev.flatlaf.util.HSLColor;
import cz.muni.fi.pv168.project.model.*;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TestDataGenerator {
    private static final UuidGuidProvider guidProvider = new UuidGuidProvider();

    private final Random random = new Random();


    public Category createTestCategory(int index) {
        // HSL color range for generating pastel colors
        float h = random.nextFloat(0, 360);
        float s = random.nextFloat(0.3f * 100, 0.7f * 100);
        float l = random.nextFloat(0.7f * 100, 0.9f * 100);
        HSLColor hslColor = new HSLColor(h, s, l);
        return new Category("Category " + index, hslColor.getRGB());
    }

    public Ingredient createTestIngredient(int index) {
        int pick = random.nextInt(IngredientType.values().length);
        return new Ingredient("Ingredient " + index, createTestUnit(index), random.nextInt(10, 100));
    }

    public Unit createTestUnit(int index) {
        int pick = random.nextInt(IngredientType.values().length);
        return new Unit("Unit " + index, "abb " + index,IngredientType.values()[pick], index);
    }

    public Recipe createTestRecipe(int index, Category category, HashMap<Ingredient, Pair<Unit, Integer>> ingredient, Unit unit) {
        return new Recipe( guidProvider.newGuid(),"Recipe " + index,
                "Description for " + index,
                random.nextInt(1, 10),
                "Instruction for Recipe "+ index,
                random.nextInt(10, 100),
                category,
                ingredient
                );
    }

    public List<Category> createTestCategories(int count){
        return IntStream.range(0, count)
                .mapToObj(this::createTestCategory)
                .collect(Collectors.toList());
    }

    public List<Ingredient> createTestIngredients(int count){
        return IntStream.range(0, count)
                .mapToObj(this::createTestIngredient)
                .collect(Collectors.toList());
    }

    public List<Unit> createTestUnits(int count){
        return IntStream.range(0, count)
                .mapToObj(this::createTestUnit)
                .collect(Collectors.toList());
    }

    public List<Recipe> createTestRecipes(int count,List<Category> categories, List<Ingredient> ingredients, List<Unit> units){
        List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            recipes.add(createTestRecipe(i, selectRandom(categories), generateRandomIngredientsForRecipe(ingredients, units), selectRandom(units)));
        }
        return recipes;
    }

    public HashMap<Ingredient, Pair<Unit, Integer>> generateRandomIngredientsForRecipe (List<Ingredient> data, List<Unit> units){
        int nextSize = random.nextInt(1, Math.min(data.size(), units.size()));
        HashMap<Ingredient, Pair<Unit, Integer>> sublist = new HashMap<>();
        for (int i = 0; i < nextSize; i++) {
            Ingredient selected = selectRandom(data);
            if (!sublist.containsKey(selected))
                sublist.put(selected, new MutablePair<>(units.get(i), i));
        }
        return sublist;
    }

    private <T> List<T> selectRandomMultiple(List<T> data) {
        int nextSize = random.nextInt(1, data.size());
        List<T> sublist = new ArrayList<>(nextSize);
        for (int i = 0; i < nextSize; i++) {
            T selected = selectRandom(data);
            if (!sublist.contains(selected))
                sublist.add(selected);
        }
        return sublist;
    }

    private <T> T selectRandom(List<T> data) {
        int index = random.nextInt(data.size());
        return data.get(index);
    }
}
