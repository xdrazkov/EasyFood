package cz.muni.fi.pv168.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cz.muni.fi.pv168.project.export.json.deserializers.CategoryJsonDeserializer;
import cz.muni.fi.pv168.project.export.json.seralizers.CategoryJsonSerializer;

import java.awt.*;

@JsonDeserialize(using = CategoryJsonDeserializer.class)
@JsonSerialize(using = CategoryJsonSerializer.class)
public class Category extends Entity {
    private String name;
    private Color color;

    public Category(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public Category(String guid, String name, Color color) {
        super(guid);
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

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Category that)) {
            return false;
        }
        return this.color.equals(that.color) && this.name.equals(that.name);
    }

    @Override
    public Category deepClone() {
        return new Category(getGuid(),  getName(), getColor());
    }

    @Override
    public void setAll(Entity setObject) {
        if (!(setObject instanceof Category setCategory)) {
            return;
        }

        this.setName(setCategory.getName());
        this.setColor(setCategory.getColor());
    }
}
