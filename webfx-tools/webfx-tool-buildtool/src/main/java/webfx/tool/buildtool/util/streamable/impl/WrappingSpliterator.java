package webfx.tool.buildtool.util.streamable.impl;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
interface WrappingSpliterator<T> extends Spliterator<T> {

    Spliterator<T> getWrappedSpliterator();

    @Override
    default boolean tryAdvance(Consumer<? super T> action) {
        return getWrappedSpliterator().tryAdvance(action);
    }

    @Override
    default Spliterator<T> trySplit() {
        return getWrappedSpliterator().trySplit();
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
    default void forEachRemaining(Consumer<? super T> action) {
        getWrappedSpliterator().forEachRemaining(action);
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
    default Comparator<? super T> getComparator() {
        return getWrappedSpliterator().getComparator();
    }
}
