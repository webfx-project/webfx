package webfx.tool.buildtool.util.spliterable.operable.impl;

import webfx.tool.buildtool.util.spliterable.operable.OperableSpliterable;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
public final class OperableSpliterableImpl<T> implements OperableSpliterable<T> {

    private final Iterable<T> iterable;

    public OperableSpliterableImpl(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    @Override
    public Spliterator<T> spliterator() {
        return iterable.spliterator();
    }

    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }

    @Override
    public OperableSpliterable<T> filter(Predicate<? super T> predicate) {
        return OperableSpliterable.fromSpliterable(() -> new FilteredSpliterator<>(spliterator(), predicate));
    }

    @Override
    public <R> OperableSpliterable<R> map(Function<? super T, ? extends R> mapper) {
        return OperableSpliterable.fromSpliterable(() -> new MappedSpliterator<>(spliterator(), mapper));
    }

    @Override
    public <R> OperableSpliterable<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        return OperableSpliterable.fromSpliterable(() -> new FlatMappedSpliterator<>(spliterator(), mapper));
    }

    @Override
    public OperableSpliterable<T> distinct() {
        return OperableSpliterable.fromSpliterable(() -> new DistinctSpliterator<>(spliterator()));
    }

    @Override
    public OperableSpliterable<T> cache() {
        return OperableSpliterable.fromSpliterable(new CachedSpliterable<>(spliterator()));
    }
}
