package naga.core.rx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import rx.Observable;
import rx.Subscriber;

/**
 * @author Bruno Salmon
 */
public class ObservableValueSource {

    public static <T> Observable<T> fromObservableValue(ObservableValue<T> observableValue) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                subscriber.onNext(observableValue.getValue());

                ChangeListener<T> listener = (observableValue, prev, current) -> subscriber.onNext(current);

                observableValue.addListener(listener);

                subscriber.add(UiSubscriptions.unsubscribeInUiThread(() -> observableValue.removeListener(listener)));

            }
        });
    }

    /*public static <T> Observable<Change<T>> fromObservableValueChanges(final ObservableValue<T> fxObservable) {
        return Observable.create(new Observable.OnSubscribe<Change<T>>() {
            @Override
            public void call(final Subscriber<? super Change<T>> subscriber) {

                final ChangeListener<T> listener = (observableValue, prev, current) -> subscriber.onNext(new Change<>(prev,current));

                fxObservable.addListener(listener);

                subscriber.add(UiSubscriptions.unsubscribeInUiThread(() -> fxObservable.removeListener(listener)));

            }
        });
    }*/

}

