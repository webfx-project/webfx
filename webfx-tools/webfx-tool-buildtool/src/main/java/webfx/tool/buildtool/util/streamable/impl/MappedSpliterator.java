package webfx.tool.buildtool.util.streamable.impl;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
final class MappedSpliterator<T, R> implements Spliterator<R> {

    private final Spliterator<T> underlyingSpliterator;
    private final Function<? super T, ? extends R> mapper;


    MappedSpliterator(Spliterator<T> underlyingSpliterator, Function<? super T, ? extends R> mapper) {
        this.underlyingSpliterator = underlyingSpliterator;
        this.mapper = mapper;
    }

    private Consumer<? super T> mappedAction(Consumer<? super R> action) {
        return t -> action.accept(mapper.apply(t));
    }

    @Override
    public boolean tryAdvance(Consumer<? super R> action) {
        return underlyingSpliterator.tryAdvance(mappedAction(action));
    }

    @Override
    public Spliterator<R> trySplit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        return underlyingSpliterator.estimateSize();
    }

    @Override
    public int characteristics() {
        return underlyingSpliterator.characteristics();
    }

    @Override
    public void forEachRemaining(Consumer<? super R> action) {
        underlyingSpliterator.forEachRemaining(mappedAction(action));
    }

    @Override
    public long getExactSizeIfKnown() {
        return underlyingSpliterator.getExactSizeIfKnown();
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return underlyingSpliterator.hasCharacteristics(characteristics);
    }

    @Override
    public Comparator<? super R> getComparator() {
        throw new UnsupportedOperationException();
    }
}
