package cz.muni.fi.pv168.project.ui.filters.components;

import cz.muni.fi.pv168.project.ui.model.ComboBoxModelAdapter;
import cz.muni.fi.pv168.project.ui.model.CustomValuesModelDecorator;
import cz.muni.fi.pv168.project.ui.renderers.AbstractRenderer;
import cz.muni.fi.pv168.project.ui.renderers.EitherRenderer;
import cz.muni.fi.pv168.project.util.Either;

import javax.swing.*;
import java.util.function.Consumer;

/**
 * Builder for {@link JComboBox} with {@link Either} as elements.
 *
 * @param <L> Enum with special selectable values.
 * @param <R> Type of ordinary selectable values (entities).
 */
public class FilterComboboxBuilder<L extends Enum<L>, R>
{
    private final Class<L> clazz;
    private final ComboBoxModel<R> values;
    private AbstractRenderer<L> specialValuesRenderer;
    private AbstractRenderer<R> valuesRenderer;
    private Either<L, R> selectedItem;
    private Consumer<Either<L, R>> filter;

    private FilterComboboxBuilder(Class<L> clazz, ComboBoxModel<R> values)
    {
        this.clazz = clazz;
        this.values = values;
    }

    public static <L extends Enum<L>, R> FilterComboboxBuilder<L, R> create(Class<L> clazz, R[] values) {
        return new FilterComboboxBuilder<>(clazz, new DefaultComboBoxModel<>(values));
    }

    public static <L extends Enum<L>, R> FilterComboboxBuilder<L, R> create(Class<L> clazz, ListModel<R> values) {
        return new FilterComboboxBuilder<>(clazz, new ComboBoxModelAdapter<>(values));
    }

    public JComboBox<Either<L, R>> build() {
        var comboBox = new JComboBox<>(CustomValuesModelDecorator.addCustomValues(clazz, values));
        comboBox.setRenderer(EitherRenderer.create(specialValuesRenderer, valuesRenderer));
        comboBox.addActionListener(e -> filter.accept(comboBox.getItemAt(comboBox.getSelectedIndex())));
        if (selectedItem != null) {
            comboBox.setSelectedItem(selectedItem);
        }

        return comboBox;
    }

    public FilterComboboxBuilder<L, R> setSpecialValuesRenderer(AbstractRenderer<L> specialValuesRenderer)
    {
        this.specialValuesRenderer = specialValuesRenderer;
        return this;
    }

    public FilterComboboxBuilder<L, R> setValuesRenderer(AbstractRenderer<R> valuesRenderer)
    {
        this.valuesRenderer = valuesRenderer;
        return this;
    }

    public FilterComboboxBuilder<L, R> setSelectedItem(L selectedItem)
    {
        this.selectedItem = Either.left(selectedItem);
        return this;
    }

    public FilterComboboxBuilder<L, R> setSelectedItem(R selectedItem)
    {
        this.selectedItem = Either.right(selectedItem);
        return this;
    }

    public FilterComboboxBuilder<L, R> setFilter(Consumer<Either<L, R>> filter)
    {
        this.filter = filter;
        return this;
    }
}
