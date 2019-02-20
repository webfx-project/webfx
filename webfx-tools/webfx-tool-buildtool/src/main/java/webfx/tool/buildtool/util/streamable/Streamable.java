package webfx.tool.buildtool.util.streamable;

import webfx.tool.buildtool.util.streamable.impl.StreamableImpl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collector;
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

    // Terminal operations (forwarded to stream)

    // default void forEach(Consumer<? super T> action) { stream().forEach(action); }

    default void forEachOrdered(Consumer<? super T> action) { stream().forEachOrdered(action); }

    default T reduce(T identity, BinaryOperator<T> accumulator) { return stream().reduce(identity, accumulator); }

    default Optional<T> reduce(BinaryOperator<T> accumulator) { return stream().reduce(accumulator); }

    default <U> U reduce(U identity,
                 BiFunction<U, ? super T, U> accumulator,
                 BinaryOperator<U> combiner) { return stream().reduce(identity, accumulator, combiner); }

    default <R> R collect(Supplier<R> supplier,
                  BiConsumer<R, ? super T> accumulator,
                  BiConsumer<R, R> combiner) { return stream().collect(supplier, accumulator, combiner); }

    default <R, A> R collect(Collector<? super T, A, R> collector) { return stream().collect(collector); }

    default Optional<T> min(Comparator<? super T> comparator) { return stream().min(comparator); }

    default Optional<T> max(Comparator<? super T> comparator) { return stream().max(comparator); }

    default long count() { return stream().count(); }

    default boolean anyMatch(Predicate<? super T> predicate) { return stream().anyMatch(predicate); }

    default boolean allMatch(Predicate<? super T> predicate) { return stream().allMatch(predicate); }

    default boolean noneMatch(Predicate<? super T> predicate) { return stream().noneMatch(predicate); }

    default Optional<T> findFirst() { return stream().findFirst(); }

    default Optional<T> findAny() { return stream().findAny(); }

    // Static factory methods

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
