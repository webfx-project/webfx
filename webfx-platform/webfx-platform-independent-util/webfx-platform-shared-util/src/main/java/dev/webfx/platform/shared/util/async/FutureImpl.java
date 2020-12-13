package dev.webfx.platform.shared.util.async;

/**
 * Represents the result of an asynchronous operation that may, or may not, have finished yet.
 *
 * @author Bruno Salmon
 */
public class FutureImpl<T> implements Future<T> {
    private boolean failed;
    private boolean succeeded;
    private Handler<AsyncResult<T>> handler;
    private T result;
    private Throwable throwable;

    /**
     * Create a FutureResult that hasn't completed yet
     */
    protected FutureImpl() {
    }

    /**
     * Create a VoidResult that has already completed
     * @param t The Throwable or null if succeeded
     */
    FutureImpl(Throwable t) {
        if (t == null)
            complete();
        else
            fail(t);
    }

    FutureImpl(String failureMessage, boolean failed) {
        this(new NoStackTraceThrowable(failureMessage));
    }

    /**
     * Create a FutureResult that has already succeeded
     * @param result The result
     */
    FutureImpl(T result) {
        complete(result);
    }

    /**
     * The result of the operation. This will be null if the operation failed.
     */
    @Override
    public T result() {
        return result;
    }

    /**
     * An exception describing failure. This will be null if the operation succeeded.
     */
    @Override
    public Throwable cause() {
        return throwable;
    }

    /**
     * Did it succeed?
     */
    @Override
    public boolean succeeded() {
        return succeeded;
    }

    /**
     * Did it fail?
     */
    @Override
    public boolean failed() {
        return failed;
    }

    /**
     * Has it completed?
     */
    @Override
    public boolean isComplete() {
        return failed || succeeded;
    }

    /**
     * Set a handler for the result. It will get called when it's complete
     */
    @Override
    public void setHandler(Handler<AsyncResult<T>> handler) {
        this.handler = handler;
        checkCallHandler();
    }

    /**
     * Set the result. Any handler will be called, if there is one
     */
    @Override
    public void complete(T result) {
        checkComplete();
        this.result = result;
        succeeded = true;
        checkCallHandler();
    }

    @Override
    public void complete() {
        complete((T) null);
    }

    /**
     * Set the failure. Any handler will be called, if there is one
     */
    @Override
    public void fail(Throwable throwable) {
        checkComplete();
        this.throwable = throwable;
        failed = true;
        checkCallHandler();
    }

    @Override
    public void fail(String failureMessage) {
        fail(new NoStackTraceThrowable(failureMessage));
    }

    private void checkCallHandler() {
        if (handler != null && isComplete()) {
            try {
                handler.handle(this);
            } catch (Throwable t) { // Tracing any uncaught exception from the handler
                t.printStackTrace();
            }
        }
    }

    private void checkComplete() {
        if (succeeded || failed) {
            throw new IllegalStateException("Result is already complete: " + (succeeded ? "succeeded" : "failed"));
        }
    }
}
