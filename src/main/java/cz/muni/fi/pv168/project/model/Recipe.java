package cz.muni.fi.pv168.project.model;

import java.time.LocalDate;

public class Recipe {
    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Recipe(String title, String description) {
        setTitle(title);
        setDescription(description);
    }
}
