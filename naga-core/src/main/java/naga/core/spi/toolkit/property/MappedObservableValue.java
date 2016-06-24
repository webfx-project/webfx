package naga.core.spi.toolkit.property;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import naga.core.spi.platform.Platform;
import naga.core.util.function.Converter;

/**
 * @author Bruno Salmon
 */
public class MappedObservableValue<A, B> implements ObservableValue<A> {

    protected final ObservableValue<B> observableValue;
    protected final Converter<B,A> bToAConverter;

    public MappedObservableValue(ObservableValue<B> observableValue, Converter<B, A> bToAConverter) {
        this.observableValue = observableValue;
        this.bToAConverter = bToAConverter;
    }

    @Override
    public void addListener(ChangeListener<? super A> listener) {
        observableValue.addListener((observable, oldValue, newValue) -> {
            listener.changed(MappedObservableValue.this, bToAConverter.convert(oldValue), bToAConverter.convert(newValue));
        });
    }

    @Override
    public void removeListener(ChangeListener<? super A> listener) {
        Platform.log("MappedProperty.removeListener() not yet implemented");
    }

    @Override
    public A getValue() {
        return bToAConverter.convert(observableValue.getValue());
    }


    @Override
    public void addListener(InvalidationListener listener) {
        observableValue.addListener(observable -> {
            listener.invalidated(MappedObservableValue.this);
        });
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        Platform.log("MappedProperty.removeListener() not yet implemented");
    }
}
