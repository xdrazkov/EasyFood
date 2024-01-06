package cz.muni.fi.pv168.project.service.validation;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.service.validation.common.StringLengthValidator;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import java.util.List;

public class CategoryValidator implements Validator<Category> {

    @Override
    public ValidationResult validate(Category model) {
        var validators = List.of(
                Validator.extracting(Category::getName, new StringLengthValidator(1, 150, "Category name"))
        );

        ValidationResult validationResult = Validator.compose(validators).validate(model);

        if (model.getName().equals("No Category")) {
            validationResult.add("Default category \"No Category\" cannot be manipulated");
        }
        return validationResult;
    }
}
