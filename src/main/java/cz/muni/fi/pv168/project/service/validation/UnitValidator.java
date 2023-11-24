package cz.muni.fi.pv168.project.service.validation;

import cz.muni.fi.pv168.project.model.Unit;
import cz.muni.fi.pv168.project.service.validation.common.StringLengthValidator;

import java.util.List;

public class UnitValidator implements Validator<Unit> {

    @Override
    public ValidationResult validate(Unit model) {
        var validators = List.of(
                Validator.extracting(Unit::getName, new StringLengthValidator(1, 150, "Unit name"))
        );

        return Validator.compose(validators).validate(model);
    }
}
