package webfx.tool.buildtool.util.streamable.impl;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
abstract class DelegatingSpliterator<T> implements Spliterator<T> {

    private final Spliterator<T> delegateSpliterator;

    DelegatingSpliterator(Spliterator<T> delegateSpliterator) {
        this.delegateSpliterator = delegateSpliterator;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return delegateSpliterator.tryAdvance(action);
    }

    @Override
    public Spliterator<T> trySplit() {
        //return getSpliterator().trySplit();
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        return delegateSpliterator.estimateSize();
    }

    @Override
    public int characteristics() {
        return delegateSpliterator.characteristics();
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        delegateSpliterator.forEachRemaining(action);
    }

    @Override
    public long getExactSizeIfKnown() {
        return delegateSpliterator.getExactSizeIfKnown();
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return delegateSpliterator.hasCharacteristics(characteristics);
    }

    @Override
    public Comparator<? super T> getComparator() {
        return delegateSpliterator.getComparator();
    }
}
