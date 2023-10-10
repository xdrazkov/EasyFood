package cz.muni.fi.pv168.project.model;

import java.awt.*;

public class Category {
    private String name;
    private Color color;
    private static final Color DEFAULT_COLOR = Color.WHITE;
    public Category(String name) {
        this.name = name;
        this.color = DEFAULT_COLOR;
    }
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
