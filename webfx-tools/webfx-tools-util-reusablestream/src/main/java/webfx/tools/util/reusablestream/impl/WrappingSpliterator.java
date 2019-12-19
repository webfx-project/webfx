package webfx.tools.util.reusablestream.impl;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
interface WrappingSpliterator<T, R> extends Spliterator<R> {

    Spliterator<T> getWrappedSpliterator();

    Consumer<? super T> getWrappedAction(Consumer<? super R> action);

    @Override
    default boolean tryAdvance(Consumer<? super R> action) {
        return getWrappedSpliterator().tryAdvance(getWrappedAction(action));
    }

    @Override
    default void forEachRemaining(Consumer<? super R> action) {
        getWrappedSpliterator().forEachRemaining(getWrappedAction(action));
    }

    @Override
    default long estimateSize() {
        return getWrappedSpliterator().estimateSize();
    }

    @Override
    default int characteristics() {
        return getWrappedSpliterator().characteristics();
    }

    @Override
    default long getExactSizeIfKnown() {
        return getWrappedSpliterator().getExactSizeIfKnown();
    }

    @Override
    default boolean hasCharacteristics(int characteristics) {
        return getWrappedSpliterator().hasCharacteristics(characteristics);
    }

    @Override
    default Spliterator<R> trySplit() {
        throw new UnsupportedOperationException();
    }

    @Override
    default Comparator<? super R> getComparator() {
        throw new UnsupportedOperationException();
    }
}
