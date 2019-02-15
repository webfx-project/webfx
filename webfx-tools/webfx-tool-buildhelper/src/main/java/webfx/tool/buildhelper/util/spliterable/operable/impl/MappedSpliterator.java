package webfx.tool.buildhelper.util.spliterable.operable.impl;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
final class MappedSpliterator<T, R> implements Spliterator<R> {

    private final Spliterator<T> spliterator;
    private final Function<? super T, ? extends R> mapper;


    MappedSpliterator(Spliterator<T> spliterator, Function<? super T, ? extends R> mapper) {
        this.spliterator = spliterator;
        this.mapper = mapper;
    }

    private Consumer<? super T> mappedAction(Consumer<? super R> action) {
        return t -> action.accept(mapper.apply(t));
    }

    @Override
    public boolean tryAdvance(Consumer<? super R> action) {
        return spliterator.tryAdvance(mappedAction(action));
    }

    @Override
    public Spliterator<R> trySplit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        return spliterator.estimateSize();
    }

    @Override
    public int characteristics() {
        return spliterator.characteristics();
    }

    @Override
    public void forEachRemaining(Consumer<? super R> action) {
        spliterator.forEachRemaining(mappedAction(action));
    }

    @Override
    public long getExactSizeIfKnown() {
        return spliterator.getExactSizeIfKnown();
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return spliterator.hasCharacteristics(characteristics);
    }

    @Override
    public Comparator<? super R> getComparator() {
        throw new UnsupportedOperationException();
    }
}
