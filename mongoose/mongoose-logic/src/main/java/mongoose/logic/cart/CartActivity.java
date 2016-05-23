package mongoose.logic.cart;

import naga.core.ngui.displayresultset.DisplayColumn;
import naga.core.ngui.presentation.PresentationActivity;
import naga.core.ngui.presentation.ViewBuilder;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.nodes.Table;
import naga.core.spi.toolkit.nodes.VBox;

/**
 * @author Bruno Salmon
 */
public class CartActivity extends PresentationActivity<CartViewModel, CartPresentationModel> {

    public static ViewBuilder<CartViewModel> viewBuilder;

    public CartActivity() {
        setPresentationModelFactory(CartPresentationModel::new);
        setViewBuilder(viewBuilder);
    }

    @Override
    protected CartViewModel buildUiModel(Toolkit toolkit) {
        // Building the UI components
        Table documentTable = toolkit.createNode(Table.class);
        Table documentLineTable = toolkit.createNode(Table.class);
        Table paymentTable = toolkit.createNode(Table.class);

        // Displaying the UI
        VBox vBox = toolkit.createNode(VBox.class);
        vBox.getChildren().setAll(documentTable, documentLineTable, paymentTable);
        return new CartViewModel(vBox, documentTable, documentLineTable, paymentTable);
    }

    @Override
    protected void bindUiModelWithPresentationModel(CartViewModel vm, CartPresentationModel pm) {
        // Binding the UI with the presentation model for further state changes
        // User outputs: the presentation model changes are transferred in the UI
        vm.getDocumentTable().displayResultSetProperty().bind(pm.documentDisplayResultSetProperty());
        vm.getDocumentLineTable().displayResultSetProperty().bind(pm.documentLineDisplayResultSetProperty());
        vm.getPaymentTable().displayResultSetProperty().bind(pm.paymentDisplayResultSetProperty());
    }

    @Override
    protected void initializePresentationModel(CartPresentationModel pm) {
        pm.cartUuidProperty().setValue(getActivityContext().getParams().get("cartUuid"));
    }

    @Override
    protected void bindPresentationModelWithLogic(CartPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createRxFilter("{class: 'Document', orderBy: 'creationDate desc'}")
                // Condition
                .combine(pm.cartUuidProperty(), s -> "{where: 'cart.uuid=`" + s + "`'}")
                //.registerParameter(new Parameter("cartUuid", "constant"))
                //.registerParameter(new Parameter("cartUuid", pm.cartUuidProperty()))
                //.combine("{where: 'cart.uuid=?cartUuid'}")
                .setDisplayColumns(
                        new DisplayColumn("Ref", "ref"),
                        new DisplayColumn("First name", "person_firstName"),
                        new DisplayColumn("Last name", "person_lastName"),
                        new DisplayColumn("Invoiced", "price_net"),
                        new DisplayColumn("Deposit", "price_deposit"),
                        new DisplayColumn("Balance", "price_balance")
                )
                .displayResultSetInto(pm.documentDisplayResultSetProperty());

        // Loading the domain model and setting up the reactive filter
        createRxFilter("{class: 'DocumentLine', where: 'item.family.code!=`round`', orderBy: 'creationDate'}")
                // Condition
                .combine(pm.cartUuidProperty(), s -> "{where: 'document.cart.uuid=`" + s + "`'}")
                //.combine("{where: 'document=?documentDisplaySelection'}")
                .setDisplayColumns(
                        new DisplayColumn("Site", "site.name"),
                        new DisplayColumn("Item", "item.name"),
                        new DisplayColumn("Dates", "dates"),
                        new DisplayColumn("Fees", "price_net")
                )
                .displayResultSetInto(pm.documentLineDisplayResultSetProperty());


        // Loading the domain model and setting up the reactive filter
        createRxFilter("{class: 'MoneyTransfer', orderBy: 'date'}")
                // Condition
                .combine(pm.cartUuidProperty(), s -> "{where: 'document.cart.uuid=`" + s + "`'}")
                //.combine("{where: 'document.cart.uuid=?cartUuid'}")
                .setDisplayColumns(
                        new DisplayColumn("Date", "date"),
                        new DisplayColumn("Booking ref", "document.ref"),
                        new DisplayColumn("Amount", "amount"),
                        new DisplayColumn("Status", "pending ? 'Pending' : successful ? 'Success' : 'Failed'")
                )
                .displayResultSetInto(pm.paymentDisplayResultSetProperty());
    }
}
