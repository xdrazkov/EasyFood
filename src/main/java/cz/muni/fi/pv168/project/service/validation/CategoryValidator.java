package cz.muni.fi.pv168.project.service.validation;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.service.validation.common.StringLengthValidator;

import java.util.List;

public class CategoryValidator implements Validator<Category> {

    @Override
    public ValidationResult validate(Category model) {
        var validators = List.of(
                Validator.extracting(Category::getName, new StringLengthValidator(1, 150, "Category name"))
        );

        return Validator.compose(validators).validate(model);
    }
}
