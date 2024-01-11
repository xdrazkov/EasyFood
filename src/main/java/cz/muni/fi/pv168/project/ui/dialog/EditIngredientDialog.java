package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.service.validation.Validator;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import javax.swing.*;

public class EditIngredientDialog extends EntityDialog<Ingredient> {
    private final JTextField name = new JTextField();
    private final JFormattedTextField nutritionalValue = FieldMaker.makeFloatField();
    private final JComboBox<Unit> defaultUnit = new JComboBox<>();
    private final DependencyProvider dependencyProvider;
    private final Ingredient ingredient;

    public EditIngredientDialog(Ingredient ingredient, DependencyProvider dependencyProvider, Validator<Ingredient> ingredientValidator) {
        super(ingredientValidator);
        this.ingredient = ingredient;
        this.dependencyProvider = dependencyProvider;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(ingredient.getName());
        nutritionalValue.setText(ingredient.getCaloriesPerUnit() + "");
        defaultUnit.setModel(new javax.swing.DefaultComboBoxModel<>(dependencyProvider.getUnitCrudService().findAll().toArray(new Unit[0])));
        defaultUnit.getModel().setSelectedItem(ingredient.getDefaultUnit());
    }

    private void addFields() {
        add("Name:", name, THIN_HEIGHT);
        add("Default Unit:", defaultUnit, THIN_HEIGHT);
        add("Nutritional value(kcal) per default ingredient:", nutritionalValue, THIN_HEIGHT);
    }

    @Override
    Ingredient getEntity() {
        ingredient.setName(name.getText());
        ingredient.setDefaultUnit((Unit) defaultUnit.getSelectedItem());
        ingredient.setCaloriesPerUnit(Float.parseFloat(nutritionalValue.getValue().toString().replaceAll(" ", "")));
        return ingredient;
    }
}
