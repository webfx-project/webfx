package naga.core.spi.toolkit.property;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import naga.core.util.function.Converter;
import naga.core.util.tuples.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class MappedObservableList {

    public static <A, B> ObservableList<B> create(ObservableList<A> aList, Converter<A, B> aToBConverter, Converter<B, A> bToAConverter) {
        if (aList == null)
            return null;
        ObservableList<B> bList = FXCollections.observableArrayList();
        Unit<Boolean> syncing = new Unit<>(false);
        aList.addListener((ListChangeListener<A>) c -> {
            if (!syncing.get()) {
                syncing.set(true);
                copy(aList, bList, aToBConverter);
                syncing.set(false);
            }
        });
        bList.addListener((ListChangeListener<B>) c -> {
            if (!syncing.get()) {
                syncing.set(true);
                copy(bList, aList, bToAConverter);
                syncing.set(false);
            }
        });
        return bList;
    }

    private static <A, B> void copy(List<A> aList, ObservableList<B> bList, Converter<A, B> aToBConverter) {
        // GWT/J2OBJC bList.setAll(aList.stream().map(aToBConverter::convert).collect(Collectors.toList()));
        List<B> list = new ArrayList<>(aList.size());
        for (A a : aList)
            list.add(aToBConverter.convert(a));
        bList.setAll(list);
    }

}
