package mongoose.activities.shared.book.event.terms;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.shared.book.event.shared.BookingProcessPresentationModel;
import naga.fxdata.displaydata.DisplayResult;

/**
 * @author Bruno Salmon
 */
class TermsPresentationModel extends BookingProcessPresentationModel {

    // Display output

    private final Property<DisplayResult> termsLetterDisplayResultProperty = new SimpleObjectProperty<>();
    Property<DisplayResult> termsLetterDisplayResultProperty() { return termsLetterDisplayResultProperty; }

}
