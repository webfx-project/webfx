package naga.fx.properties;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import naga.util.function.Consumer;
import naga.util.function.Func2;
import naga.util.function.Function;
import naga.util.function.Predicate;
import naga.fx.spi.Toolkit;

/**
 * @author Bruno Salmon
 */
public class Properties {

    public static Unregistrable runNowAndOnPropertiesChange(Consumer<ObservableValue> runnable, ObservableValue... properties) {
        runnable.accept(properties.length == 1 ? properties[0] : null);
        return runOnPropertiesChange(runnable, properties);
    }

    public static Unregistrable runOnPropertiesChange(Consumer<ObservableValue> runnable, ObservableValue... properties) {
        return new Unregistrable(runnable, properties);
    }

    public static <T, R> ObservableValue<R> compute(ObservableValue<? extends T> p, Function<? super T, ? extends R> function) {
        Property<R> combinedProperty = new SimpleObjectProperty<>();
        runNowAndOnPropertiesChange(arg -> combinedProperty.setValue(function.apply(p.getValue())), p);
        return combinedProperty;
    }

    public static ObservableValue<Boolean> not(ObservableValue<Boolean> p) {
        return compute(p, value -> !value);
    }

    public static <T1, T2, R> ObservableValue<R> combine(ObservableValue<? extends T1> p1, ObservableValue<? extends T2> p2, Func2<? super T1, ? super T2, ? extends R> combineFunction) {
        Property<R> combinedProperty = new SimpleObjectProperty<>();
        runNowAndOnPropertiesChange(arg -> combinedProperty.setValue(combineFunction.call(p1.getValue(), p2.getValue())), p1, p2);
        return combinedProperty;
    }

    public static <T> ObservableValue<T> filter(ObservableValue<T> property, Predicate<T> predicate) {
        Property<T> filteredProperty = new SimpleObjectProperty<>();
        runNowAndOnPropertiesChange(arg -> { if (predicate.test(property.getValue())) filteredProperty.setValue(property.getValue()); }, property);
        return filteredProperty;
    }

    public static <T> void consume(ObservableValue<T> property, Consumer<T> consumer) {
        runNowAndOnPropertiesChange(p -> consumer.accept(property.getValue()), property);
    }

    public static <T> void consumeInUiThread(ObservableValue<T> property, Consumer<T> consumer) {
        consume(property, arg -> Toolkit.get().scheduler().scheduleDeferred(() -> consumer.accept(arg)));
    }

    public static <T> void setIfNotBound(Property<T> property, T value) {
        if (!property.isBound())
            property.setValue(value);
    }
}