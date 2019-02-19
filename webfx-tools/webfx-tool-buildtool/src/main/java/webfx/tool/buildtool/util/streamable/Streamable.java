package webfx.tool.buildtool.util.streamable;

import webfx.tool.buildtool.util.streamable.impl.StreamableImpl;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Bruno Salmon
 */
public interface Streamable<T> extends Spliterable<T> {

    Streamable<T> filter(Predicate<? super T> predicate);

    <R> Streamable<R> map(Function<? super T, ? extends R> mapper);

    <R> Streamable<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper);

    Streamable<T> takeWhile(Predicate<? super T> predicate);

    Streamable<T> distinct();

    Streamable<T> resume();

    Streamable<T> cache();

    Streamable<T> concat(Iterable<? extends T>... iterables);

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    static <T> Streamable<T> create(Spliterable<T> spliterable) {
        return fromIterable(spliterable);
    }

    static <T> Streamable<T> fromIterable(Iterable<T> iterable) {
        return new StreamableImpl<>(iterable);
    }

    @SafeVarargs
    static <T> Streamable<T> of(T... array) {
        return fromIterable(Arrays.asList(array));
    }

    static <T> Streamable<T> concat(Streamable<T> a, Iterable<? extends T> b) {
        return a.concat(b);
    }

}
