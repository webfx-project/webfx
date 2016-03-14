package naga.core.rx;

import javafx.beans.value.ObservableValue;
import rx.Observable;

/**
 * @author Bruno Salmon
 */
public class PropertySource {

    public static <T> Observable<T> fromObervableValue(ObservableValue<T> observableValue) {
        return ObservableValueSource.fromObservableValue(observableValue);
    }
}
