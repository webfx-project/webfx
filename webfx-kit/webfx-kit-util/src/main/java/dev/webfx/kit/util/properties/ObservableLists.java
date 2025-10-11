package dev.webfx.kit.util.properties;

import dev.webfx.platform.util.Arrays;
import dev.webfx.platform.util.Objects;
import dev.webfx.platform.util.collection.Collections;
import dev.webfx.platform.util.function.Converter;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public final class ObservableLists {

    public static <T> void setAllNonNulls(ObservableList<T> list, T... elements) {
        if (elements != null) {
            if (Arrays.allNonNulls(elements))
                list.setAll(elements);
            else
                list.setAll(Arrays.nonNullsAsList(elements));
        }
    }

    public static <T> void setAllNonNulls(ObservableList<T> list, List<? extends T> elements) {
        if (elements != null) {
            if (Collections.allNonNulls(elements))
                list.setAll(elements);
            else
                //Doesn't work on Android: list.setAll(elements.stream().filter(Objects::nonNull).collect(Collectors.toList()));
                list.setAll(Collections.filter(elements, Objects::nonNull));
        }
    }

    public static <A, B> void setAllConverted(List<A> aList, Converter<A, B> aToBConverter, ObservableList<B> bList) {
        // GWT/J2OBJC bList.setAll(aList.stream().map(aToBConverter::convert).collect(Collectors.toList()));
        bList.setAll(Collections.map(aList, aToBConverter));
    }

    public static <A, B> void setAllNonNullsConverted(List<A> aList, Converter<A, B> aToBConverter, ObservableList<B> bList) {
        // GWT/J2OBJC bList.setAll(aList.stream().map(aToBConverter::convert).collect(Collectors.toList()));
        setAllNonNulls(bList, Collections.map(aList, aToBConverter));
    }

    public static <A, B extends A> Unregisterable bind(ObservableList<A> list1, ObservableList<B> list2) {
        return runNowAndOnListChange(c -> list1.setAll(list2), list2);
    }

    public static <A, B> Unregisterable bindTransformed(ObservableList<A> aList, ObservableList<B> bList, Function<List<B>, List<A>> bToAListTransformer) {
        return runNowAndOnListChange(c -> aList.setAll(bToAListTransformer.apply(bList)), bList);
    }

    public static <A, B> Unregisterable bindConverted(ObservableList<A> aList, ObservableList<B> bList, Converter<B, A> bToAConverter) {
        return runNowAndOnListChange(c -> setAllConverted(bList, bToAConverter, aList), bList);
    }

    public static <A, B> ObservableList<A> map(ObservableList<B> bList, Converter<B, A> bToAConverter) {
        ObservableList<A> aList = FXCollections.observableArrayList();
        bindConverted(aList, bList, bToAConverter);
        return aList;
    }

    public static <T> Unregisterable runOnListChange(ListChangeListener<T> listener, ObservableList<T> list) {
        list.addListener(listener);
        return new Unregisterable() {
            @Override
            public void register() {
                list.addListener(listener);
            }

            @Override
            public void unregister() {
                list.removeListener(listener);
            }
        };
    }

    public static <T> Unregisterable runOnListChange(Runnable listener, ObservableList<T> list) {
        return runOnListChange(change -> listener.run(), list);
    }

    public static <T> Unregisterable runNowAndOnListChange(ListChangeListener<T> listener, ObservableList<T> list) {
        listener.onChanged(null);
        return runOnListChange(listener, list);
    }

    public static <T> void runNowAndOnListOrPropertiesChange(ListChangeListener<T> listener, ObservableList<T> list, ObservableValue... properties) {
        listener.onChanged(null);
        runOnListOrPropertiesChange(listener, list, properties);
    }

    public static <T> void runOnListOrPropertiesChange(ListChangeListener<T> listener, ObservableList<T> list, ObservableValue... properties) {
        runOnListChange(listener, list);
        FXProperties.runOnPropertiesChange(() -> listener.onChanged(null), properties);
    }

    public static BooleanBinding isEmpty(ObservableList<?> list) {
        return Bindings.isEmpty(list);
    }

    public static BooleanBinding isNotEmpty(ObservableList<?> list) {
        return Bindings.isNotEmpty(list);
    }

    public static IntegerBinding size(ObservableList<?> list) {
        return Bindings.size(list);
    }

    public static ReadOnlyIntegerProperty versionNumber(ObservableList<?> list) {
        IntegerProperty versionNumber = new SimpleIntegerProperty();
        runOnListChange(c -> versionNumber.set(versionNumber.get() + 1), list);
        return versionNumber;
    }

    public static <T> ObservableList<T> newObservableList(Consumer<ObservableList<T>> onInvalidated) {
        ObservableList<T> list = FXCollections.observableArrayList();
        list.addListener((InvalidationListener) observable -> {
            if (onInvalidated != null)
                onInvalidated.accept(list);
        });
        return list;
    }

    public static <T> ObservableList<T> newObservableList(Runnable onInvalidated) {
        return newObservableList(list -> onInvalidated.run());
    }

    public static <T> ObservableList<T> newObservableListWithListener(ListChangeListener<? super T> changeListener) {
        ObservableList<T> list = FXCollections.observableArrayList();
        list.addListener(changeListener);
        return list;
    }

}