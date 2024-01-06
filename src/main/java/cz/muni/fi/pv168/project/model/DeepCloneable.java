package cz.muni.fi.pv168.project.model;


public interface DeepCloneable<T> {
    /**
     * produces a deep copy of itself
     */
    T deepClone();
}
