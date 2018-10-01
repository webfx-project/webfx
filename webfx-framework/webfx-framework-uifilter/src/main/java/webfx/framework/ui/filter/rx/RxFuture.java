package webfx.framework.ui.filter.rx;

import webfx.platform.shared.util.async.Future;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * @author Bruno Salmon
 */
public final class RxFuture {

    public static <T> Observable<T> from(Future<T> future) {
        return Observable.create(OnSubscribeToObservableFuture.toObservableFuture(future));
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

        /* package accessible for unit tests */static final class ToObservableFuture<T> implements Observable.OnSubscribe<T> {
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
