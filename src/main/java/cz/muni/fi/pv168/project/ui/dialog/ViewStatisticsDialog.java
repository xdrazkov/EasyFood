package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.model.Unit;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ViewStatisticsDialog extends EntityDialog<String>  {

    private final JLabel total = new JLabel();
    private final JPanel ingredientPanel = new JPanel();

    private final List<Recipe> recipes;
    private final List<Ingredient> ingredients;


    public ViewStatisticsDialog(List<Recipe> recipes, List<Ingredient> ingredients) {
        this.recipes = recipes;
        this.ingredients = ingredients;
        setValues();
        addFields();
        addIngredients();
    }

    private void setValues() {
        total.setText("Total number of recipes is " + recipes.size());
        ingredientPanel.setLayout(new MigLayout("wrap 2"));
    }

    private void addFields() {
        panel.add(total);
        panel.add(ingredientPanel);
    }

    private void addIngredients(){
        for(Ingredient ingredient : ingredients){
            JLabel ingredientLabel = new JLabel();
            ingredientLabel.setText(ingredient.getName());
            ingredientLabel.setFont(ingredientLabel.getFont().deriveFont(Font.BOLD, 14f));
            ingredientPanel.add(ingredientLabel);
            JLabel ingredientLabel2 = new JLabel();
            ingredientLabel2.setText("is used in " + countInstances(ingredient) + " recipes");
            ingredientPanel.add(ingredientLabel2);
        }
    }

    public int countInstances(Ingredient ingredient){
        int count = 0;
        for(Recipe recipe : recipes){
            if(recipe.getIngredients().containsKey(ingredient)){
                count++;
            }
        }
        return count;
    }

    @Override
    String getEntity() {
        return "";
    }
}
