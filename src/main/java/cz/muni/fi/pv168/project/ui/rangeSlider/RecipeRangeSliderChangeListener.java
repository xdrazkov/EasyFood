package cz.muni.fi.pv168.project.ui.rangeSlider;

import cz.muni.fi.pv168.project.util.Either;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.function.Consumer;

public class RecipeRangeSliderChangeListener<T> implements ChangeListener {
    private final Consumer<Either<T, Pair<Integer, Integer>>> filterFunction;

    public RecipeRangeSliderChangeListener(Consumer<Either<T, Pair<Integer, Integer>>> filterFunction) {
        this.filterFunction = filterFunction;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        RangeSlider rangeSlider = (RangeSlider) e.getSource();
        Integer selectedLowerVal = rangeSlider.getValue();
        Integer selectedUpperVal = rangeSlider.getUpperValue();
        this.filterFunction.accept(Either.right(Pair.of(selectedLowerVal, selectedUpperVal)));
    }
}

