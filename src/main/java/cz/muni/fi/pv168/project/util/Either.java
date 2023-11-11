package cz.muni.fi.pv168.project.util;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Generic class representing either Left or Right type. Exactly one of those types is present.
 *
 * @param <L> The left type
 * @param <R> The right type
 */
public abstract class Either<L, R> {
    /**
     * Create an instance with the L type.
     *
     * @param value The value to be placed into {@link Either}
     */
    public static <L, R> Either<L, R> left(L value) {
        Objects.requireNonNull(value, "left value must be valid");
        return new Either<>() {
            @Override
            public <T> T map(Function<? super L, ? extends T> lFunc,
                             Function<? super R, ? extends T> rFunc) {
                return lFunc.apply(value);
            }
        };
    }

    /**
     * Create an instance with the R type.
     *
     * @param value The value to be placed into {@link Either}
     */
    public static <L, R> Either<L, R> right(R value) {
        Objects.requireNonNull(value, "right value must be valid");
        return new Either<>() {
            @Override
            public <T> T map(Function<? super L, ? extends T> lFunc,
                             Function<? super R, ? extends T> rFunc) {
                return rFunc.apply(value);
            }

        };
    }

    private Either() {
    }

    /**
     * Execute either the first or the second function depending on the presence of either the left or the right type.
     *
     * @param lFunc A function to be applied if the {@link Either} contains the left value.
     * @param rFunc A function to be applied if the {@link Either} contains the right value.
     */
    public abstract <T> T map(Function<? super L, ? extends T> lFunc, Function<? super R, ? extends T> rFunc);

    /**
     * Execute either the first or the second consumer depending on the presence of either the left or the right type.
     *
     * @param lFunc A consumer to be applied if the {@link Either} contains the left value.
     * @param rFunc A consumer to be applied if the {@link Either} contains the right value.
     */
    public void apply(Consumer<? super L> lFunc, Consumer<? super R> rFunc) {
        map(consume(lFunc), consume(rFunc));
    }

    /**
     * Get the left value.
     *
     * @return A valid {@link Optional} in case the left value was present,
     * {@code Optional.empty()} otherwise.
     */
    public Optional<L> getLeft() {
        return this.map(
                Optional::of,
                r -> Optional.empty()
        );
    }

    /**
     * Get the right value.
     *
     * @return A valid {@link Optional} in case the right value was present,
     * {@code Optional.empty()} otherwise.
     */
    public Optional<R> getRight() {
        return this.map(
                l -> Optional.empty(),
                Optional::of
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Either<?, ?> other = (Either<?, ?>) o;

        return map(
                l -> other.getLeft().map(l::equals).orElse(false),
                r -> other.getRight().map(r::equals).orElse(false)
        );
    }

    @Override
    public int hashCode() {
        return map(
                Object::hashCode,
                Object::hashCode
        );
    }

    private <T> Function<T, Void> consume(Consumer<T> c) {
        return t -> {
            c.accept(t);
            return null;
        };
    }
}
