package webfx.tool.buildtool.util.streamable.impl;

import webfx.tool.buildtool.util.streamable.Streamable;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
public final class StreamableImpl<T> implements Streamable<T> {

    private final Iterable<T> iterable;

    public StreamableImpl(Iterable<T> iterable) {
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
    public Streamable<T> filter(Predicate<? super T> predicate) {
        return Streamable.fromSpliterable(() -> new FilteredSpliterator<>(spliterator(), predicate));
    }

    @Override
    public <R> Streamable<R> map(Function<? super T, ? extends R> mapper) {
        return Streamable.fromSpliterable(() -> new MappedSpliterator<>(spliterator(), mapper));
    }

    @Override
    public <R> Streamable<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        return Streamable.fromSpliterable(() -> new FlatMappedSpliterator<>(spliterator(), mapper));
    }

    @Override
    public Streamable<T> takeWhile(Predicate<? super T> predicate) {
        return Streamable.fromSpliterable(() -> new TakeWhileSpliterator<>(spliterator(), predicate));
    }

    @Override
    public Streamable<T> distinct() {
        return Streamable.fromSpliterable(() -> new DistinctSpliterator<>(spliterator()));
    }

    @Override
    public Streamable<T> resume() {
        return Streamable.fromSpliterable(new ResumeSpliterable<>(this));
    }

    @Override
    public Streamable<T> cache() {
        return Streamable.fromSpliterable(new CachedSpliterable<>(this));
    }

    @Override
    @SafeVarargs
    public final Streamable<T> concat(Iterable<? extends T>... iterables) {
        return Streamable.fromSpliterable(() -> new ConcatSpliterator<>(spliterator(), iterables));
    }
}
