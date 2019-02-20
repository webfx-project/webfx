package webfx.tool.buildtool.util.reusablestream.impl;

import webfx.tool.buildtool.util.reusablestream.Spliterable;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
final class MapOperator<T, R> extends SpliteratorTransformOperator<T, R> {

    private final Function<? super T, ? extends R> mapper;

    MapOperator(Spliterable<T> deferredProvidedSpliteratorGetter, Function<? super T, ? extends R> mapper) {
        super(deferredProvidedSpliteratorGetter);
        this.mapper = mapper;
    }

    @Override
    MapOperation<T, R> newOperation() {
        return new MapOperation<>();
    }

    final class MapOperation<_T extends T, _R extends R> extends SpliteratorTransformOperation<_T, _R> {

        @Override
        Consumer<? super T> createMappedAction(Consumer<? super _R> action) {
            return t -> action.accept((_R) mapper.apply(t));
        }
    }
}
