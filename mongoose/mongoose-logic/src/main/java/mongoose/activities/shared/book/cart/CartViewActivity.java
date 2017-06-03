package mongoose.activities.shared.book.cart;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mongoose.activities.shared.book.event.shared.BookingOptionsPanel;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.Document;
import mongoose.services.CartService;
import mongoose.services.EventService;
import naga.commons.util.collection.Collections;
import naga.framework.activity.view.impl.ViewActivityImpl;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.controls.LayoutUtil;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.mapping.EntityListToDisplayResultSetGenerator;
import naga.fx.properties.Properties;
import naga.fxdata.control.DataGrid;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.displaydata.DisplaySelection;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class CartViewActivity extends ViewActivityImpl {

    private final Property<DisplayResultSet> documentDisplayResultSetProperty = new SimpleObjectProperty<>();
    private final Property<DisplayResultSet> paymentDisplayResultSetProperty = new SimpleObjectProperty<>();
    // Display input & output
    private final Property<DisplaySelection> documentDisplaySelectionProperty = new SimpleObjectProperty<>();

    private BookingOptionsPanel bookingOptionsPanel;

    private final Property<Object> cartUuidProperty = new SimpleObjectProperty<>();

    private int selectedRow;
    private Document selectedDocument;
    private WorkingDocument selectedWorkingDocument;

    @Override
    public Node buildUi() {
        I18n i18n = getI18n();
        BorderPane bookingsPanel = HighLevelComponents.createSectionPanel(null, null, "YourBookings", i18n);
        DataGrid documentTable = LayoutUtil.setPrefMaxHeightToMin(new DataGrid());
        bookingsPanel.setCenter(new VBox(documentTable));
        BorderPane optionsPanel = HighLevelComponents.createSectionPanel(null, null, "YourOptions", i18n);
        bookingOptionsPanel = new BookingOptionsPanel(i18n);
        optionsPanel.setCenter(bookingOptionsPanel.getGrid());
        BorderPane paymentsPanel = HighLevelComponents.createSectionPanel(null, null, "YourPayments", i18n);
        DataGrid paymentTable = LayoutUtil.setPrefMaxHeightToMin(new DataGrid());
        paymentsPanel.setCenter(new VBox(paymentTable));

        Button addBookingButton = i18n.translateText(new Button(), "AddAnotherBooking");
        Button modifyBookingButton = i18n.translateText(new Button(), "Modify");
        Button paymentButton = i18n.translateText(new Button(), "MakePayment");
        HBox buttonBar = new HBox(20, addBookingButton, LayoutUtil.createHGrowable(), modifyBookingButton, LayoutUtil.createHGrowable(), paymentButton);

        addBookingButton.setOnAction(e -> {
            if (selectedDocument != null) {
                EventService.getOrCreateFromDocument(selectedDocument).setCurrentCart(selectedDocument.getCart());
                getHistory().push("/book/event/" + selectedDocument.getEventId().getPrimaryKey() + "/fees");
            }
        });

        modifyBookingButton.setOnAction(e -> {
            EventService eventService = EventService.getOrCreateFromDocument(selectedDocument);
            eventService.setSelectedOptionsPreselection(null);
            eventService.setWorkingDocument(selectedWorkingDocument);
            getHistory().push("/book/event/" + selectedDocument.getEventId().getPrimaryKey() + "/options");
        });

        paymentButton.setOnAction(e -> getHistory().push("/book/cart/" + cartUuidProperty.getValue() + "/payment"));

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        documentTable.displaySelectionProperty().bindBidirectional(documentDisplaySelectionProperty);
        // User outputs: the presentation model changes are transferred in the UI
        documentTable.displayResultSetProperty().bind(documentDisplayResultSetProperty);
        paymentTable.displayResultSetProperty().bind(paymentDisplayResultSetProperty);

        syncBookingOptionsPanelIfReady();

        return LayoutUtil.createVerticalScrollPane(new VBox(20, bookingsPanel, optionsPanel, paymentsPanel, buttonBar));
    }

    @Override
    protected void fetchRouteParameters() {
        cartUuidProperty.setValue(getParameter("cartUuid"));
    }

    @Override
    public void onStart() {
        super.onStart();
        startLogic();
    }

    @Override
    protected void refreshDataOnActive() {
        cartService().unload();
        loadAndDisplayCart(false);
    }

    private CartService cartService() {
        return CartService.getOrCreate(cartUuidProperty.getValue(), getDataSourceModel());
    }

    private void displayEntities(List<? extends Entity> entities, String columnsDefinition, Object classId, Property<DisplayResultSet> displayResultSetProperty) {
        displayResultSetProperty.setValue(EntityListToDisplayResultSetGenerator.createDisplayResultSet(entities, columnsDefinition
                , getDataSourceModel().getDomainModel(), classId, getI18n()));
    }

    public void startLogic() {
        Properties.runNowAndOnPropertiesChange(p -> loadAndDisplayCart(true), cartUuidProperty);
        Properties.runNowAndOnPropertiesChange(p -> displayCart(), getI18n().dictionaryProperty());
        documentDisplaySelectionProperty.addListener((observable, oldValue, selection) -> {
            int selectedRow = selection.getSelectedRow();
            CartService cartService = cartService();
            List<Document> cartDocuments = cartService.getCartDocuments();
            if (Collections.indexInRange(selectedRow, cartDocuments)) {
                selectedDocument = cartDocuments.get(this.selectedRow = selectedRow);
                selectedWorkingDocument = cartService.getCartWorkingDocuments().get(selectedRow);
                syncBookingOptionsPanelIfReady();
            }
        });
    }

    private void loadAndDisplayCart(boolean resetSelection) {
        if (resetSelection)
            selectedRow = 0;
        cartService().onCartDocuments().setHandler(ar -> {
            if (ar.succeeded())
                displayCart();
        });
    }

    private void displayCart() {
        List<Document> cartDocuments = cartService().getCartDocuments();
        displayEntities(cartDocuments, "[" +
                        "'ref'," +
                        "'person_firstName'," +
                        "'person_lastName'," +
                        "{expression: 'price_net', format: 'price'}," +
                        "{expression: 'price_deposit', format: 'price'}," +
                        "{expression: 'price_balance', format: 'price'}" +
                        "]"
                , "Document", documentDisplayResultSetProperty);
        displayEntities(cartService().getCartPayments(), "[" +
                        "{expression: 'date', format: 'dateTime'}," +
                        "{expression: 'document.ref', label: 'Booking ref'}," +
                        "{expression: 'method.name', label: 'Method'}," +
                        "{expression: 'amount', format: 'price'}," +
                        "{expression: 'pending ? `Pending` : successful ? `Success` : `Failed`', label: 'Status'}" +
                        "]"
                , "MoneyTransfer", paymentDisplayResultSetProperty);
        javafx.application.Platform.runLater(() -> {
            if (Collections.indexInRange(selectedRow, cartDocuments))
                documentDisplaySelectionProperty.setValue(DisplaySelection.createSingleRowSelection(selectedRow));
        });
    }

    private void syncBookingOptionsPanelIfReady() {
        if (bookingOptionsPanel != null && selectedWorkingDocument != null)
            bookingOptionsPanel.syncUiFromModel(selectedWorkingDocument);
    }
}
