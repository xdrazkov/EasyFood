package cz.muni.fi.pv168.project.service.validation;

import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.service.validation.common.StringLengthValidator;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import java.util.List;

public class UnitValidator implements Validator<Unit> {

    @Override
    public ValidationResult validate(Unit model) {
        var validators = List.of(
                Validator.extracting(Unit::getName, new StringLengthValidator(1, 150, "Unit name")),
                Validator.extracting(Unit::getAbbreviation, new StringLengthValidator(1, 10, "Unit abbrevation"))
        );

        ValidationResult validationResult = Validator.compose(validators).validate(model);
        if (model.getConversionRate() <= 0) {
            validationResult.add("Unit conversion rate has to be higher than 0");
        }

        Unit baseUnit = UnitTableModel.getBaseUnit(model.getIngredientType()); // null if only base units are added
        if (baseUnit != null && baseUnit.equals(model)) {
            validationResult.add(model + " is base unit, cannot be manipulated");
        }
        return validationResult;
    }
}
