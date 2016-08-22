package mongoose.activities.cart;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;
import naga.framework.ui.presentation.PresentationModel;

/**
 * @author Bruno Salmon
 */
class CartPresentationModel implements PresentationModel {

    // Input parameter

    private final Property<Object> cartUuidProperty = new SimpleObjectProperty<>();
    Property<Object> cartUuidProperty() { return cartUuidProperty; }

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
