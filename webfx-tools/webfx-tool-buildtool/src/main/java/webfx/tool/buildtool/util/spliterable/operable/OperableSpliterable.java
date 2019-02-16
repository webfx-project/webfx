package webfx.tool.buildtool.util.spliterable.operable;

import webfx.tool.buildtool.util.spliterable.ThrowableSpliterable;
import webfx.tool.buildtool.util.spliterable.operable.impl.OperableSpliterableImpl;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Bruno Salmon
 */
public interface OperableSpliterable<T> extends Iterable<T> {

    OperableSpliterable<T> filter(Predicate<? super T> predicate);

    <R> OperableSpliterable<R> map(Function<? super T, ? extends R> mapper);

    <R> OperableSpliterable<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper);

    OperableSpliterable<T> distinct();

    OperableSpliterable<T> cache();

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    static <T> OperableSpliterable<T> fromIterable(Iterable<T> iterable) {
        return new OperableSpliterableImpl<>(iterable);
    }

    static <T> OperableSpliterable<T> fromSpliterable(ThrowableSpliterable<T> throwableSpliterable) {
        return fromIterable(throwableSpliterable.toSpliterable());
    }
}
