package webfx.tool.buildtool.util.spliterable.operable.impl;

import webfx.tool.buildtool.util.spliterable.Spliterable;
import webfx.tool.buildtool.util.spliterable.ThrowableSpliterable;
import webfx.tool.buildtool.util.spliterable.operable.OperableSpliterable;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
public final class OperableSpliterableImpl<T> implements OperableSpliterable<T> {

    private final ThrowableSpliterable<T> spliterable;

    public OperableSpliterableImpl(ThrowableSpliterable<T> spliterable) {
        this.spliterable = spliterable;
    }

    @Override
    public Spliterator<T> spliterator() {
        try {
            return spliterable.spliterator();
        } catch (Exception e) {
            e.printStackTrace();
            return Spliterators.emptySpliterator();
        }
    }

    @Override
    public OperableSpliterable<T> filter(Predicate<? super T> predicate) {
        return OperableSpliterable.create(() -> new FilteredSpliterator<>(spliterator(), predicate));
    }

    @Override
    public <R> OperableSpliterable<R> map(Function<? super T, ? extends R> mapper) {
        return OperableSpliterable.create(() -> new MappedSpliterator<>(spliterator(), mapper));
    }

    @Override
    public <R> OperableSpliterable<R> flatMap(Function<? super T, ? extends Spliterable<? extends R>> mapper) {
        return OperableSpliterable.create(() -> new FlatMappedSpliterator<>(spliterator(), mapper));
    }

    @Override
    public OperableSpliterable<T> distinct() {
        return OperableSpliterable.create(() -> new DistinctSpliterator<>(spliterator()));
    }

    @Override
    public OperableSpliterable<T> cache() {
        return OperableSpliterable.create(new CachedSpliterable<>(spliterator()));
    }
}
