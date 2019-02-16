package webfx.tool.buildtool.util.streamable.impl;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
final class FlatMappedSpliterator<T, R> implements Spliterator<R> {

    private final Spliterator<T> originalSpliterator;
    private final Function<? super T, ? extends Iterable<? extends R>> mapper;
    private Spliterator<? extends R> runningSpliterator;

    FlatMappedSpliterator(Spliterator<T> underlyingSpliterator, Function<? super T, ? extends Iterable<? extends R>> mapper) {
        this.originalSpliterator = underlyingSpliterator;
        this.mapper = mapper;
    }

    private void nextRunningSpliterator(T t) {
        runningSpliterator = mapper.apply(t).spliterator();
    }

    @Override
    public boolean tryAdvance(Consumer<? super R> action) {
        while (true) {
            if (runningSpliterator != null && runningSpliterator.tryAdvance(action))
                return true;
            runningSpliterator = null;
            if (!originalSpliterator.tryAdvance(this::nextRunningSpliterator))
                return false;
        }
    }

    @Override
    public void forEachRemaining(Consumer<? super R> action) {
        while (true) {
            if (runningSpliterator != null)
                runningSpliterator.forEachRemaining(action);
            runningSpliterator = null;
            if (!originalSpliterator.tryAdvance(this::nextRunningSpliterator))
                return;
        }
    }

    @Override
    public Spliterator<R> trySplit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        return originalSpliterator.estimateSize();
    }

    @Override
    public int characteristics() {
        return originalSpliterator.characteristics() & ~SIZED;
    }

    @Override
    public long getExactSizeIfKnown() {
        if (originalSpliterator.getExactSizeIfKnown() == 0)
            return 0;
        return -1;
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return (characteristics & SIZED) == 0 && originalSpliterator.hasCharacteristics(characteristics);
    }

    @Override
    public Comparator<? super R> getComparator() {
        throw new UnsupportedOperationException();
    }
}
