package mongoose.activities.shared.generic;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.displaydata.DisplaySelection;

/**
 * @author Bruno Salmon
 */
public class GenericTablePresentationModel {

    // Display input

    private final Property<String> searchTextProperty = new SimpleObjectProperty<>();
    public Property<String> searchTextProperty() { return searchTextProperty; }

    private final Property<Boolean> limitProperty = new SimpleObjectProperty<>(true); // Limit initially set to true
    public Property<Boolean> limitProperty() { return limitProperty; }

    private final Property<DisplaySelection> genericDisplaySelectionProperty = new SimpleObjectProperty<>();
    public Property<DisplaySelection> genericDisplaySelectionProperty() { return genericDisplaySelectionProperty; }

    // Display output

    private final Property<DisplayResultSet> genericDisplayResultSetProperty = new SimpleObjectProperty<>();
    public Property<DisplayResultSet> genericDisplayResultSetProperty() { return genericDisplayResultSetProperty; }

}
