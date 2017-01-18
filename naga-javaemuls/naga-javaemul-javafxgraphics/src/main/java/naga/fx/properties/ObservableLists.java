package naga.fx.properties;

import emul.javafx.collections.ListChangeListener;
import emul.javafx.collections.ObservableList;
import naga.commons.util.Arrays;
import naga.commons.util.collection.Collections;
import naga.commons.util.function.Converter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public class ObservableLists {

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
                list.setAll(elements.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        }
    }

    public static <A, B> void setAllConverted(List<A> aList, Converter<A, B> aToBConverter, ObservableList<B> bList) {
        // GWT/J2OBJC bList.setAll(aList.stream().map(aToBConverter::convert).collect(Collectors.toList()));
        bList.setAll(Collections.convert(aList, aToBConverter));
    }

    public static <A, B> void setAllNonNullsConverted(List<A> aList, Converter<A, B> aToBConverter, ObservableList<B> bList) {
        // GWT/J2OBJC bList.setAll(aList.stream().map(aToBConverter::convert).collect(Collectors.toList()));
        setAllNonNulls(bList, Collections.convert(aList, aToBConverter));
    }

    public static <T> void bind(ObservableList<T> list1, ObservableList<T> list2) {
        runNowAndOnListChange(() -> list1.setAll(list2), list2);
    }

    public static <A, B> void bindConverted(ObservableList<A> aList, ObservableList<B> bList, Converter<B, A> bToAConverter) {
        runNowAndOnListChange(() -> setAllConverted(bList, bToAConverter, aList), bList);
    }

    public static void runNowAndOnListChange(Runnable runnable, ObservableList list) {
        runnable.run();
        runOnListChange(runnable, list);
    }

    public static void runOnListChange(Runnable runnable, ObservableList list) {
        list.addListener((ListChangeListener) c -> runnable.run());
    }
}