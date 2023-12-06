package cz.muni.fi.pv168.project.service.validation;

import cz.muni.fi.pv168.project.model.Ingredient;
import cz.muni.fi.pv168.project.service.validation.common.StringLengthValidator;

import java.util.List;

public class IngredientValidator implements Validator<Ingredient> {

    @Override
    public ValidationResult validate(Ingredient model) {
        var validators = List.of(
                Validator.extracting(Ingredient::getName, new StringLengthValidator(1, 150, "Ingredient name"))
        );

        return Validator.compose(validators).validate(model);
    }
}
