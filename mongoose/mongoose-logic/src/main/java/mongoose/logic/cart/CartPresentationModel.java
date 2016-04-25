package mongoose.logic.cart;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.ngui.presentationmodel.PresentationModel;

/**
 * @author Bruno Salmon
 */
class CartPresentationModel implements PresentationModel {

    private final Property<String> cartUuidProperty = new SimpleObjectProperty<>();
    Property<String> cartUuidProperty() { return cartUuidProperty; }

    // Display output

    private final Property<DisplayResult> documentDisplayResultProperty = new SimpleObjectProperty<>();
    Property<DisplayResult> documentDisplayResultProperty() { return documentDisplayResultProperty; }

    private final Property<DisplayResult> documentLineDisplayResultProperty = new SimpleObjectProperty<>();
    Property<DisplayResult> documentLineDisplayResultProperty() { return documentLineDisplayResultProperty; }

    private final Property<DisplayResult> paymentDisplayResultProperty = new SimpleObjectProperty<>();
    Property<DisplayResult> paymentDisplayResultProperty() { return paymentDisplayResultProperty; }

}
