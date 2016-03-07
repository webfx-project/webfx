package naga.core.util.async;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

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
     * Create a FutureResult that hasn't completed yet
     */
    protected Future() {
    }

    /**
     * Create a VoidResult that has already completed
     * @param t The Throwable or null if succeeded
     */
    private Future(Throwable t) {
        if (t == null) {
            complete(null);
        } else {
            fail(t);
        }
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

    /* Rx API */

    public Observable<T> toObservable() {
        return Observable.create(OnSubscribeToObservableFuture.toObservableFuture(this));
    }

    /**
     * Converts a {@code Future} into an {@code Observable}.
     * <p>
     * <img width="640" src="https://github.com/ReactiveX/RxJava/wiki/images/rx-operators/from.Future.png" alt="">
     * <p>
     * You can convert any object that supports the {@code Future} interface into an {@code Observable} that emits
     * the return value of the {@code get} method of that object, by using this operator.
     * <p>
     * This is blocking so the {@code Subscription} returned when calling
     * {@code Observable.unsafeSubscribe(Observer)} does nothing.
     */
    static final class OnSubscribeToObservableFuture {
        private OnSubscribeToObservableFuture() {
            throw new IllegalStateException("No instances!");
        }

        /* package accessible for unit tests */static class ToObservableFuture<T> implements Observable.OnSubscribe<T> {
            final Future<? extends T> that;
            //private final long time;
            //private final TimeUnit unit;

            public ToObservableFuture(Future<? extends T> that) {
                this.that = that;
                //this.time = 0;
                //this.unit = null;
            }

            /*public ToObservableFuture(Future<? extends T> that, long time, TimeUnit unit) {
                this.that = that;
                this.time = time;
                this.unit = unit;
            }*/

            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        // If the Future is already completed, "cancel" does nothing.
                        //that.cancel(true);
                    }
                }));
                //don't block or propagate CancellationException if already unsubscribed
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                that.setHandler(asyncResult -> {
                    if (asyncResult.succeeded()) {
                        subscriber.onNext(asyncResult.result());
                        subscriber.onCompleted();
                    } else {
                        // If this Observable is unsubscribed, we will receive an CancellationException.
                        // However, CancellationException will not be passed to the final Subscriber
                        // since it's already subscribed.
                        // If the Future is canceled in other place, CancellationException will be still
                        // passed to the final Subscriber.
                        if (subscriber.isUnsubscribed()) {
                            //refuse to emit onError if already unsubscribed
                            return;
                        }
                        Exceptions.throwOrReport(asyncResult.cause(), subscriber);
                    }
                });
            }
        }

        public static <T> Observable.OnSubscribe<T> toObservableFuture(final Future<? extends T> that) {
            return new ToObservableFuture<T>(that);
        }

        /*public static <T> Observable.OnSubscribe<T> toObservableFuture(final Future<? extends T> that, long time, TimeUnit unit) {
            return new ToObservableFuture<T>(that, time, unit);
        }*/
    }
}
