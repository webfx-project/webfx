package naga.core.spi.toolkit.property;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import naga.core.spi.platform.Platform;
import naga.core.util.function.Converter;

/**
 * @author Bruno Salmon
 */
public class MappedProperty<A, B> implements Property<A> {

    private A dontGarbageA; // to avoid garbage collection
    private final Property<B> property;
    private final Converter<A,B> aToBConverter;
    private final Converter<B,A> bToAConverter;
    private ObservableValue<? extends A> bindObservable;
    private ChangeListener<A> bindListener;

    public MappedProperty(Property<B> property, Converter<A, B> aToBConverter, Converter<B, A> bToAConverter) {
        this.property = property;
        this.aToBConverter = aToBConverter;
        this.bToAConverter = bToAConverter;
    }

    @Override
    public void setValue(A value) {
        dontGarbageA = value;
        property.setValue(aToBConverter.convert(value));
    }

    @Override
    public A getValue() {
        return bToAConverter.convert(property.getValue());
    }

    @Override
    public Object getBean() {
        return property.getBean();
    }

    @Override
    public String getName() {
        return property.getName();
    }

    @Override
    public void bind(ObservableValue<? extends A> observable) {
        unbind();
        setValue(observable.getValue());
        observable.addListener(bindListener = (observable1, oldValue, newValue) -> setValue(newValue));
    }

    @Override
    public void unbind() {
        if (bindObservable != null) {
            bindObservable.removeListener(bindListener);
            bindListener = null;
            bindObservable = null;
        }
    }

    @Override
    public boolean isBound() {
        return bindObservable != null;
    }

    @Override
    public void bindBidirectional(Property<A> other) {
        property.bindBidirectional(new MappedProperty<>(other, bToAConverter, aToBConverter));
    }

    @Override
    public void unbindBidirectional(Property<A> other) {
        Platform.log("MappedProperty.unbindBidirectional() not yet implemented");
        //TODO property.unbindBidirectional(new MappedProperty<>(other, bToAConverter, aToBConverter));
    }

    @Override
    public void addListener(ChangeListener<? super A> listener) {
        property.addListener((observable, oldValue, newValue) -> {
            listener.changed(MappedProperty.this, bToAConverter.convert(oldValue), bToAConverter.convert(newValue));
        });
    }

    @Override
    public void removeListener(ChangeListener<? super A> listener) {
        Platform.log("MappedProperty.removeListener() not yet implemented");
    }

    @Override
    public void addListener(InvalidationListener listener) {
        property.addListener(observable -> {
            listener.invalidated(MappedProperty.this);
        });
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        Platform.log("MappedProperty.removeListener() not yet implemented");
    }
}
