package mongoose.activities.frontend.cart;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.fx.ext.control.DataGrid;
import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
class CartViewModel extends AbstractViewModel {

    private final DataGrid documentTable;
    private final DataGrid documentLineTable;
    private final DataGrid paymentTable;

    CartViewModel(Node contentNode, DataGrid documentTable, DataGrid documentLineTable, DataGrid paymentTable) {
        super(contentNode);
        this.documentTable = documentTable;
        this.documentLineTable = documentLineTable;
        this.paymentTable = paymentTable;
    }

    DataGrid getDocumentTable() {
        return documentTable;
    }

    DataGrid getDocumentLineTable() {
        return documentLineTable;
    }

    DataGrid getPaymentTable() {
        return paymentTable;
    }
}
