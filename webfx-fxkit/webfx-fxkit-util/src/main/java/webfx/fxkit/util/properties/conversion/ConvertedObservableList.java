package webfx.fxkit.util.properties.conversion;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import webfx.fxkit.util.properties.ObservableLists;
import webfx.platform.shared.util.function.Converter;
import webfx.platform.shared.util.tuples.Unit;

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
