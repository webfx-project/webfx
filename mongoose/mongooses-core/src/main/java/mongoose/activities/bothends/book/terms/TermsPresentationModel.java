package mongoose.activities.bothends.book.terms;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.bothends.book.shared.BookingProcessPresentationModel;
import webfx.fxdata.displaydata.DisplayResult;

/**
 * @author Bruno Salmon
 */
final class TermsPresentationModel extends BookingProcessPresentationModel {

    // Display output

    private final Property<DisplayResult> termsLetterDisplayResultProperty = new SimpleObjectProperty<>();
    Property<DisplayResult> termsLetterDisplayResultProperty() { return termsLetterDisplayResultProperty; }

}
