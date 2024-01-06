package cz.muni.fi.pv168.project.model;

public interface Settable<T> {
    /**
     * copies all properties of @param setObject
     */
    void setAll(T setObject);
}
