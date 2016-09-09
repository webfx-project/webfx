package mongoose.activities.event.letters;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.event.shared.EventBasedPresentationModel;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;

/**
 * @author Bruno Salmon
 */
class LettersPresentationModel extends EventBasedPresentationModel {

    // Display input

    private final Property<String> searchTextProperty = new SimpleObjectProperty<>();
    Property<String> searchTextProperty() { return searchTextProperty; }

    private final Property<Boolean> limitProperty = new SimpleObjectProperty<>(true); // Limit initially set to true
    Property<Boolean> limitProperty() { return limitProperty; }

    private final Property<DisplaySelection> bookingsDisplaySelectionProperty = new SimpleObjectProperty<>();
    Property<DisplaySelection> bookingsDisplaySelectionProperty() { return bookingsDisplaySelectionProperty; }

    // Display output

    private final Property<DisplayResultSet> bookingsDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> bookingsDisplayResultSetProperty() { return bookingsDisplayResultSetProperty; }

}
