package cz.muni.fi.pv168.project.ui.resources;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.net.URL;

public final class Icons {

    public static final Icon DELETE_ICON = createIcon("delete_action.png");
    public static final Icon EDIT_ICON = createIcon("edit_action.png");
    public static final Icon ADD_ICON = createIcon("add_action.png");
    public static final Icon OPEN_ICON = createIcon("open_action.png");
    public static final Icon IMPORT_ICON = createIcon("import_action.png");
    public static final Icon EXPORT_ICON = createIcon("export_action.png");

    private Icons() {
        throw new AssertionError("This class is not instantiable");
    }

    private static ImageIcon createIcon(String name) {
        URL url = Icons.class.getResource(name);
        if (url == null) {
            throw new IllegalArgumentException("Icon resource not found on classpath: " + name);
        }
        return new ImageIcon(url);
    }
}
