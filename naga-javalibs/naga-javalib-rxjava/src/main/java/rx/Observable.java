package rx;

import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action1;
import rx.internal.util.ScalarSynchronousObservable;
import rx.plugins.RxJavaObservableExecutionHook;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.Subscriptions;

/**
 * @author Bruno Salmon
 */
public class Observable<T> {

    final OnSubscribe<T> onSubscribe;

    /**
     * Creates an Observable with a Function to execute when it is subscribed to.
     * <p>
     * <em>Note:</em> Use {@link #create(OnSubscribe)} to create an Observable, instead of this constructor,
     * unless you specifically have a need for inheritance.
     *
     * @param f
     *            {@link OnSubscribe} to be executed when {@link #subscribe(Subscriber)} is called
     */
    protected Observable(OnSubscribe<T> f) {
        this.onSubscribe = f;
    }

    static final RxJavaObservableExecutionHook hook = RxJavaPlugins.getInstance().getObservableExecutionHook();

    /**
     * Returns an Observable that will execute the specified function when a {@link Subscriber} subscribes to
     * it.
     * <p>
     * <img width="640" height="200" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/create.png" alt="">
     * <p>
     * Write the function you pass to {@code create} so that it behaves as an Observable: It should invoke the
     * Subscriber's {@link Subscriber#onNext onNext}, {@link Subscriber#onError onError}, and
     * {@link Subscriber#onCompleted onCompleted} methods appropriately.
     * <p>
     * A well-formed Observable must invoke either the Subscriber's {@code onCompleted} method exactly once or
     * its {@code onError} method exactly once.
     * <p>
     * See <a href="http://go.microsoft.com/fwlink/?LinkID=205219">Rx Design Guidelines (PDF)</a> for detailed
     * information.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param <T>
     *            the type of the items that this Observable emits
     * @param f
     *            a function that accepts an {@code Subscriber<T>}, and invokes its {@code onNext},
     *            {@code onError}, and {@code onCompleted} methods as appropriate
     * @return an Observable that, when a {@link Subscriber} subscribes to it, will execute the specified
     *         function
     * @see <a href="http://reactivex.io/documentation/operators/create.html">ReactiveX operators documentation: Create</a>
     */
    public static <T> Observable<T> create(OnSubscribe<T> f) {
        return new Observable<T>(hook.onCreate(f));
    }


    /**
     * Subscribes to an Observable and provides a Subscriber that implements functions to handle the items the
     * Observable emits and any error or completion notification it issues.
     * <p>
     * A typical implementation of {@code subscribe} does the following:
     * <ol>
     * <li>It stores a reference to the Subscriber in a collection object, such as a {@code List<T>} object.</li>
     * <li>It returns a reference to the {@link Subscription} interface. This enables Subscribers to
     * unsubscribe, that is, to stop receiving items and notifications before the Observable completes, which
     * also invokes the Subscriber's {@link Subscriber#onCompleted onCompleted} method.</li>
     * </ol><p>
     * An {@code Observable<T>} instance is responsible for accepting all subscriptions and notifying all
     * Subscribers. Unless the documentation for a particular {@code Observable<T>} implementation indicates
     * otherwise, Subscriber should make no assumptions about the order in which multiple Subscribers will
     * receive their notifications.
     * <p>
     * For more information see the
     * <a href="http://reactivex.io/documentation/observable.html">ReactiveX documentation</a>.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code subscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param subscriber
     *            the {@link Subscriber} that will handle emissions and notifications from the Observable
     * @return a {@link Subscription} reference with which Subscribers that are {@link Observer}s can
     *         unsubscribe from the Observable
     * @throws IllegalStateException
     *             if {@code subscribe} is unable to obtain an {@code OnSubscribe<>} function
     * @throws IllegalArgumentException
     *             if the {@link Subscriber} provided as the argument to {@code subscribe} is {@code null}
     * @throws OnErrorNotImplementedException
     *             if the {@link Subscriber}'s {@code onError} method is null
     * @throws RuntimeException
     *             if the {@link Subscriber}'s {@code onError} method itself threw a {@code Throwable}
     * @see <a href="http://reactivex.io/documentation/operators/subscribe.html">ReactiveX operators documentation: Subscribe</a>
     */
    public final Subscription subscribe(Subscriber<? super T> subscriber) {
        return Observable.subscribe(subscriber, this);
    }

    private static <T> Subscription subscribe(Subscriber<? super T> subscriber, Observable<T> observable) {
        // validate and proceed
        if (subscriber == null) {
            throw new IllegalArgumentException("observer can not be null");
        }
        if (observable.onSubscribe == null) {
            throw new IllegalStateException("onSubscribe function can not be null.");
            /*
             * the subscribe function can also be overridden but generally that's not the appropriate approach
             * so I won't mention that in the exception
             */
        }

        // new Subscriber so onStart it
        subscriber.onStart();

        /*
         * See https://github.com/ReactiveX/RxJava/issues/216 for discussion on "Guideline 6.4: Protect calls
         * to user code from within an Observer"
         */
        // if not already wrapped
        /* NAGA if (!(subscriber instanceof SafeSubscriber)) {
            // assign to `observer` so we return the protected version
            subscriber = new SafeSubscriber<T>(subscriber);
        }*/

        // The code below is exactly the same an unsafeSubscribe but not used because it would
        // add a significant depth to already huge call stacks.
        try {
            // allow the hook to intercept and/or decorate
            hook.onSubscribeStart(observable, observable.onSubscribe).call(subscriber);
            return hook.onSubscribeReturn(subscriber);
        } catch (Throwable e) {
            // special handling for certain Throwable/Error/Exception types
            Exceptions.throwIfFatal(e);
            // if an unhandled error occurs executing the onSubscribe we will propagate it
            try {
                subscriber.onError(hook.onSubscribeError(e));
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                // if this happens it means the onError itself failed (perhaps an invalid function implementation)
                // so we are unable to propagate the error correctly and will just throw
                RuntimeException r = new RuntimeException("Error occurred attempting to subscribe [" + e.getMessage() + "] and then again while trying to pass to onError.", e2);
                // TODO could the hook be the cause of the error in the on error handling.
                hook.onSubscribeError(r);
                // TODO why aren't we throwing the hook's return value.
                throw r;
            }
            return Subscriptions.unsubscribed();
        }
    }

    /**
     * Subscribes to an Observable and provides a callback to handle the items it emits.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code subscribe} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param onNext
     *             the {@code Action1<T>} you have designed to accept emissions from the Observable
     * @return a {@link Subscription} reference with which the {@link Observer} can stop receiving items before
     *         the Observable has finished sending them
     * @throws IllegalArgumentException
     *             if {@code onNext} is null
     * @throws OnErrorNotImplementedException
     *             if the Observable calls {@code onError}
     * @see <a href="http://reactivex.io/documentation/operators/subscribe.html">ReactiveX operators documentation: Subscribe</a>
     */
    public final Subscription subscribe(final Action1<? super T> onNext) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        }

        return subscribe(new Subscriber<T>() {

            @Override
            public final void onCompleted() {
                // do nothing
            }

            @Override
            public final void onError(Throwable e) {
                throw new OnErrorNotImplementedException(e);
            }

            @Override
            public final void onNext(T args) {
                onNext.call(args);
            }

        });
    }


    /**
     * Returns an Observable that emits a single item and then completes.
     * <p>
     * <img width="640" height="310" src="https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/just.png" alt="">
     * <p>
     * To convert any object into an Observable that emits that object, pass that object into the {@code just}
     * method.
     * <p>
     * This is similar to the {@link #from(java.lang.Object[])} method, except that {@code from} will convert
     * an {@link Iterable} object into an Observable that emits each of the items in the Iterable, one at a
     * time, while the {@code just} method converts an Iterable into an Observable that emits the entire
     * Iterable as a single item.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code just} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param value
     *            the item to emit
     * @param <T>
     *            the type of that item
     * @return an Observable that emits {@code value} as a single item and then completes
     * @see <a href="http://reactivex.io/documentation/operators/just.html">ReactiveX operators documentation: Just</a>
     */
    public static <T> Observable<T> just(final T value) {
        return ScalarSynchronousObservable.create(value);
    }

    /**
     * Invoked when Observable.subscribe is called.
     */
    public interface OnSubscribe<T> extends Action1<Subscriber<? super T>> {
        // cover for generics insanity
    }

}
