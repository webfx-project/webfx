package webfx.tool.buildhelper.util.spliterable.operable.impl;

import webfx.tool.buildhelper.util.spliterable.Spliterable;
import webfx.tool.buildhelper.util.spliterable.operable.OperableSpliterable;

import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
public final class OperableSpliterableImpl<T> implements OperableSpliterable<T> {

    private final Spliterable<T> builder;

    public OperableSpliterableImpl(Spliterable<T> builder) {
        this.builder = builder;
    }

    @Override
    public Spliterator<T> buildSpliterator() {
        return builder.buildSpliterator();
    }

    @Override
    public OperableSpliterable<T> filter(Predicate<? super T> predicate) {
        return OperableSpliterable.create(() -> new FilteredSpliterator<>(buildSpliterator(), predicate));
    }

    @Override
    public <R> OperableSpliterable<R> map(Function<? super T, ? extends R> mapper) {
        return OperableSpliterable.create(() -> new MappedSpliterator<>(buildSpliterator(), mapper));
    }

    @Override
    public OperableSpliterable<T> cache() {
        return OperableSpliterable.create(new CachedSpliterable<>(buildSpliterator()));
    }
}
