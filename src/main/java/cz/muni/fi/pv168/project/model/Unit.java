package cz.muni.fi.pv168.project.model;

public class Unit {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit(String name) {
        this.name = name;
    }

    private String name;
}
