package dev.webfx.kit.util.properties;

import dev.webfx.platform.util.Arrays;
import dev.webfx.platform.util.Objects;
import dev.webfx.platform.util.collection.Collections;
import dev.webfx.platform.util.function.Converter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;
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

    public static <A, B extends A> void bind(ObservableList<A> list1, ObservableList<B> list2) {
        runNowAndOnListChange(c -> list1.setAll(list2), list2);
    }

    public static <A, B> void bindTransformed(ObservableList<A> aList, ObservableList<B> bList, Function<List<B>, List<A>> bToAListTransformer) {
        runNowAndOnListChange(c -> aList.setAll(bToAListTransformer.apply(bList)), bList);
    }

    public static <A, B> void bindConverted(ObservableList<A> aList, ObservableList<B> bList, Converter<B, A> bToAConverter) {
        runNowAndOnListChange(c -> setAllConverted(bList, bToAConverter, aList), bList);
    }

    public static <A, B> ObservableList<A> map(ObservableList<B> bList, Converter<B, A> bToAConverter) {
        ObservableList<A> aList = FXCollections.observableArrayList();
        bindConverted(aList, bList, bToAConverter);
        return aList;
    }

    public static <T> void runNowAndOnListChange(ListChangeListener<T> listener, ObservableList<T> list) {
        listener.onChanged(null);
        runOnListChange(listener, list);
    }

    public static <T> void runOnListChange(ListChangeListener<T> listener, ObservableList<T> list) {
        list.addListener(listener);
    }

    public static <T> void runNowAndOnListOrPropertiesChange(ListChangeListener<T> listener, ObservableList<T> list, ObservableValue... properties) {
        listener.onChanged(null);
        runOnListOrPropertiesChange(listener, list, properties);
    }

    public static <T> void runOnListOrPropertiesChange(ListChangeListener<T> listener, ObservableList<T> list, ObservableValue... properties) {
        runOnListChange(listener, list);
        FXProperties.runOnPropertiesChange(() -> listener.onChanged(null), properties);
    }

    public static <T> BooleanExpression isEmpty(ObservableList<T> list) {
        return Bindings.isEmpty(list);
    }

}