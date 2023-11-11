package cz.muni.fi.pv168.project.ui.filters.components;

import cz.muni.fi.pv168.project.ui.model.CustomValuesModelDecorator;
import cz.muni.fi.pv168.project.ui.renderers.AbstractRenderer;
import cz.muni.fi.pv168.project.ui.renderers.EitherRenderer;
import cz.muni.fi.pv168.project.util.Either;

import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;

public class FilterListModelBuilder<L extends Enum<L>, R> {

    private final Class<L> clazz;
    private final ListModel<R> values;
    private AbstractRenderer<L> specialValuesRenderer;
    private AbstractRenderer<R> valuesRenderer;
    private Consumer<List<Either<L, R>>> filter;
    private int selectedIndex = 0;
    private int visibleRowsCount = 3;

    private FilterListModelBuilder(Class<L> clazz, ListModel<R> values) {
        this.clazz = clazz;
        this.values = values;
    }

    public static <L extends Enum<L>, R> FilterListModelBuilder<L, R> create(Class<L> clazz, ListModel<R> values) {
        return new FilterListModelBuilder<>(clazz, values);
    }

    public JList<Either<L, R>> build() {
        var jList = new JList<>(CustomValuesModelDecorator.addCustomValues(clazz, values));
        jList.setCellRenderer(EitherRenderer.create(specialValuesRenderer, valuesRenderer));
        jList.setSelectedIndex(selectedIndex);
        jList.setVisibleRowCount(visibleRowsCount);
        jList.addListSelectionListener(e -> filter.accept(jList.getSelectedValuesList()));

        return jList;
    }

    public FilterListModelBuilder<L, R> setSpecialValuesRenderer(AbstractRenderer<L> specialValuesRenderer)
    {
        this.specialValuesRenderer = specialValuesRenderer;
        return this;
    }

    public FilterListModelBuilder<L, R> setValuesRenderer(AbstractRenderer<R> valuesRenderer)
    {
        this.valuesRenderer = valuesRenderer;
        return this;
    }

    public FilterListModelBuilder<L, R> setSelectedIndex(int selectedIndex)
    {
        this.selectedIndex = selectedIndex;
        return this;
    }

    public FilterListModelBuilder<L, R> setVisibleRowsCount(int visibleRowsCount)
    {
        this.visibleRowsCount = visibleRowsCount;
        return this;
    }

    public FilterListModelBuilder<L, R> setFilter(Consumer<List<Either<L, R>>> filter)
    {
        this.filter = filter;
        return this;
    }
}
