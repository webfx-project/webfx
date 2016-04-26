package mongoose.logic.cart;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.ngui.presentationmodel.PresentationModel;

/**
 * @author Bruno Salmon
 */
class CartPresentationModel implements PresentationModel {

    private final Property<String> cartUuidProperty = new SimpleObjectProperty<>();
    Property<String> cartUuidProperty() { return cartUuidProperty; }

    // Display output

    private final Property<DisplayResultSet> documentDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> documentDisplayResultSetProperty() { return documentDisplayResultSetProperty; }

    private final Property<DisplayResultSet> documentLineDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> documentLineDisplayResultSetProperty() { return documentLineDisplayResultSetProperty; }

    private final Property<DisplayResultSet> paymentDisplayResultSetProperty = new SimpleObjectProperty<>();
    Property<DisplayResultSet> paymentDisplayResultSetProperty() { return paymentDisplayResultSetProperty; }

}
