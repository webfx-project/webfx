package naga.core.util.async;

import naga.core.spi.platform.Platform;
import naga.core.util.function.Consumer;

/**
 * Represents the result of an asynchronous operation that may, or may not, have finished yet.
 *
 * @author Bruno Salmon
 */
public class Future<T> implements AsyncResult<T> {
    private boolean failed;
    private boolean succeeded;
    private Handler<AsyncResult<T>> handler;
    private T result;
    private Throwable throwable;

    /**
     * Create a future that hasn't completed yet
     *
     * @param <T>  the result type
     * @return  the future
     */
    public static <T> Future<T> future() {
        return new Future<>();
    }

    /**
     * Create a succeeded future with a null result
     *
     * @param <T>  the result type
     * @return  the future
     */
    public static <T> Future<T> succeededFuture() {
        return new Future<>((T)null);
    }

    /**
     * Created a succeeded future with the specified result.
     *
     * @param result  the result
     * @param <T>  the result type
     * @return  the future
     */
    public static <T> Future<T> succeededFuture(T result) {
        return new Future<>(result);
    }

    /**
     * Create a failed future with the specified failure cause.
     *
     * @param t  the failure cause as a Throwable
     * @param <T>  the result type
     * @return  the future
     */
    public static <T> Future<T> failedFuture(Throwable t) {
        return new Future<>(t);
    }

    /**
     * Create a failed future with the specified failure message.
     *
     * @param failureMessage  the failure message
     * @param <T>  the result type
     * @return  the future
     */
    public static <T> Future<T> failedFuture(String failureMessage) {
        return new Future<>(failureMessage, true);
    }

    /**
     * Wrap a runnable into a future that complete immediately or fail if an exception is thrown.
     *
     * @param runnable  the runnable
     * @return  the future
     */
    public static Future<Void> runAsync(Runnable runnable) {
        try {
            runnable.run();
            return succeededFuture();
        } catch (Throwable t) {
            // temporary tracing the exception while exception handling mechanism is not finished
            Platform.log("Exception raised in Future.runAsync(): " + t.getMessage(), t); // temporary dependency to Platform so the trace is working with GWT
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
    public static <T> Future<Void> consumeAsync(Consumer<T> consumer, T arg) {
        try {
            consumer.accept(arg);
            return succeededFuture();
        } catch (Throwable t) {
            // temporary tracing the exception while exception handling mechanism is not finished
            Platform.log("Exception raised in Future.consumeAsync(): " + t.getMessage(), t); // temporary dependency to Platform so the trace is working with GWT
            return failedFuture(t);
        }
    }

    /**
     * Create a FutureResult that hasn't completed yet
     */
    protected Future() {
    }

    /**
     * Create a VoidResult that has already completed
     * @param t The Throwable or null if succeeded
     */
    private Future(Throwable t) {
        if (t == null)
            complete(null);
        else
            fail(t);
    }

    private Future(String failureMessage, boolean failed) {
        this(new NoStackTraceThrowable(failureMessage));
    }

    /**
     * Create a FutureResult that has already succeeded
     * @param result The result
     */
    private Future(T result) {
        complete(result);
    }

    /**
     * The result of the operation. This will be null if the operation failed.
     */
    public T result() {
        return result;
    }

    /**
     * An exception describing failure. This will be null if the operation succeeded.
     */
    public Throwable cause() {
        return throwable;
    }

    /**
     * Did it succeeed?
     */
    public boolean succeeded() {
        return succeeded;
    }

    /**
     * Did it fail?
     */
    public boolean failed() {
        return failed;
    }

    /**
     * Has it completed?
     */
    public boolean isComplete() {
        return failed || succeeded;
    }

    /**
     * Set a handler for the result. It will get called when it's complete
     */
    public void setHandler(Handler<AsyncResult<T>> handler) {
        this.handler = handler;
        checkCallHandler();
    }

    /**
     * Set the result. Any handler will be called, if there is one
     */
    public void complete(T result) {
        checkComplete();
        this.result = result;
        succeeded = true;
        checkCallHandler();
    }

    public void complete() {
        complete(null);
    }

    /**
     * Set the failure. Any handler will be called, if there is one
     */
    public void fail(Throwable throwable) {
        checkComplete();
        this.throwable = throwable;
        failed = true;
        checkCallHandler();
    }

    public void fail(String failureMessage) {
        fail(new NoStackTraceThrowable(failureMessage));
    }

    private void checkCallHandler() {
        if (handler != null && isComplete()) {
            handler.handle(this);
        }
    }

    private void checkComplete() {
        if (succeeded || failed) {
            throw new IllegalStateException("Result is already complete: " + (succeeded ? "succeeded" : "failed"));
        }
    }
}
