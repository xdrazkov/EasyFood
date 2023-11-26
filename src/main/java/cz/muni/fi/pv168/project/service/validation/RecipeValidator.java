package cz.muni.fi.pv168.project.service.validation;

import cz.muni.fi.pv168.project.model.AmountInUnit;
import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.service.validation.common.StringLengthValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeValidator implements Validator<Recipe> {

    @Override
    public ValidationResult validate(Recipe model) {
        ValidationResult validationResult = new ValidationResult();

        var stringValidationResult = new StringLengthValidator(2, 150, "Recipe title").validate(model.getTitle());
        if (!stringValidationResult.isValid()) {
            validationResult.add(stringValidationResult.getValidationErrors());
        } if (model.getIngredients().isEmpty()) {
            validationResult.add("Recipe needs to contain at least one ingredient");
        } if (model.getIngredients().containsKey(null)) {
            validationResult.add("Ingredient field cannot be empty");
        }
        for (Map.Entry<Ingredient, AmountInUnit> entry: model.getIngredients().entrySet()) {
            var amountInUnit = entry.getValue();
            var ingredient = entry.getKey();
            // already checked
            if (ingredient == null) {
                continue;
            }
            if (amountInUnit.getUnit() == null) {
                validationResult.add("Unit not stated for ingredient " + ingredient.getName());
            }
            if (amountInUnit.getAmount() == 0) {
                validationResult.add("Non-zero amount required for ingredient " + ingredient.getName());
            }
        }
        return validationResult;
    }


}