package mongoose.activities.frontend.event.fees;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.frontend.event.shared.BookingProcessPresentationModel;
import naga.fxdata.displaydata.DisplayResultSet;

/**
 * @author Bruno Salmon
 */
class FeesPresentationModel extends BookingProcessPresentationModel {

    // Display output

    private final Property<DisplayResultSet> dateInfoDisplayResultSetProperty = new SimpleObjectProperty<>();
    public Property<DisplayResultSet> dateInfoDisplayResultSetProperty() { return dateInfoDisplayResultSetProperty; }
}
