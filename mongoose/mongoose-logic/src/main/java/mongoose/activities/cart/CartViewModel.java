package mongoose.activities.cart;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
class CartViewModel extends AbstractViewModel {

    private final Table documentTable;
    private final Table documentLineTable;
    private final Table paymentTable;

    CartViewModel(GuiNode contentNode, Table documentTable, Table documentLineTable, Table paymentTable) {
        super(contentNode);
        this.documentTable = documentTable;
        this.documentLineTable = documentLineTable;
        this.paymentTable = paymentTable;
    }

    Table getDocumentTable() {
        return documentTable;
    }

    Table getDocumentLineTable() {
        return documentLineTable;
    }

    Table getPaymentTable() {
        return paymentTable;
    }
}
