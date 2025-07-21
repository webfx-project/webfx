package dev.webfx.kit.util.properties;

import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.util.collection.Collections;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
public final class FXProperties {

    public static <T> Unregisterable runOnPropertyChange(ChangeListener<? super T> listener, ObservableValue<T> property) {
        return new UnregisterableListener(listener, property);
    }

    public static <T> Unregisterable runOrUnregisterOnPropertyChange(UnregisterableChangeListener<T> unregisterableChangeListener, ObservableValue<T> property) {
        UnregisterableListener[] thisListener = new UnregisterableListener[1];
        thisListener[0] = new UnregisterableListener((o, oldValue, newValue) -> unregisterableChangeListener.changed(thisListener[0], (T) oldValue, (T) newValue), property);
        return thisListener[0];
    }

    public static <T> Unregisterable runOnPropertyChange(Consumer<? super T> newValueListener, ObservableValue<T> property) {
        return runOnPropertyChange((o, oldValue, newValue) -> newValueListener.accept(newValue), property);
    }

    public static <T> Unregisterable runOnPropertyChange(Runnable listener, ObservableValue<T> property) {
        return runOnPropertyChange(p -> listener.run(), property);
    }

    public static Unregisterable runOnDoublePropertyChange(Consumer<Double> newValueListener, ObservableValue<Number> property) {
        return runOnPropertyChange(n -> newValueListener.accept(n.doubleValue()), property);
    }

    public static Unregisterable runOnIntegerPropertyChange(Consumer<Integer> newValueListener, ObservableValue<Number> property) {
        return runOnPropertyChange(n -> newValueListener.accept(n.intValue()), property);
    }

    public static <T> Unregisterable runNowAndOnPropertyChange(ChangeListener<? super T> listener, ObservableValue<T> property) {
        listener.changed(property, property.getValue(), property.getValue());
        return runOnPropertyChange(listener, property);
    }

    public static <T> Unregisterable runNowAndOnPropertyChange(Consumer<? super T> newValueListener, ObservableValue<T> property) {
        return runNowAndOnPropertyChange((o, oldValue, newValue) -> newValueListener.accept(newValue), property);
    }

    public static <T> Unregisterable runNowAndOnPropertyChange(Runnable listener, ObservableValue<T> property) {
        return runNowAndOnPropertyChange(p -> listener.run(), property);
    }

    public static Unregisterable runNowAndOnDoublePropertyChange(Consumer<Double> newValueListener, ObservableValue<Number> property) {
        return runNowAndOnPropertyChange(n -> newValueListener.accept(n.doubleValue()), property);
    }

    public static Unregisterable runOnPropertiesChange(Consumer<ObservableValue> consumer, ObservableValue... properties) {
        return new UnregisterableListener(consumer, properties);
    }

    public static Unregisterable runNowAndOnPropertiesChange(Consumer<ObservableValue> consumer, ObservableValue... properties) {
        consumer.accept(properties.length == 1 ? properties[0] : null);
        return runOnPropertiesChange(consumer, properties);
    }

    // Same API but with Collection instead of varargs

    public static Unregisterable runNowAndOnPropertiesChange(Consumer<ObservableValue> consumer, Collection<ObservableValue> properties) {
        return runNowAndOnPropertiesChange(consumer, Collections.toArray(properties, ObservableValue[]::new));
    }

    public static Unregisterable runOnPropertiesChange(Consumer<ObservableValue> consumer, Collection<ObservableValue> properties) {
        return runOnPropertiesChange(consumer, Collections.toArray(properties, ObservableValue[]::new));
    }

    // Same API but with Runnable instead of Consumer

    public static Unregisterable runOnPropertiesChange(Runnable runnable, ObservableValue... properties) {
        return runOnPropertiesChange(p -> runnable.run(), properties);
    }

    public static Unregisterable runNowAndOnPropertiesChange(Runnable runnable, ObservableValue... properties) {
        return runNowAndOnPropertiesChange(p -> runnable.run(), properties);
    }

    public static Unregisterable runNowAndOnPropertiesChange(Runnable runnable, Collection<ObservableValue> properties) {
        return runNowAndOnPropertiesChange(p -> runnable.run(), properties);
    }

    public static Unregisterable runOnPropertiesChange(Runnable runnable, Collection<ObservableValue> properties) {
        return runOnPropertiesChange(p -> runnable.run(), properties);
    }

    public static <T> ObservableValue<T> deferredProperty(ObservableValue<T> p) {
        Property<T> dp = new SimpleObjectProperty<>();
        dp.setValue(p.getValue());
        FXProperties.runOnPropertyChange(() -> UiScheduler.scheduleDeferred(() -> dp.setValue(p.getValue())), p);
        return dp;
    }

    public static <T, R> ObservableValue<R> compute(ObservableValue<? extends T> p, Function<? super T, ? extends R> function) {
        Property<R> combinedProperty = new SimpleObjectProperty<>();
        runNowAndOnPropertiesChange(o -> combinedProperty.setValue(function.apply(p.getValue())), p);
        return combinedProperty;
    }

    public static <T1, T2, R> ObservableValue<R> combine(ObservableValue<? extends T1> p1, ObservableValue<? extends T2> p2, BiFunction<? super T1, ? super T2, ? extends R> combineFunction) {
        Property<R> combinedProperty = new SimpleObjectProperty<>();
        runNowAndOnPropertiesChange(arg -> combinedProperty.setValue(combineFunction.apply(p1.getValue(), p2.getValue())), p1, p2);
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

    public static <T> void setIfNotBound(Property<T> property, T value) {
        if (!property.isBound())
            property.setValue(value);
    }

    public static <T> void setEvenIfBound(Property<T> property, T value) {
        if (property.isBound())
            property.unbind();
        property.setValue(value);
    }

    public static <T> void setIfNotEquals(Property<T> property, T value) {
        if (!Objects.equals(value, property.getValue()))
            property.setValue(value);
    }

    public static <T> void onPropertySet(ObservableValue<T> property, Consumer<T> valueConsumer) {
        onPropertySet(property, valueConsumer, false);
    }

    public static <T> void onPropertySet(ObservableValue<T> property, Consumer<T> valueConsumer, boolean callIfNullProperty) {
        onPropertyChecks(property, Objects::nonNull, valueConsumer, callIfNullProperty);
    }

    public static <T> void onPropertyEquals(ObservableValue<T> property, T value, Consumer<T> valueConsumer) {
        onPropertyChecks(property, v -> Objects.equals(v, value), valueConsumer, false);
    }

    public static <T> void onPropertyChecks(ObservableValue<T> property, Predicate<T> predicate, Consumer<T> valueConsumer) {
        onPropertyChecks(property, predicate, valueConsumer, false);
    }

    public static <T> void onPropertyChecks(ObservableValue<T> property, Predicate<T> predicate, Consumer<T> valueConsumer, boolean callIfNullProperty) {
        if (property == null) {
            if (callIfNullProperty)
                valueConsumer.accept(null);
        } else {
            T value = property.getValue();
            if (predicate.test(value))
                valueConsumer.accept(value);
            else
                runOrUnregisterOnPropertyChange((thisListener, oldValue, newValue) -> {
                    if (predicate.test(newValue)) {
                        thisListener.unregister();
                        valueConsumer.accept(newValue);
                    }
                }, property);
        }
    }

    public static <A, B> void bindConverted(Property<A> pA, ObservableValue<B> pB, Function<B, A> baConverter) {
        pA.bind(pB.map(baConverter));
    }

    public static <A, B> void bindConvertedBidirectional(Property<A> pA, Property<B> pB, Function<B, A> baConverter, Function<A, B> abConverter) {
        boolean[] converting = { false };
        pB.addListener((observable, oldValue, newValue) -> {
            if (!converting[0]) {
                converting[0] = true;
                try {
                    pA.setValue(baConverter.apply(newValue));
                } finally {
                    converting[0] = false;
                }
            }
        });
        pA.addListener((observable, oldValue, newValue) -> {
            if (!converting[0]) {
                converting[0] = true;
                try {
                    pB.setValue(abConverter.apply(newValue));
                } finally {
                    converting[0] = false;
                }
            }
        });
    }

    public static BooleanProperty newBooleanProperty(boolean initialValue, Consumer<Boolean> onInvalidated) {
        return new SimpleBooleanProperty(initialValue) {
            @Override
            protected void invalidated() {
                if (onInvalidated != null)
                    onInvalidated.accept(get());
            }
        };
    }

    public static BooleanProperty newBooleanProperty(boolean initialValue, Runnable onInvalidated) {
        return newBooleanProperty(initialValue, e -> onInvalidated.run());
    }

    public static BooleanProperty newBooleanProperty(Consumer<Boolean> onInvalidated) {
        return newBooleanProperty(false, onInvalidated);
    }

    public static BooleanProperty newBooleanProperty(Runnable onInvalidated) {
        return newBooleanProperty(false, onInvalidated);
    }

    public static IntegerProperty newIntegerProperty(int initialValue, Consumer<Integer> onInvalidated) {
        return new SimpleIntegerProperty(initialValue) {
            @Override
            protected void invalidated() {
                if (onInvalidated != null)
                    onInvalidated.accept(get());
            }
        };
    }

    public static IntegerProperty newIntegerProperty(int initialValue, Runnable onInvalidated) {
        return newIntegerProperty(initialValue, e -> onInvalidated.run());
    }

    public static IntegerProperty newIntegerProperty(Consumer<Integer> onInvalidated) {
        return newIntegerProperty(0, onInvalidated);
    }

    public static IntegerProperty newIntegerProperty(Runnable onInvalidated) {
        return newIntegerProperty(0, onInvalidated);
    }

    public static <T> ObjectProperty<T> newObjectProperty(T initialValue, Consumer<T> onInvalidated) {
        return new SimpleObjectProperty<>(initialValue) {
            @Override
            protected void invalidated() {
                if (onInvalidated != null)
                    onInvalidated.accept(get());
            }
        };
    }

    public static DoubleProperty newDoubleProperty(double initialValue, Consumer<Double> onInvalidated) {
        return new SimpleDoubleProperty(initialValue) {
            @Override
            protected void invalidated() {
                if (onInvalidated != null)
                    onInvalidated.accept(get());
            }
        };
    }

    public static DoubleProperty newDoubleProperty(double initialValue, Runnable onInvalidated) {
        return newDoubleProperty(initialValue, e -> onInvalidated.run());
    }

    public static DoubleProperty newDoubleProperty(Consumer<Double> onInvalidated) {
        return newDoubleProperty(0, onInvalidated);
    }

    public static DoubleProperty newDoubleProperty(Runnable onInvalidated) {
        return newDoubleProperty(0, onInvalidated);
    }

    public static <T> ObjectProperty<T> newObjectProperty(T initialValue, Runnable onInvalidated) {
        return newObjectProperty(initialValue, e -> onInvalidated.run());
    }

    public static <T> ObjectProperty<T> newObjectProperty(Consumer<T> onInvalidated) {
        return newObjectProperty(null, onInvalidated);
    }

    public static <T> ObjectProperty<T> newObjectProperty(Runnable onInvalidated) {
        return newObjectProperty(null, onInvalidated);
    }

    public static void toggleProperty(WritableValue<Boolean> p) {
        p.setValue(!p.getValue());
    }

    public static BooleanExpression booleanExpression(ObservableValue<Boolean> booleanProperty) {
        return BooleanBinding.booleanExpression(booleanProperty);
    }

    public static BooleanBinding not(ObservableValue<Boolean> booleanProperty) {
        return booleanExpression(booleanProperty).not();
    }

}