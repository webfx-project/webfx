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
        return Streamable.create(new FilterOperator<>(this, predicate));
    }

    @Override
    public <R> Streamable<R> map(Function<? super T, ? extends R> mapper) {
        return Streamable.create(new MapOperator<>(this, mapper));
    }

    @Override
    public <R> Streamable<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        return Streamable.create(new FlatMapOperator<>(this, mapper));
    }

    @Override
    public Streamable<T> takeWhile(Predicate<? super T> predicate) {
        return Streamable.create(new TakeWhileOperator<>(this, predicate));
    }

    @Override
    public Streamable<T> distinct() {
        return Streamable.create(new DistinctOperator<>(this));
    }

    @Override
    public Streamable<T> resume() {
        return Streamable.create(new ResumeOperator<>(this));
    }

    @Override
    public Streamable<T> cache() {
        return Streamable.create(new CacheOperator<>(this));
    }

    @Override
    @SafeVarargs
    public final Streamable<T> concat(Iterable<? extends T>... iterables) {
        return Streamable.create(new ConcatOperator<>(this, iterables));
    }
}
