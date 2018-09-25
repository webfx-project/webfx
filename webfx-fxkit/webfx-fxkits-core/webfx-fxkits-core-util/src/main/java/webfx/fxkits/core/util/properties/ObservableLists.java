package webfx.fxkits.core.util.properties;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import webfx.platforms.core.util.Arrays;
import webfx.platforms.core.util.Objects;
import webfx.platforms.core.util.collection.Collections;
import webfx.platforms.core.util.function.Converter;

import java.util.List;

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

    public static <T> void bind(ObservableList<T> list1, ObservableList<T> list2) {
        runNowAndOnListChange(c -> list1.setAll(list2), list2);
    }

    public static <A, B> void bindConverted(ObservableList<A> aList, ObservableList<B> bList, Converter<B, A> bToAConverter) {
        runNowAndOnListChange(c -> setAllConverted(bList, bToAConverter, aList), bList);
    }

    public static <T> void runNowAndOnListChange(ListChangeListener<T> listener, ObservableList<T> list) {
        listener.onChanged(null);
        runOnListChange(listener, list);
    }

    public static <T> void runOnListChange(ListChangeListener<T> listener, ObservableList<T> list) {
        list.addListener(listener);
    }
}