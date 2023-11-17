package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.MainWindow;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;
import java.nio.charset.MalformedInputException;
import java.util.Optional;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;

abstract class EntityDialog<E> {

    protected final JPanel panel = new JPanel();
    protected static final int DIALOG_WIDTH = MainWindow.SCREEN_SIZE.width/5;
    protected static final int DIALOG_HEIGHT = MainWindow.SCREEN_SIZE.height/2;
    protected static final int THICC_HEIGHT = DIALOG_HEIGHT / 5;
    protected static final int THIN_HEIGHT = THICC_HEIGHT / 4;

    EntityDialog() {
        panel.setMaximumSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
//        panel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        panel.setLayout(new MigLayout("wrap 1"));
    }

    void add(String labelText, JComponent component, int thickness) {
        component.setPreferredSize(new Dimension(DIALOG_WIDTH, thickness));
        if (!labelText.isEmpty()) {
            var label = new JLabel(labelText);
            panel.add(label);
        }
        panel.add(component);
    }
    void add(JComponent component, int thickness) {
        add("", component, thickness);
    }
    
    abstract E getEntity();

    public Optional<E> show(JComponent parentComponent, String title) {
        int result = JOptionPane.showOptionDialog(parentComponent, panel, title,
                OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);
        E entity = getEntity();
        if (result == OK_OPTION && entity != null) {
            return Optional.of(entity);
        } else {
            return Optional.empty();
        }
    }
}
