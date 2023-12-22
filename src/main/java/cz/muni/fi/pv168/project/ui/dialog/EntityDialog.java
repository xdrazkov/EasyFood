package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.service.validation.Validator;
import cz.muni.fi.pv168.project.ui.MainWindow;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;

public abstract class EntityDialog<E> {

    protected final JPanel panel = new JPanel();
    protected final Validator<E> entityValidator;
    protected static final int DIALOG_WIDTH = MainWindow.SCREEN_SIZE.width/5;
    protected static final int DIALOG_HEIGHT = MainWindow.SCREEN_SIZE.height/2;
    protected static final int THICC_HEIGHT = DIALOG_HEIGHT / 5;
    protected static final int THIN_HEIGHT = THICC_HEIGHT / 4;

    EntityDialog(Validator<E> entityValidator) {
        this.entityValidator = entityValidator;
        panel.setMaximumSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
//        panel.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        panel.setLayout(new MigLayout("wrap 1"));
    }
    EntityDialog() {
        this(null);
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
        int result = showOptionDialog(parentComponent, title);
        while (result == OK_OPTION) {
            E entity = getEntity();
            if (entity == null) {
                return Optional.empty();
            }

            if (entityValidator == null) {
                return Optional.of(entity);
            }

            var validation = entityValidator.validate(entity);
            if (validation.isValid()) {
                return Optional.of(entity);
            }

            openErrorDialog(validation);
            result = showOptionDialog(parentComponent, title);
        }
        return Optional.empty();
    }

    private int showOptionDialog(JComponent parentComponent, String title) {
        return JOptionPane.showOptionDialog(
                parentComponent, panel, title, OK_CANCEL_OPTION, PLAIN_MESSAGE,
                null, null, null
        );
    }

    public static void openErrorDialog(ValidationResult validationResult) {
        openErrorDialog(validationResult.getValidationErrors());
    }

    public static void openErrorDialog(String errorMessage) {
        openErrorDialog(List.of(errorMessage));
    }

    public static void openErrorDialog(Collection<String> validationErrors) {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String validationError : validationErrors) {
            listModel.addElement(validationError);
        }

        JList<String> errorList = new JList<>(listModel);

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(errorList));

        JOptionPane.showMessageDialog(null, panel,"Action not successful!", JOptionPane.ERROR_MESSAGE);
    }
}
