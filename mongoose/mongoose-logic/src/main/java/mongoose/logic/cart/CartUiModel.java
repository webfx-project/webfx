package mongoose.logic.cart;

import naga.core.ngui.presentation.UiModel;
import naga.core.spi.toolkit.GuiNode;
import naga.core.spi.toolkit.nodes.Table;

/**
 * @author Bruno Salmon
 */
public class CartUiModel implements UiModel {

    private final GuiNode contentNode;
    private final Table documentTable;
    private final Table documentLineTable;
    private final Table paymentTable;

    @Override
    public GuiNode getContentNode() {
        return contentNode;
    }

    public Table getDocumentTable() {
        return documentTable;
    }

    public Table getDocumentLineTable() {
        return documentLineTable;
    }

    public Table getPaymentTable() {
        return paymentTable;
    }

    public CartUiModel(GuiNode contentNode, Table documentTable, Table documentLineTable, Table paymentTable) {


        this.contentNode = contentNode;
        this.documentTable = documentTable;
        this.documentLineTable = documentLineTable;
        this.paymentTable = paymentTable;
    }
}
