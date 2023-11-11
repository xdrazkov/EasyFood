package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.model.Ingredient;

import javax.swing.*;

public class IngredientRenderer extends AbstractRenderer<Ingredient> {

    public IngredientRenderer() {
        super(Ingredient.class);
    }

    @Override
    protected void updateLabel(JLabel label, Ingredient ingredient) {
        if (ingredient != null){
            label.setText(ingredient.getName());
        }
    }
}
