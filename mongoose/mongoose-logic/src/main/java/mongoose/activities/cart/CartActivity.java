package mongoose.activities.cart;

import naga.core.orm.entity.Entity;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.nodes.Table;
import naga.core.spi.toolkit.nodes.VBox;
import naga.core.ui.presentation.PresentationActivity;
import naga.core.ui.rx.RxFilter;

/**
 * @author Bruno Salmon
 */
public class CartActivity extends PresentationActivity<CartViewModel, CartPresentationModel> {

    public CartActivity() {
        super(CartPresentationModel::new);
    }

    @Override
    protected CartViewModel buildView(Toolkit toolkit) {
        // Building the UI components
        Table documentTable = toolkit.createTable();
        Table documentLineTable = toolkit.createTable();
        Table paymentTable = toolkit.createTable();

        // Displaying the UI
        VBox vBox = toolkit.createVBox();
        vBox.getChildren().setAll(documentTable, documentLineTable, paymentTable);
        return new CartViewModel(vBox, documentTable, documentLineTable, paymentTable);
    }

    @Override
    protected void bindViewModelWithPresentationModel(CartViewModel vm, CartPresentationModel pm) {
        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        vm.getDocumentTable().displaySelectionProperty().bindBidirectional(pm.documentDisplaySelectionProperty());
        // User outputs: the presentation model changes are transferred in the UI
        vm.getDocumentTable().displayResultSetProperty().bind(pm.documentDisplayResultSetProperty());
        vm.getDocumentLineTable().displayResultSetProperty().bind(pm.documentLineDisplayResultSetProperty());
        vm.getPaymentTable().displayResultSetProperty().bind(pm.paymentDisplayResultSetProperty());
    }

    @Override
    protected void initializePresentationModel(CartPresentationModel pm) {
        pm.cartUuidProperty().setValue(getParams().get("cartUuid"));
    }

    @Override
    protected void bindPresentationModelWithLogic(CartPresentationModel pm) {
        // Setting up the documents filter
        RxFilter documentFilter = createRxFilter("{class: 'Document', orderBy: 'creationDate desc'}")
                // Condition
                .combine(pm.cartUuidProperty(), s -> "{where: 'cart.uuid=`" + s + "`'}")
                //.registerParameter(new Parameter("cartUuid", "constant"))
                //.registerParameter(new Parameter("cartUuid", pm.cartUuidProperty()))
                //.combine("{where: 'cart.uuid=?cartUuid'}")
                .setExpressionColumns("[" +
                        "'ref'," +
                        "'person_firstName'," +
                        "'person_lastName'," +
                        "{expression: 'price_net', format: 'price'}," +
                        "{expression: 'price_deposit', format: 'price'}," +
                        "{expression: 'price_balance', format: 'price'}" +
                        "]")
                .setDisplaySelectionProperty(pm.documentDisplaySelectionProperty())
                .selectFirstRowOnFirstDisplay()
                .displayResultSetInto(pm.documentDisplayResultSetProperty());

        // Setting up the document lines filter
        createRxFilter("{class: 'DocumentLine', where: 'item.family.code!=`round`', orderBy: 'item.family.ord,item.ord'}")
                // Condition
                .combine(pm.cartUuidProperty(), s -> "{where: 'document.cart.uuid=`" + s + "`'}")
                .combine(documentFilter.getDisplaySelectionProperty(), displaySelection -> {
                    Entity selectedEntity = documentFilter.getSelectedEntity();
                    return selectedEntity == null ? "{where: 'false'}" : "{where: 'document=" + selectedEntity.getId().getPrimaryKey() + "'}";
                })
                //.combine("{where: 'document=?documentDisplaySelection'}")
                .setExpressionColumns("[" +
                        "{expression: 'site.name', label: 'Site'}," +
                        "{expression: 'item.name', label: 'Item'}," +
                        "'dates'," +
                        "{expression: 'price_net', label: 'Fees', format: 'price'}" +
                        "]")
                .displayResultSetInto(pm.documentLineDisplayResultSetProperty());

        // Setting up the payments filter
        createRxFilter("{class: 'MoneyTransfer', orderBy: 'date'}")
                // Condition
                .combine(pm.cartUuidProperty(), s -> "{where: 'document.cart.uuid=`" + s + "`'}")
                //.combine("{where: 'document.cart.uuid=?cartUuid'}")
                .setExpressionColumns("[" +
                        "{expression: 'date', format: 'date'}," +
                        "{expression: 'document.ref', label: 'Booking ref'}," +
                        "{expression: 'method.name', label: 'Method'}," +
                        "{expression: 'amount', format: 'price'}," +
                        "{expression: 'pending ? `Pending` : successful ? `Success` : `Failed`', label: 'Status'}" +
                        "]")
                .displayResultSetInto(pm.paymentDisplayResultSetProperty());
    }
}
