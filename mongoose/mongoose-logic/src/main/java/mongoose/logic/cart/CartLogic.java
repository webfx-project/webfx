package mongoose.logic.cart;

import mongoose.domainmodel.DomainModelSnapshotLoader;
import naga.core.ngui.displayresultset.DisplayColumn;
import naga.core.ngui.routing.UiRouteHandler;
import naga.core.ngui.routing.UiState;
import naga.core.ngui.rx.RxFilter;
import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.nodes.BorderPane;
import naga.core.spi.gui.nodes.Table;

/**
 * @author Bruno Salmon
 */
public class CartLogic {

    public static UiRouteHandler cartUiRouterHandler = new UiRouteHandler()
            .setPresentationModelFactory(CartPresentationModel::new)
            .setPresentationModelInitializer(CartLogic::initializeCartPresentationModel)
            .setUiBuilder(CartLogic::buildCartUi)
            .setPresentationModelLogicBinder(CartLogic::doCartPresentationModelLogicBinding);

    private static void initializeCartPresentationModel(UiState uiState) {
        CartPresentationModel pm = (CartPresentationModel) uiState.presentationModel();
        pm.cartUuidProperty().setValue(uiState.getParams().get("cartUuid"));
    }

    private static void buildCartUi(UiState uiState) {
        // Building the UI components
        GuiToolkit toolkit = GuiToolkit.get();
        Table documentTable = toolkit.createNode(Table.class);
        Table documentLineTable = toolkit.createNode(Table.class);
        Table paymentTable = toolkit.createNode(Table.class);

        // Displaying the UI
        toolkit.displayRootNode(toolkit.createNode(BorderPane.class)
                .setTop(documentTable)
                .setCenter(documentLineTable)
                .setBottom(paymentTable));

        // Initializing the UI state from the presentation model current state
        CartPresentationModel pm = (CartPresentationModel) uiState.presentationModel();

        // Binding the UI with the presentation model for further state changes
        // User outputs: the presentation model changes are transferred in the UI
        documentTable.displayResultSetProperty().bind(pm.documentDisplayResultSetProperty());
        documentLineTable.displayResultSetProperty().bind(pm.documentLineDisplayResultSetProperty());
        paymentTable.displayResultSetProperty().bind(pm.paymentDisplayResultSetProperty());
    }

    private static void doCartPresentationModelLogicBinding(CartPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        new RxFilter("{class: Document, orderBy: 'creationDate desc'}")
                .setDomainModel(DomainModelSnapshotLoader.getOrLoadDomainModel())
                .setDataSourceId(3)
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
                .setDomainModel(DomainModelSnapshotLoader.getOrLoadDomainModel())
                .setDataSourceId(3)
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
                .setDomainModel(DomainModelSnapshotLoader.getOrLoadDomainModel())
                .setDataSourceId(3)
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
