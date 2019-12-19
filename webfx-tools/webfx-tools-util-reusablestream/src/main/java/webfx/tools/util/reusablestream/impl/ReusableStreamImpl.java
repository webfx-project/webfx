package webfx.tools.util.reusablestream.impl;

import webfx.tools.util.reusablestream.ReusableStream;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
public final class ReusableStreamImpl<T> implements ReusableStream<T> {

    private final Iterable<T> iterable;

    public ReusableStreamImpl(Iterable<T> iterable) {
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
    public ReusableStream<T> filter(Predicate<? super T> predicate) {
        return ReusableStream.create(new FilterOperator<>(this, predicate));
    }

    @Override
    public <R> ReusableStream<R> map(Function<? super T, ? extends R> mapper) {
        return ReusableStream.create(new MapOperator<>(this, mapper));
    }

    @Override
    public <R> ReusableStream<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        return ReusableStream.create(new FlatMapOperator<>(this, mapper));
    }

    @Override
    public ReusableStream<T> takeWhile(Predicate<? super T> predicate) {
        return ReusableStream.create(new TakeWhileOperator<>(this, predicate));
    }

    @Override
    public ReusableStream<T> distinct() {
        return ReusableStream.create(new DistinctOperator<>(this));
    }

    @Override
    public ReusableStream<T> resume() {
        return ReusableStream.create(new ResumeOperator<>(this));
    }

    @Override
    public ReusableStream<T> cache() {
        return ReusableStream.create(new CacheOperator<>(this));
    }

    @Override
    @SafeVarargs
    public final ReusableStream<T> concat(Iterable<? extends T>... iterables) {
        return ReusableStream.create(new ConcatOperator<>(this, iterables));
    }
}
