package naga.fx.properties;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import naga.commons.util.function.Consumer;
import naga.commons.util.function.Func2;
import naga.commons.util.function.Function;
import naga.commons.util.function.Predicate;
import naga.fx.spi.Toolkit;

/**
 * @author Bruno Salmon
 */
public class Properties {

    public static void runNowAndOnPropertiesChange(Consumer<ObservableValue> runnable, ObservableValue... properties) {
        runnable.accept(null);
        runOnPropertiesChange(runnable, properties);
    }

    public static void runOnPropertiesChange(Consumer<ObservableValue> runnable, ObservableValue... properties) {
        for (ObservableValue property : properties)
            property.addListener((observable, oldValue, newValue) -> runnable.accept(property));
    }

    public static void runOnceOnPropertiesChange(Consumer<ObservableValue> runnable, ObservableValue... properties) {
        int n = properties.length;
        ChangeListener[] listeners = new ChangeListener[n];
        for (int i = 0; i < n; i++) {
            ObservableValue property = properties[i];
            property.addListener(listeners[i] = (observable, oldValue, newValue) -> {
                for (int j = 0; j < n; j++)
                    properties[j].removeListener(listeners[j]);
                runnable.accept(property);
            });
        }
    }

    public static <T, R> Property<R> compute(ObservableValue<? extends T> p, Function<? super T, ? extends R> function) {
        Property<R> combinedProperty = new SimpleObjectProperty<>();
        runNowAndOnPropertiesChange(arg -> combinedProperty.setValue(function.apply(p.getValue())), p);
        return combinedProperty;
    }

    public static <T1, T2, R> Property<R> combine(ObservableValue<? extends T1> p1, ObservableValue<? extends T2> p2, Func2<? super T1, ? super T2, ? extends R> combineFunction) {
        Property<R> combinedProperty = new SimpleObjectProperty<>();
        runNowAndOnPropertiesChange(arg -> combinedProperty.setValue(combineFunction.call(p1.getValue(), p2.getValue())), p1, p2);
        return combinedProperty;
    }

    public static <T> Property<T> filter(ObservableValue<T> property, Predicate<T> predicate) {
        Property<T> filteredProperty = new SimpleObjectProperty<>();
        runNowAndOnPropertiesChange(arg -> { if (predicate.test(property.getValue())) filteredProperty.setValue(property.getValue()); }, property);
        return filteredProperty;
    }

    public static <T> void consume(ObservableValue<T> property, Consumer<T> consumer) {
        runNowAndOnPropertiesChange(p -> consumer.accept(property.getValue()), property);
    }

    public static <T> void consumeInUiThread(Property<T> property, Consumer<T> consumer) {
        consume(property, arg -> Toolkit.get().scheduler().scheduleDeferred(() -> consumer.accept(arg)));
    }

    public static <T> void setIfNotBound(Property<T> property, T value) {
        if (!property.isBound())
            property.setValue(value);
    }
}