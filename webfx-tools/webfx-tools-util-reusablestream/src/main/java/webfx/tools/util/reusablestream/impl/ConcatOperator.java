package webfx.tools.util.reusablestream.impl;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author Bruno Salmon
 */
final class ConcatOperator<T> extends FlatMapOperator<Integer, T> {

    @SafeVarargs
    ConcatOperator(Iterable<T> firstIterable, Iterable<? extends T>... nextIterables) {
        super(() -> Arrays.spliterator(IntStream.iterate(-1, i -> i < nextIterables.length, i -> i + 1).toArray()),
                i -> i == -1 ? firstIterable : nextIterables[i]);
    }
}
