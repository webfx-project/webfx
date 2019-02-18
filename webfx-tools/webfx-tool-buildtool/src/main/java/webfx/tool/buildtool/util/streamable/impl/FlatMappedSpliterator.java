package webfx.tool.buildtool.util.streamable.impl;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
class FlatMappedSpliterator<T, R> implements Spliterator<R> {

    private final Spliterator<T> sourceSpliterator;
    private final Function<? super T, ? extends Iterable<? extends R>> mapper;
    private Spliterator<? extends R> runningMappedSpliterator;

    FlatMappedSpliterator(Spliterator<T> sourceSpliterator, Function<? super T, ? extends Iterable<? extends R>> mapper) {
        this.sourceSpliterator = sourceSpliterator;
        this.mapper = mapper;
    }

    private void setNextRunningMappedSpliterator(T t) {
        runningMappedSpliterator = mapper.apply(t).spliterator();
    }

    private boolean goToNextRunningMappedSpliterator() {
        runningMappedSpliterator = null;
        sourceSpliterator.tryAdvance(this::setNextRunningMappedSpliterator);
        return runningMappedSpliterator != null;
    }

    @Override
    public boolean tryAdvance(Consumer<? super R> action) {
        do {
            if (runningMappedSpliterator != null && runningMappedSpliterator.tryAdvance(action))
                return true;
        } while (goToNextRunningMappedSpliterator());
        return false;
    }

    @Override
    public void forEachRemaining(Consumer<? super R> action) {
        do {
            if (runningMappedSpliterator != null)
                runningMappedSpliterator.forEachRemaining(action);
        } while (goToNextRunningMappedSpliterator());
    }

    @Override
    public Spliterator<R> trySplit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        return sourceSpliterator.estimateSize() == 0 ? 0 : -1;
    }

    @Override
    public int characteristics() {
        return sourceSpliterator.characteristics() & ~SIZED;
    }

    @Override
    public long getExactSizeIfKnown() {
        return sourceSpliterator.getExactSizeIfKnown() == 0 ? 0 : -1;
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return (characteristics & SIZED) == 0 && sourceSpliterator.hasCharacteristics(characteristics);
    }

    @Override
    public Comparator<? super R> getComparator() {
        throw new UnsupportedOperationException();
    }
}
