package mongoose.logic.cart;

import naga.core.ngui.presentation.ViewModel;
import naga.core.spi.toolkit.GuiNode;
import naga.core.spi.toolkit.nodes.ActionButton;
import naga.core.spi.toolkit.nodes.Table;

/**
 * @author Bruno Salmon
 */
class CartViewModel implements ViewModel {

    private final GuiNode contentNode;
    private final Table documentTable;
    private final Table documentLineTable;
    private final Table paymentTable;
    private final ActionButton testButton;

    public CartViewModel(GuiNode contentNode, Table documentTable, Table documentLineTable, Table paymentTable, ActionButton testButton) {
        this.contentNode = contentNode;
        this.documentTable = documentTable;
        this.documentLineTable = documentLineTable;
        this.paymentTable = paymentTable;
        this.testButton = testButton;
    }

    @Override
    public GuiNode getContentNode() {
        return contentNode;
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

    public ActionButton getTestButton() {
        return testButton;
    }
}
