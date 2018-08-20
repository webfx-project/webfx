package naga.fx.properties.conversion;

import emul.javafx.collections.FXCollections;
import emul.javafx.collections.ListChangeListener;
import emul.javafx.collections.ObservableList;
import naga.util.function.Converter;
import naga.util.tuples.Unit;
import naga.fx.properties.ObservableLists;

/**
 * @author Bruno Salmon
 */
public final class ConvertedObservableList {

    public static <A, B> ObservableList<B> create(ObservableList<A> aList, Converter<A, B> aToBConverter, Converter<B, A> bToAConverter) {
        if (aList == null)
            return null;
        ObservableList<B> bList = FXCollections.observableArrayList();
        Unit<Boolean> syncing = new Unit<>(false);
        aList.addListener((ListChangeListener<A>) c -> {
            if (!syncing.get()) {
                syncing.set(true);
                ObservableLists.setAllConverted(aList, aToBConverter, bList);
                syncing.set(false);
            }
        });
        bList.addListener((ListChangeListener<B>) c -> {
            if (!syncing.get()) {
                syncing.set(true);
                ObservableLists.setAllConverted(bList, bToAConverter, aList);
                syncing.set(false);
            }
        });
        return bList;
    }

}
