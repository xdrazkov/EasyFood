package cz.muni.fi.pv168.project.service.validation.common;

import cz.muni.fi.pv168.project.service.validation.Validator;

/**
 * Validator based on Entity property values.
 *
 * @param <T> entity type
 */
public abstract class PropertyValidator<T> implements Validator<T> {

    public final String name;

    protected PropertyValidator(String name) {
        this.name = name;
    }

    protected String getName() {
        return name;
    }

    protected String getName(Object defaultName) {
        return(name != null) ? getName() : String.valueOf(defaultName);
    }
}
