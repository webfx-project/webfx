package webfx.tool.buildtool.util.streamable.impl;

import webfx.tool.buildtool.util.streamable.Spliterable;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.stream.IntStream;

/**
 * @author Bruno Salmon
 */
final class ConcatSpliterator<T> extends FlatMappedSpliterator<Integer, T> {

    @SafeVarargs
    ConcatSpliterator(Spliterator<T> underlyingSpliterator, Iterable<? extends T>... iterables) {
        super(Arrays.spliterator(IntStream.iterate(-1, i -> i < iterables.length, i -> i + 1).toArray()),
                i -> i == -1 ? (Spliterable<T>) () -> underlyingSpliterator : iterables[i]);
    }
}
