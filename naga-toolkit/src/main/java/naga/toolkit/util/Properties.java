package naga.toolkit.util;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.commons.util.function.Consumer;
import naga.commons.util.function.Func2;
import naga.commons.util.function.Predicate;
import naga.toolkit.spi.Toolkit;

/**
 * @author Bruno Salmon
 */
public class Properties {

    public static void runNowAndOnPropertiesChange(Consumer<Property> runnable, Property... properties) {
        runnable.accept(null);
        runOnPropertiesChange(runnable, properties);
    }

    public static void runOnPropertiesChange(Consumer<Property> runnable, Property... properties) {
        for (Property property : properties)
            property.addListener((observable, oldValue, newValue) -> runnable.accept(property));
    }

    public static <T1, T2, R> Property<R> combine(Property<? extends T1> p1, Property<? extends T2> p2, Func2<? super T1, ? super T2, ? extends R> combineFunction) {
        Property<R> combinedProperty = new SimpleObjectProperty<>();
        runNowAndOnPropertiesChange(arg -> combinedProperty.setValue(combineFunction.call(p1.getValue(), p2.getValue())), p1, p2);
        return combinedProperty;
    }

    public static <T> Property<T> filter(Property<T> property, Predicate<T> predicate) {
        Property<T> filteredProperty = new SimpleObjectProperty<>();
        runNowAndOnPropertiesChange(arg -> { if (predicate.test(property.getValue())) filteredProperty.setValue(property.getValue()); }, property);
        return filteredProperty;
    }

    public static <T> void consume(Property<T> property, Consumer<T> consumer) {
        runNowAndOnPropertiesChange(p -> consumer.accept(property.getValue()), property);
    }

    public static <T> void consumeInUiThread(Property<T> property, Consumer<T> consumer) {
        consume(property, arg -> Toolkit.get().scheduler().scheduleDeferred(() -> consumer.accept(arg)));
    }
}