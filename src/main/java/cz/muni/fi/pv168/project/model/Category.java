package cz.muni.fi.pv168.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.*;

public class Category {
    private String name;
    private Color color;
    public Category(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     *     substitution for getClass, see usage
     */
    @JsonIgnore
    public Category getItself() {
        return this;
    }
}
