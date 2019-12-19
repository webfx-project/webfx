package webfx.tools.util.reusablestream;

import webfx.tools.util.reusablestream.impl.ReusableStreamImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Bruno Salmon
 */
public interface ReusableStream<T> extends Spliterable<T> {

    // API Specific methods (not shared with Stream)

    ReusableStream<T> cache();

    ReusableStream<T> resume();

    ReusableStream<T> concat(Iterable<? extends T>... iterables);

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    // Methods shared with Stream API

    ReusableStream<T> filter(Predicate<? super T> predicate);

    <R> ReusableStream<R> map(Function<? super T, ? extends R> mapper);

    <R> ReusableStream<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper);

    ReusableStream<T> takeWhile(Predicate<? super T> predicate);

    ReusableStream<T> distinct();

    // Terminal operations (forwarded to stream)

    // default void forEach(Consumer<? super T> action) { stream().forEach(action); } // Not necessary as Iterable already has a forEach() default implementation

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


    /**************************
     * Static factory methods *
     *************************/

    static <T> ReusableStream<T> create(Spliterable<T> spliterable) {
        return fromIterable(spliterable);
    }

    static <T> ReusableStream<T> create(Supplier<ReusableStream<T>> factory) {
        return create(() -> factory.get().spliterator());
    }

    static <T> ReusableStream<T> fromIterable(Iterable<T> iterable) {
        return new ReusableStreamImpl<>(iterable);
    }

    static <T> ReusableStream<T> empty() {
        return fromIterable(Collections.emptyList());
    }

    @SafeVarargs
    static <T> ReusableStream<T> of(T... array) {
        return fromIterable(Arrays.asList(array));
    }

    static <T> ReusableStream<T> concat(ReusableStream<T> a, Iterable<? extends T> b) {
        return a.concat(b);
    }

    static <T> ReusableStream<T> concat(ReusableStream<T> a, Iterable<? extends T> b, Iterable<? extends T> c) {
        return a.concat(b).concat(c);
    }

    static <T> ReusableStream<T> concat(ReusableStream<T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d) {
        return a.concat(b).concat(c).concat(d);
    }

    static <T> ReusableStream<T> concat(ReusableStream<T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d, Iterable<? extends T> e) {
        return a.concat(b).concat(c).concat(d).concat(e);
    }

    static <T> ReusableStream<T> concat(ReusableStream<T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d, Iterable<? extends T> e, Iterable<? extends T> f) {
        return a.concat(b).concat(c).concat(d).concat(e).concat(f);
    }
}
