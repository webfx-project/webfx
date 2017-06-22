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
import mongoose.entities.Document;
import mongoose.entities.Event;
import mongoose.services.CartService;
import mongoose.services.EventService;
import naga.commons.type.PrimType;
import naga.commons.util.collection.Collections;
import naga.framework.expression.lci.DataReader;
import naga.framework.expression.terms.function.Function;
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

        Button cancelBookingButton = i18n.translateText(new Button(), "Cancel");
        Button modifyBookingButton = i18n.translateText(new Button(), "Modify");
        Button contactUsButton = i18n.translateText(new Button(), "ContactUs");
        HBox bookingButtonBar = new HBox(20, LayoutUtil.createHGrowable(), cancelBookingButton, modifyBookingButton, contactUsButton, LayoutUtil.createHGrowable());
        optionsPanel.setBottom(LayoutUtil.createPadding(bookingButtonBar));

        Button addBookingButton = i18n.translateText(new Button(), "AddAnotherBooking");
        Button paymentButton = i18n.translateText(new Button(), "MakePayment");
        HBox bottomButtonBar = new HBox(20, addBookingButton, LayoutUtil.createHGrowable(), paymentButton);

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

        return LayoutUtil.createVerticalScrollPaneWithPadding(new VBox(20, bookingsPanel, optionsPanel, paymentsPanel, bottomButtonBar));
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
        new TranslateFunction(getI18n()).register();
        new Function<Document>("documentStatus", null, null, PrimType.STRING, true) {
            @Override
            public Object evaluate(Document document, DataReader<Document> dataReader) {
                return getI18n().instantTranslate(getDocumentStatus(document));
            }
        }.register();
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
                        "{expression: 'price_balance', format: 'priceWithCurrency'}," +
                        "{expression: 'documentStatus(this)', label: 'Status', textAlign: 'center'}" +
                        "]"
                , "Document", documentDisplayResultSetProperty);
        displayEntities(cartService.getCartPayments(), "[" +
                        "{expression: 'date', format: 'dateTime'}," +
                        "{expression: 'document.ref', label: 'Booking ref'}," +
                        "{expression: 'translate(method)', label: 'Method', textAlign: 'center'}," +
                        "{expression: 'amount', format: 'priceWithCurrency'}," +
                        "{expression: 'translate(pending ? `PendingStatus` : successful ? `SuccessStatus` : `FailedStatus`)', label: 'Status', textAlign: 'center'}" +
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
            Document selectedDocument = selectedWorkingDocument.getDocument();
            bookingLabel.setText(selectedDocument.getFullName() + " - " + getI18n().instantTranslate("Status:") + " " + getI18n().instantTranslate(getDocumentStatus(selectedDocument)));
            bookingOptionsPanel.syncUiFromModel(selectedWorkingDocument);
        }
    }

    private String getDocumentStatus(Document document) { // TODO: return a structure instead with also background and message to display in the booking panel (like in javascript version)
        if (document == null)
            return null;
        if (document.isCancelled())
            return "CancelledStatus";
        if (!document.isConfirmed()) {
            if (documentNeedsDeposit(document) && !hasPendingPayment(document))
                return "IncompleteStatus";
            return "InProgressStatus";
        }
        if (documentHasBalance(document))
            return "ConfirmedStatus";
        return "CompleteStatus";
    }

    private boolean documentNeedsDeposit(Document document) {
        return document.getPriceDeposit() < document.getPriceMinDeposit();
    }

    private boolean documentHasBalance(Document document) {
        return document.getPriceDeposit() < document.getPriceNet();
    }

    private boolean hasPendingPayment(Document document) {
        return Collections.hasAtLeastOneMatching(cartService().getCartPayments(), mt -> mt.getDocument() == document && mt.isPending());
    }

}
