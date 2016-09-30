package naga.commons.util.async;

import naga.commons.util.function.Consumer;

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
    static Future<Void> runAsync(Runnable runnable) {
        try {
            runnable.run();
            return succeededFuture();
        } catch (Throwable t) {
            // temporary tracing the exception while exception handling mechanism is not finished
            System.out.println("Exception raised in Future.runAsync(): " + t.getMessage());
            t.printStackTrace();
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
    static <T> Future<Void> consumeAsync(Consumer<T> consumer, T arg) {
        try {
            consumer.accept(arg);
            return succeededFuture();
        } catch (Throwable t) {
            // temporary tracing the exception while exception handling mechanism is not finished
            System.out.println("Exception raised in Future.runAsync(): " + t.getMessage());
            t.printStackTrace();
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

    /**
     * Set the failure. Any handler will be called, if there is one
     */
    void fail(Throwable throwable);

    void fail(String failureMessage);
}
