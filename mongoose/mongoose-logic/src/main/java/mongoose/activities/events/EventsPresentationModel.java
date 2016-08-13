package mongoose.activities.events;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.framework.ui.presentation.PresentationModel;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;

/**
 * @author Bruno Salmon
 */
class EventsPresentationModel implements PresentationModel {

    // Display input

    private final Property<String> searchTextProperty = new SimpleObjectProperty<>();
    Property<String> searchTextProperty() { return searchTextProperty; }

    private final Property<Boolean> limitProperty = new SimpleObjectProperty<>(true); // Limit initially set to true
    Property<Boolean> limitProperty() { return limitProperty; }

    private final Property<DisplaySelection> eventsDisplaySelectionProperty = new SimpleObjectProperty<>();
    Property<DisplaySelection> eventsDisplaySelectionProperty() { return eventsDisplaySelectionProperty; }

    // Display output

    private final Property<DisplayResultSet> eventsDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> eventsDisplayResultSetProperty() { return eventsDisplayResultSetProperty; }

}
