package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.model.Recipe;

import javax.swing.*;

public class PreparationTimeRenderer extends AbstractRenderer<Recipe>{

    public PreparationTimeRenderer() {
        super(Recipe.class);
    }

    @Override
    protected void updateLabel(JLabel label, Recipe value) {
        if (value != null){
            label.setText(value.getTimeToPrepare() + " min");
        }
    }
}
