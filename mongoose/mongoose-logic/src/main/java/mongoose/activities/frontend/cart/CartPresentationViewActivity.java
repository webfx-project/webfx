package mongoose.activities.frontend.cart;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import naga.framework.activity.presentation.view.impl.PresentationViewActivityImpl;
import naga.fxdata.control.DataGrid;

/**
 * @author Bruno Salmon
 */
public class CartPresentationViewActivity extends PresentationViewActivityImpl<CartPresentationModel> {

    private DataGrid documentTable;
    private DataGrid documentLineTable;
    private DataGrid paymentTable;

    @Override
    protected void createViewNodes(CartPresentationModel pm) {
        documentTable = new DataGrid();
        documentLineTable = new DataGrid();
        paymentTable = new DataGrid();

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        documentTable.displaySelectionProperty().bindBidirectional(pm.documentDisplaySelectionProperty());
        // User outputs: the presentation model changes are transferred in the UI
        documentTable.displayResultSetProperty().bind(pm.documentDisplayResultSetProperty());
        documentLineTable.displayResultSetProperty().bind(pm.documentLineDisplayResultSetProperty());
        paymentTable.displayResultSetProperty().bind(pm.paymentDisplayResultSetProperty());

    }

    @Override
    protected Node assemblyViewNodes() {
        return new VBox(documentTable, documentLineTable, paymentTable);
    }
}
