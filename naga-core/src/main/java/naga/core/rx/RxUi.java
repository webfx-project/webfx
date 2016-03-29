package naga.core.rx;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.GuiToolkit;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * @author Bruno Salmon
 */
public class RxUi {

    public static <T> Observable<T> observe(ObservableValue<T> observableValue) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                subscriber.onNext(observableValue.getValue());
                ChangeListener<T> listener = (observableValue, prev, current) -> subscriber.onNext(current);
                observableValue.addListener(listener);
                subscriber.add(unsubscribeInUiThread(() -> observableValue.removeListener(listener)));

            }
        });
    }

    public static <T> Observable<T> observeIf(Observable<T> observable, Observable<Boolean> conditionObservable) {
        return Observable.combineLatest(observable, conditionObservable, (value, condition) -> condition == Boolean.TRUE ? value : null);
    }

    public static <T> Observable<T> observeIf(Observable<T> observable, Property<Boolean> conditionProperty) {
        return observeIf(observable, observe(conditionProperty));
    }

    /*public static <T> Observable<Change<T>> fromObservableValueChanges(final ObservableValue<T> fxObservable) {
        return Observable.create(new Observable.OnSubscribe<Change<T>>() {
            @Override
            public void call(final Subscriber<? super Change<T>> subscriber) {
                final ChangeListener<T> listener = (observableValue, prev, current) -> subscriber.onNext(new Change<>(prev,current));
                fxObservable.addListener(listener);
                subscriber.add(unsubscribeInUiThread(() -> fxObservable.removeListener(listener)));

            }
        });
    }*/

    /**
     * Create an Subscription that always runs <code>unsubscribe</code> in the event dispatch thread.
     *
     * @param unsubscribe the action to be performed in the ui thread at un-subscription
     * @return an Subscription that always runs <code>unsubscribe</code> in the event dispatch thread.
     */
    public static Subscription unsubscribeInUiThread(Action0 unsubscribe) {
        return Subscriptions.create(() -> {
            if (GuiToolkit.isUiThread())
                unsubscribe.call();
            else {
                Scheduler.Worker worker = RxScheduler.UI_SCHEDULER.createWorker();
                worker.schedule(() -> {
                    unsubscribe.call();
                    worker.unsubscribe();
                });
            }
        });
    }

    public static void displayObservable(Observable<DisplayResult> displayResultObservable, Property<DisplayResult> displayResultProperty) {
        displayResultObservable.map(GuiToolkit.get()::transformDisplayResultForGui).observeOn(RxScheduler.UI_SCHEDULER).subscribe(getPropertySubscriber(displayResultProperty));
    }


    public static <T> Subscriber<T> getPropertySubscriber(Property<T> property) {
        return new Subscriber<T>() {

            @Override
            public final void onCompleted() {
                System.out.println("Completed!!!");
            }

            @Override
            public final void onError(Throwable e) {
                throw new OnErrorNotImplementedException(e);
            }

            @Override
            public final void onNext(T args) {
                property.setValue(args);
            }

        };
    }
}

