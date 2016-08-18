package naga.toolkit.util;

import javafx.collections.ObservableList;
import naga.commons.util.Arrays;
import naga.commons.util.collection.Collections;
import naga.commons.util.function.Converter;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class ObservableLists {

    public static <T> void setAllNonNulls(ObservableList<T> list, T... elements) {
        if (Arrays.allNonNulls(elements))
            list.setAll(elements);
        list.setAll(Arrays.nonNullsAsList(elements));
    }

    public static <A, B> void setAllConverted(List<A> aList, Converter<A, B> aToBConverter, ObservableList<B> bList) {
        // GWT/J2OBJC bList.setAll(aList.stream().map(aToBConverter::convert).collect(Collectors.toList()));
        bList.setAll(Collections.convert(aList, aToBConverter));
    }
}
