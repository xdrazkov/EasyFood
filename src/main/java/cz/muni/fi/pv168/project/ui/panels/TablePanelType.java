package cz.muni.fi.pv168.project.ui.panels;

public enum TablePanelType {
    RECIPE,
    INGREDIENT,
    CATEGORY,
    UNIT;

    public String getSingularName() {
        return first() + lower().substring(1);
    }

    public String getPluralName() {
        String middle = lower().substring(1, name().length() - 1);
        char end = lower().charAt(name().length() - 1);
        return first() + middle + ((end == 'y')? "ies": end + "s");
    }

    private String lower() {
        return name().toLowerCase();
    }

    private char first() {
        return Character.toUpperCase(name().charAt(0));
    }
}
