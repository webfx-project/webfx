package webfx.fx.properties.conversion;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.value.ObservableValue;
import webfx.util.function.Converter;

/**
 * @author Bruno Salmon
 */
public class ConvertedProperty<A, B> extends ConvertedObservableValue<A, B> implements Property<A> {

    private A dontGarbageA; // to avoid garbage collection
    private final Property<B> property;
    private final Converter<A, B> aToBConverter;

    public ConvertedProperty(Property<B> property, Converter<B, A> bToAConverter) {
        this(property, null, bToAConverter);
    }

    public ConvertedProperty(Property<B> property, Converter<A, B> aToBConverter, Converter<B, A> bToAConverter) {
        super(property, bToAConverter);
        this.property = property;
        this.aToBConverter = aToBConverter;
    }

    @Override
    public void setValue(A value) {
        dontGarbageA = value;
        property.setValue(aToBConverter.convert(value));
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
        property.bind(new ConvertedObservableValue<>((ObservableValue<A>) observable, aToBConverter));
    }

    @Override
    public void unbind() {
        property.unbind();
    }

    @Override
    public boolean isBound() {
        return property.isBound();
    }

    @Override
    public void bindBidirectional(Property<A> other) {
        property.bindBidirectional(new ConvertedProperty<>(other, bToAConverter, aToBConverter));
    }

    @Override
    public void unbindBidirectional(Property<A> other) {
        System.out.println("ConvertedProperty.unbindBidirectional() not yet implemented");
        //TODO property.unbindBidirectional(new ConvertedProperty<>(other, bToAConverter, aToBConverter));
    }

    public static ConvertedProperty<Integer, Number> numberToIntegerProperty(Property<Number> numberProperty) {
        return new ConvertedProperty<>(numberProperty, Integer::doubleValue, Number::intValue);
    }

    public static ConvertedProperty<Integer, Double> doubleToIntegerProperty(Property<Double> doubleProperty) {
        return new ConvertedProperty<>(doubleProperty, Integer::doubleValue, Double::intValue);
    }

    public static ConvertedProperty<Double, Number> numberToDoubleProperty(Property<Number> numberProperty) {
        return new ConvertedProperty<>(numberProperty, Double::doubleValue, Number::doubleValue);
    }

    public static ConvertedProperty<Double, Integer> integerToDoubleProperty(Property<Integer> numberProperty) {
        return new ConvertedProperty<>(numberProperty, Double::intValue, Integer::doubleValue);
    }

}
