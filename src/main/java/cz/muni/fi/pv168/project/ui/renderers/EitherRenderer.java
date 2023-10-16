package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.util.Either;

import javax.swing.*;

public class EitherRenderer<L, R> extends AbstractRenderer<Either<L, R>> {

    private final AbstractRenderer<L> leftRenderer;
    private final AbstractRenderer<R> rightRenderer;

    public static <L, R> EitherRenderer<L, R> create(AbstractRenderer<L> leftRenderer,
                                                     AbstractRenderer<R> rightRenderer) {
        return new EitherRenderer<>(Either.class, leftRenderer, rightRenderer);
    }

    private EitherRenderer(Class<?> clazz, AbstractRenderer<L> leftRenderer, AbstractRenderer<R> rightRenderer) {
        super((Class<Either<L, R>>) clazz);
        this.leftRenderer = leftRenderer;
        this.rightRenderer = rightRenderer;
    }

    @Override
    protected void updateLabel(JLabel label, Either<L, R> value) {
        if (value != null) {
            value.apply(
                    l -> leftRenderer.updateLabel(label, l),
                    r -> rightRenderer.updateLabel(label, r)
            );
        }
    }
}

