package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;

import javax.swing.*;
import java.util.List;

public class ViewAboutDialog extends EntityDialog<String> {

    private final JLabel authors = new JLabel();
    private final JLabel description = new JLabel();
    private final JLabel copyright = new JLabel();


    public ViewAboutDialog() {
        setValues();
        addFields();
    }

    private void setValues() {
        authors.setText("Peter Dražkovec    Timotej Hajzuš   Matúš Jakuboc   Jaroslav Petrisko");
        description.setText("Application made as a project for PV168 at FI MUNI.");
        copyright.setText("©2023");
    }

    private void addFields() {
        add("Application made by", authors, THIN_HEIGHT);
        add(description, THIN_HEIGHT);
        add(copyright, THIN_HEIGHT);
    }

    @Override
    String getEntity() {
        return "";
    }
}
