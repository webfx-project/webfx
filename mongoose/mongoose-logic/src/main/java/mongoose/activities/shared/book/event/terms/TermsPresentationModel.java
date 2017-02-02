package mongoose.activities.shared.book.event.terms;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.shared.book.event.shared.BookingProcessPresentationModel;
import naga.fxdata.displaydata.DisplayResultSet;

/**
 * @author Bruno Salmon
 */
public class TermsPresentationModel extends BookingProcessPresentationModel {

    // Display output

    private final Property<DisplayResultSet> termsLetterDisplayResultSetProperty = new SimpleObjectProperty<>();
    public Property<DisplayResultSet> termsLetterDisplayResultSetProperty() { return termsLetterDisplayResultSetProperty; }

}
