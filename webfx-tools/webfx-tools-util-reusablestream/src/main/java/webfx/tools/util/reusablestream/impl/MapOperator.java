package webfx.tools.util.reusablestream.impl;

import webfx.tools.util.reusablestream.Spliterable;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
final class MapOperator<T, R> extends Operator<T, R> {

    private final Function<? super T, ? extends R> mapper;

    MapOperator(Spliterable<T> wrappedSpliterable, Function<? super T, ? extends R> mapper) {
        super(wrappedSpliterable);
        this.mapper = mapper;
    }

    @Override
    MapOperation<T, R> newOperation() {
        return new MapOperation<>();
    }

    final class MapOperation<_T extends T, _R extends R> extends Operation<_T, _R> {
        @Override
        Consumer<? super T> createWrappedAction(Consumer<? super _R> action) {
            return t -> action.accept((_R) mapper.apply(t));
        }
    }
}
