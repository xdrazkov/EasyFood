package cz.muni.fi.pv168.project.service.crud;

/**
 * Thrown, if there is already an existing entity.
 *
 * @author Vojtech Sassmann
 * @since 1.0.0
 */
public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String message)
    {
        super(message);
    }
}
