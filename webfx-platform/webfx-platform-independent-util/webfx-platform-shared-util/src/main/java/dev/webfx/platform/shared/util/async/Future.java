package dev.webfx.platform.shared.util.async;

import dev.webfx.platform.shared.util.function.Callable;
import dev.webfx.platform.shared.util.tuples.Unit;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Bruno Salmon
 */
public interface Future<T> extends AsyncResult<T> {
    /**
     * Create a future that hasn't completed yet
     *
     * @param <T>  the result type
     * @return  the future
     */
    static <T> Future<T> future() {
        return new FutureImpl<>();
    }

    /**
     * Create a succeeded future with a null result
     *
     * @param <T>  the result type
     * @return  the future
     */
    static <T> Future<T> succeededFuture() {
        return new FutureImpl<>((T)null);
    }

    /**
     * Created a succeeded future with the specified result.
     *
     * @param result  the result
     * @param <T>  the result type
     * @return  the future
     */
    static <T> Future<T> succeededFuture(T result) {
        return new FutureImpl<>(result);
    }

    /**
     * Create a failed future with the specified failure cause.
     *
     * @param t  the failure cause as a Throwable
     * @param <T>  the result type
     * @return  the future
     */
    static <T> Future<T> failedFuture(Throwable t) {
        return new FutureImpl<>(t);
    }

    /**
     * Create a failed future with the specified failure message.
     *
     * @param failureMessage  the failure message
     * @param <T>  the result type
     * @return  the future
     */
    static <T> Future<T> failedFuture(String failureMessage) {
        return new FutureImpl<>(failureMessage, true);
    }

    /**
     * Wrap a runnable into a future that complete immediately or fail if an exception is thrown.
     *
     * @param runnable  the runnable
     * @return  the future
     */
    static  <T,R> Future<R> runAsync(Runnable runnable) {
        try {
            runnable.run();
            return succeededFuture();
        } catch (Throwable t) {
            // temporary tracing the exception while exception handling mechanism is not finished
            Logger.getGlobal().log(Level.SEVERE, "Exception raised in Future.runAsync()", t);
            return failedFuture(t);
        }
    }

    /**
     * Wrap a consumer into a future that complete immediately or fail if an exception is thrown.
     *
     * @param consumer  the consumer
     * @param arg  the argument to pass to the consumer
     * @param <T>  the argument type
     * @return  the future
     */
    static <T,R> Future<R> consumeAsync(Consumer<T> consumer, T arg) {
        try {
            consumer.accept(arg);
            return succeededFuture();
        } catch (Throwable t) {
            // temporary tracing the exception while exception handling mechanism is not finished
            Logger.getGlobal().log(Level.SEVERE, "Exception raised in Future.consumeAsync()", t);
            return failedFuture(t);
        }
    }

    /**
     * The result of the operation. This will be null if the operation failed.
     */
    T result();

    /**
     * An exception describing failure. This will be null if the operation succeeded.
     */
    Throwable cause();

    /**
     * Did it succeed?
     */
    boolean succeeded();

    /**
     * Did it fail?
     */
    boolean failed();

    /**
     * Has it completed?
     */
    boolean isComplete();

    /**
     * Set a handler for the result. It will get called when it's complete
     */
    void setHandler(Handler<AsyncResult<T>> handler);

    void complete(T result);

    void complete();

    default void complete(AsyncResult<T> ar) {
        if (ar.succeeded())
            complete(ar.result());
        else
            fail(ar.cause());
    }

    /**
     * @return an handler completing this future
     */
    default Handler<AsyncResult<T>> completer() {
        return this::complete;
    }

    /**
     * Set the failure. Any handler will be called, if there is one
     */
    void fail(Throwable throwable);

    void fail(String failureMessage);

    /**
     * Compose this future with a provided {@code next} future.<p>
     *
     * When this (the one on which {@code compose} is called) future succeeds, the {@code handler} will be called with
     * the completed value, this handler should complete the next future.<p>
     *
     * If the {@code handler} throws an exception, the returned future will be failed with this exception.<p>
     *
     * When this future fails, the failure will be propagated to the {@code next} future and the {@code handler}
     * will not be called.
     *
     * @param handler the handler
     * @param next the next future
     * @return the next future, used for chaining
     */
    default <U> Future<U> compose(Handler<T> handler, Future<U> next) {
        setHandler(ar -> {
            if (ar.succeeded()) {
                try {
                    handler.handle(ar.result());
                } catch (Throwable err) {
                    if (next.isComplete())
                        throw err;
                    next.fail(err);
                }
            } else
                next.fail(ar.cause());
        });
        return next;
    }

    default <U> Future<U> compose(BiConsumer<T, Future<U>> handler) {
        Future<U> finalFuture = future();
        return compose(t -> handler.accept(t, finalFuture), finalFuture);
    }

    /**
     * Compose this future with a {@code mapper} function.<p>
     *
     * When this future (the one on which {@code compose} is called) succeeds, the {@code mapper} will be called with
     * the completed value and this mapper returns another future object. This returned future completion will complete
     * the future returned by this method call.<p>
     *
     * If the {@code mapper} throws an exception, the returned future will be failed with this exception.<p>
     *
     * When this future fails, the failure will be propagated to the returned future and the {@code mapper}
     * will not be called.
     *
     * @param mapper the mapper function
     * @return the composed future
     */
    default <U> Future<U> compose(Function<T, Future<U>> mapper) {
        Future<U> ret = Future.future();
        setHandler(ar -> {
            if (ar.succeeded()) {
                Future<U> apply;
                try {
                    apply = mapper.apply(ar.result());
                } catch (Throwable e) {
                    // temporary tracing the exception while exception handling mechanism is not finished
                    Logger.getGlobal().log(Level.SEVERE, "Exception raised in Future.compose()", e);
                    ret.fail(e);
                    return;
                }
                apply.setHandler(ret.completer());
            } else
                ret.fail(ar.cause());
        });
        return ret;
    }

    /**
     * Apply a {@code mapper} function on this future.<p>
     *
     * When this future succeeds, the {@code mapper} will be called with the completed value and this mapper
     * returns a value. This value will complete the future returned by this method call.<p>
     *
     * If the {@code mapper} throws an exception, the returned future will be failed with this exception.<p>
     *
     * When this future fails, the failure will be propagated to the returned future and the {@code mapper}
     * will not be called.
     *
     * @param mapper the mapper function
     * @return the mapped future
     */
    default <U> Future<U> map(Function<T, U> mapper) {
        Future<U> ret = Future.future();
        setHandler(ar -> {
            if (ar.succeeded()) {
                U mapped;
                try {
                    mapped = mapper.apply(ar.result());
                } catch (Throwable e) {
                    // temporary tracing the exception while exception handling mechanism is not finished
                    Logger.getGlobal().log(Level.SEVERE, "Exception raised in Future.map()", e);
                    ret.fail(e);
                    return;
                }
                ret.complete(mapped);
            } else
                ret.fail(ar.cause());
        });
        return ret;
    }

    default <V> Future<V> map(Callable<V> mapper) {
        Future<V> ret = Future.future();
        setHandler(ar -> {
            if (ar.succeeded())
                ret.complete(mapper.call());
            else
                ret.fail(ar.cause());
        });
        return ret;
    }

    /**
     * Map the result of a future to a specific {@code value}.<p>
     *
     * When this future succeeds, this {@code value} will complete the future returned by this method call.<p>
     *
     * When this future fails, the failure will be propagated to the returned future.
     *
     * @param value the value that eventually completes the mapped future
     * @return the mapped future
     */
    default <V> Future<V> map(V value) {
        Future<V> ret = Future.future();
        setHandler(ar -> {
            if (ar.succeeded())
                ret.complete(value);
            else
                ret.fail(ar.cause());
        });
        return ret;
    }

    static Future<Void> allOf(Future... futures) {
        Future<Void> future = Future.future();
        Unit<Integer> latch = new Unit<>(futures.length);
        Handler<AsyncResult> latchHandler = asyncResult -> {
            if (asyncResult.failed())
                future.fail(asyncResult.cause());
            else synchronized (latch) {
                Integer count;
                latch.set(count = latch.get() - 1);
                if (count == 0)
                    future.complete();
            }
        };
        for (Future f : futures)
            f.setHandler(latchHandler);
        return future;
    }

}
