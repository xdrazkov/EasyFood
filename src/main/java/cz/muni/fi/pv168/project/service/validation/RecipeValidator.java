package cz.muni.fi.pv168.project.service.validation;

import cz.muni.fi.pv168.project.model.Recipe;
import cz.muni.fi.pv168.project.service.validation.common.StringLengthValidator;

import java.util.List;

public class RecipeValidator implements Validator<Recipe> {

    @Override
    public ValidationResult validate(Recipe model) {
        var validators = List.of(
                Validator.extracting(Recipe::getTitle, new StringLengthValidator(2, 150, "Recipe title"))
        );

        return Validator.compose(validators).validate(model);
    }
}