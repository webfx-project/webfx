package mongoose.logic.cart;

import mongoose.domainmodel.DomainModelSnapshotLoader;
import naga.core.ngui.displayresult.DisplayColumn;
import naga.core.ngui.routing.UiRouteHandler;
import naga.core.ngui.routing.UiState;
import naga.core.ngui.rx.RxFilter;
import naga.core.orm.filter.StringFilterBuilder;
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
        documentTable.displayResultProperty().bind(pm.documentDisplayResultProperty());
        documentLineTable.displayResultProperty().bind(pm.documentLineDisplayResultProperty());
        paymentTable.displayResultProperty().bind(pm.paymentDisplayResultProperty());
    }

    private static void doCartPresentationModelLogicBinding(CartPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        new RxFilter()
                .setDomainModel(DomainModelSnapshotLoader.getOrLoadDomainModel())
                .setDataSourceId(3)
                // Base filter
                .combine(new StringFilterBuilder("Document")
                        .setOrderBy("creationDate desc"))
                // Condition
                .combine(pm.cartUuidProperty(), s -> "cart.uuid='" + s + "'")
                .setDisplayColumns(
                        new DisplayColumn("Ref", "ref"),
                        new DisplayColumn("First name", "person_firstName"),
                        new DisplayColumn("Last name", "person_lastName"),
                        new DisplayColumn("Invoiced", "price_net"),
                        new DisplayColumn("Deposit", "price_deposit"),
                        new DisplayColumn("Balance", "price_balance")
                )
                .displayResultInto(pm.documentDisplayResultProperty());

        // Loading the domain model and setting up the reactive filter
        new RxFilter()
                .setDomainModel(DomainModelSnapshotLoader.getOrLoadDomainModel())
                .setDataSourceId(3)
                // Base filter
                .combine(new StringFilterBuilder("DocumentLine")
                        .setOrderBy("creationDate"))
                // Condition
                .combine(pm.cartUuidProperty(), s -> "document.cart.uuid='" + s + "'")
                .setDisplayColumns(
                        new DisplayColumn("Site", "site.name"),
                        new DisplayColumn("Item", "item.name"),
                        new DisplayColumn("Dates", "dates"),
                        new DisplayColumn("Fees", "price_net")
                )
                .displayResultInto(pm.documentLineDisplayResultProperty());


        // Loading the domain model and setting up the reactive filter
        new RxFilter()
                .setDomainModel(DomainModelSnapshotLoader.getOrLoadDomainModel())
                .setDataSourceId(3)
                // Base filter
                .combine(new StringFilterBuilder("MoneyTransfer")
                        .setOrderBy("date"))
                // Condition
                .combine(pm.cartUuidProperty(), s -> "document.cart.uuid='" + s + "'")
                .setDisplayColumns(
                        new DisplayColumn("Date", "date"),
                        new DisplayColumn("Booking ref", "document.ref"),
                        new DisplayColumn("Amount", "amount"),
                        new DisplayColumn("Status", "pending ? 'Pending' : successful ? 'Success' : 'Failed'")
                )
                .displayResultInto(pm.paymentDisplayResultProperty());
    }
}
