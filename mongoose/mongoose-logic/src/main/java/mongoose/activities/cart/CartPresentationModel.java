package mongoose.activities.cart;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.ui.displayselection.DisplaySelection;
import naga.core.ui.presentation.PresentationModel;

/**
 * @author Bruno Salmon
 */
class CartPresentationModel implements PresentationModel {

    // Input parameter

    private final Property<String> cartUuidProperty = new SimpleObjectProperty<>();
    Property<String> cartUuidProperty() { return cartUuidProperty; }

    // Display output

    private final Property<DisplayResultSet> documentDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> documentDisplayResultSetProperty() { return documentDisplayResultSetProperty; }

    private final Property<DisplayResultSet> documentLineDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> documentLineDisplayResultSetProperty() { return documentLineDisplayResultSetProperty; }

    private final Property<DisplayResultSet> paymentDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> paymentDisplayResultSetProperty() { return paymentDisplayResultSetProperty; }

    // Display input & output

    private final Property<DisplaySelection> documentDisplaySelectionProperty = new SimpleObjectProperty<>();
    Property<DisplaySelection> documentDisplaySelectionProperty() { return documentDisplaySelectionProperty; }

}
