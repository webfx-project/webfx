package mongoose.activities.shared.book.cart;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mongoose.activities.shared.book.event.shared.BookingOptionsPanel;
import mongoose.activities.shared.book.event.shared.TranslateFunction;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.domainmodel.format.PriceFormatter;
import mongoose.entities.Event;
import mongoose.services.CartService;
import mongoose.services.EventService;
import naga.commons.util.collection.Collections;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.controls.LayoutUtil;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.mapping.EntityListToDisplayResultSetGenerator;
import naga.fxdata.control.DataGrid;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.displaydata.DisplaySelection;

import java.util.List;

import static naga.framework.ui.format.FormatterRegistry.registerFormatter;

/**
 * @author Bruno Salmon
 */
public class CartViewActivity extends CartBasedViewActivity {

    private final Property<DisplayResultSet> documentDisplayResultSetProperty = new SimpleObjectProperty<>();
    private final Property<DisplayResultSet> paymentDisplayResultSetProperty = new SimpleObjectProperty<>();
    // Display input & output
    private final Property<DisplaySelection> documentDisplaySelectionProperty = new SimpleObjectProperty<>();

    private Label bookingLabel;
    private BookingOptionsPanel bookingOptionsPanel;
    private WorkingDocument selectedWorkingDocument;

    @Override
    public Node buildUi() {
        I18n i18n = getI18n();
        BorderPane bookingsPanel = HighLevelComponents.createSectionPanel(null, null, "YourBookings", i18n);
        DataGrid documentTable = LayoutUtil.setMinMaxHeightToPref(new DataGrid());
        bookingsPanel.setCenter(documentTable);
        BorderPane optionsPanel = HighLevelComponents.createSectionPanel(null, bookingLabel = new Label());
        bookingOptionsPanel = new BookingOptionsPanel(i18n);
        optionsPanel.setCenter(bookingOptionsPanel.getGrid());
        BorderPane paymentsPanel = HighLevelComponents.createSectionPanel(null, null, "YourPayments", i18n);
        DataGrid paymentTable = LayoutUtil.setMinMaxHeightToPref(new DataGrid());
        paymentsPanel.setCenter(paymentTable);

        Button addBookingButton = i18n.translateText(new Button(), "AddAnotherBooking");
        Button modifyBookingButton = i18n.translateText(new Button(), "Modify");
        Button paymentButton = i18n.translateText(new Button(), "MakePayment");
        HBox buttonBar = new HBox(20, addBookingButton, LayoutUtil.createHGrowable(), modifyBookingButton, LayoutUtil.createHGrowable(), paymentButton);

        addBookingButton.setOnAction(e -> getHistory().push("/book/event/" + getEventId() + "/fees"));

        modifyBookingButton.setOnAction(e -> {
            EventService eventService = cartService().getEventService();
            eventService.setSelectedOptionsPreselection(null);
            eventService.setWorkingDocument(selectedWorkingDocument);
            selectedWorkingDocument = null;
            getHistory().push("/book/event/" + getEventId() + "/options");
        });

        paymentButton.setOnAction(e -> getHistory().push("/book/cart/" + getCartUuid() + "/payment"));

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        documentTable.displaySelectionProperty().bindBidirectional(documentDisplaySelectionProperty);
        // User outputs: the presentation model changes are transferred in the UI
        documentTable.displayResultSetProperty().bind(documentDisplayResultSetProperty);
        paymentTable.displayResultSetProperty().bind(paymentDisplayResultSetProperty);

        syncBookingOptionsPanelIfReady();

        return LayoutUtil.createVerticalScrollPaneWithPadding(new VBox(20, bookingsPanel, optionsPanel, paymentsPanel, buttonBar));
    }

    private Event getEvent() {
        return cartService().getEventService().getEvent();
    }

    private Object getEventId() {
        return getEvent().getPrimaryKey();
    }

    @Override
    protected void startLogic() {
        super.startLogic();
        documentDisplaySelectionProperty.addListener((observable, oldValue, selection) -> {
            int selectedRow = selection.getSelectedRow();
            if (selectedRow != -1) {
                selectedWorkingDocument = Collections.get(cartService().getCartWorkingDocuments(), selectedRow);
                syncBookingOptionsPanelIfReady();
            }
        });
    }

    @Override
    protected void onCartLoaded() {
        CartService cartService = cartService();
        if (cartService.getEventService() != null)
            registerFormatter("priceWithCurrency", new PriceFormatter(getEvent()));
        displayEntities(cartService.getCartDocuments(), "[" +
                        "'ref'," +
                        "'person_firstName'," +
                        "'person_lastName'," +
                        "{expression: 'price_net', format: 'priceWithCurrency'}," +
                        "{expression: 'price_deposit', format: 'priceWithCurrency'}," +
                        "{expression: 'price_balance', format: 'priceWithCurrency'}" +
                        "]"
                , "Document", documentDisplayResultSetProperty);
        new TranslateFunction(getI18n()).register();
        displayEntities(cartService.getCartPayments(), "[" +
                        "{expression: 'date', format: 'dateTime'}," +
                        "{expression: 'document.ref', label: 'Booking ref'}," +
                        "{expression: 'translate(method)', label: 'Method'}," +
                        "{expression: 'amount', format: 'priceWithCurrency'}," +
                        "{expression: 'translate(pending ? `PendingStatus` : successful ? `SuccessStatus` : `FailedStatus`)', label: 'Status'}" +
                        "]"
                , "MoneyTransfer", paymentDisplayResultSetProperty);
        javafx.application.Platform.runLater(() -> {
            int selectedIndex = indexOfWorkingDocument(selectedWorkingDocument);
            if (selectedIndex == -1 && cartService.getEventService() != null)
                selectedIndex = indexOfWorkingDocument(cartService.getEventService().getWorkingDocument());
            documentDisplaySelectionProperty.setValue(DisplaySelection.createSingleRowSelection(Math.max(0, selectedIndex)));
        });
    }

    private int indexOfWorkingDocument(WorkingDocument workingDocument) {
        if (workingDocument == null)
            return -1;
        return Collections.indexOf(cartService().getCartWorkingDocuments(), wd -> Entity.sameId(wd.getDocument(), workingDocument.getDocument()));
    }

    private void displayEntities(List<? extends Entity> entities, String columnsDefinition, Object classId, Property<DisplayResultSet> displayResultSetProperty) {
        displayResultSetProperty.setValue(EntityListToDisplayResultSetGenerator.createDisplayResultSet(entities, columnsDefinition
                , getDataSourceModel().getDomainModel(), classId, getI18n()));
    }

    private void syncBookingOptionsPanelIfReady() {
        if (bookingOptionsPanel != null && selectedWorkingDocument != null) {
            bookingLabel.setText(selectedWorkingDocument.getDocument().getFullName());
            bookingOptionsPanel.syncUiFromModel(selectedWorkingDocument);
        }
    }
}