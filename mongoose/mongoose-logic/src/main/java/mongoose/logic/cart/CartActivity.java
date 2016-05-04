package mongoose.logic.cart;

import mongoose.domainmodel.DomainModelSnapshotLoader;
import naga.core.ngui.displayresultset.DisplayColumn;
import naga.core.ngui.presentation.PresentationActivity;
import naga.core.ngui.presentation.UiBuilder;
import naga.core.ngui.rx.RxFilter;
import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.nodes.BorderPane;
import naga.core.spi.gui.nodes.Table;

/**
 * @author Bruno Salmon
 */
public class CartActivity extends PresentationActivity<CartUiModel, CartPresentationModel> {

    public static UiBuilder<CartUiModel> uiBuilder;

    public CartActivity() {
        setPresentationModelFactory(CartPresentationModel::new);
        setUiBuilder(uiBuilder);
    }

    @Override
    protected CartUiModel buildUiModel(GuiToolkit toolkit) {
        // Building the UI components
        Table documentTable = toolkit.createNode(Table.class);
        Table documentLineTable = toolkit.createNode(Table.class);
        Table paymentTable = toolkit.createNode(Table.class);

        // Displaying the UI
        return new CartUiModel(toolkit.createNode(BorderPane.class)
                .setTop(documentTable)
                .setCenter(documentLineTable)
                .setBottom(paymentTable),
                documentTable, documentLineTable, paymentTable);
    }

    @Override
    protected void bindUiModelWithPresentationModel(CartUiModel um, CartPresentationModel pm) {
        // Binding the UI with the presentation model for further state changes
        // User outputs: the presentation model changes are transferred in the UI
        um.getDocumentTable().displayResultSetProperty().bind(pm.documentDisplayResultSetProperty());
        um.getDocumentLineTable().displayResultSetProperty().bind(pm.documentLineDisplayResultSetProperty());
        um.getPaymentTable().displayResultSetProperty().bind(pm.paymentDisplayResultSetProperty());
    }

    @Override
    protected void initializePresentationModel(CartPresentationModel pm) {
        pm.cartUuidProperty().setValue(getActivityContext().getParams().get("cartUuid"));
    }

    @Override
    protected void bindPresentationModelWithLogic(CartPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        new RxFilter("{class: Document, orderBy: 'creationDate desc'}")
                .setDataSourceModel(DomainModelSnapshotLoader.getDataSourceModel())
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
        new RxFilter("{class: 'DocumentLine', orderBy: 'creationDate'}")
                .setDataSourceModel(DomainModelSnapshotLoader.getDataSourceModel())
                // Condition
                .combine(pm.cartUuidProperty(), s -> "{where: 'document.cart.uuid=`" + s + "`'}")
                //.combine("{where: 'document.cart.uuid=?cartUuid'}")
                .setDisplayColumns(
                        new DisplayColumn("Site", "site.name"),
                        new DisplayColumn("Item", "item.name"),
                        new DisplayColumn("Dates", "dates"),
                        new DisplayColumn("Fees", "price_net")
                )
                .displayResultSetInto(pm.documentLineDisplayResultSetProperty());


        // Loading the domain model and setting up the reactive filter
        new RxFilter("{class: 'MoneyTransfer', orderBy: 'date'}")
                .setDataSourceModel(DomainModelSnapshotLoader.getDataSourceModel())
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
