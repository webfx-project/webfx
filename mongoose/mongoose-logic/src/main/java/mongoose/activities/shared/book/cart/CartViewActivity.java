package mongoose.activities.shared.book.cart;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mongoose.activities.shared.book.event.shared.BookingOptionsPanel;
import mongoose.activities.shared.logic.price.DocumentPricing;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.Document;
import mongoose.services.EventService;
import naga.framework.activity.view.impl.ViewActivityImpl;
import naga.framework.ui.controls.LayoutUtil;
import naga.framework.ui.filter.ReactiveExpressionFilter;
import naga.framework.ui.i18n.I18n;
import naga.fxdata.control.DataGrid;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.displaydata.DisplaySelection;

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
        Button makePaymentButton = i18n.translateText(new Button(), "MakePayment");
        HBox buttonBar = new HBox(20, addBookingButton, LayoutUtil.createHGrowable(), modifyBookingButton, LayoutUtil.createHGrowable(), makePaymentButton);

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

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        documentTable.displaySelectionProperty().bindBidirectional(documentDisplaySelectionProperty);
        // User outputs: the presentation model changes are transferred in the UI
        documentTable.displayResultSetProperty().bind(documentDisplayResultSetProperty);
        paymentTable.displayResultSetProperty().bind(paymentDisplayResultSetProperty);

        return LayoutUtil.createVerticalScrollPane(new VBox(20, bookingsPanel, optionsPanel, paymentsPanel, buttonBar));
    }

    @Override
    protected void fetchRouteParameters() {
        cartUuidProperty.setValue(getParameter("cartUuid"));
    }

    private ReactiveExpressionFilter documentFilter;

    @Override
    public void onResume() {
        if (documentFilter == null)
            startLogic();
        super.onResume();
    }

    @Override
    protected void refreshDataOnActive() {
        documentFilter.refreshWhenActive();
    }

    public void startLogic() {
        // Setting up the documents filter
        documentFilter = createReactiveExpressionFilter("{class: 'Document', fields:'event.id,cart.uuid', orderBy: 'creationDate desc'}")
                // Condition
                .combine(cartUuidProperty, s -> "{where: 'cart.uuid=`" + s + "`'}")
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
                .applyDomainModelRowStyle()
                .displayResultSetInto(documentDisplayResultSetProperty)
                .selectFirstRowOnFirstDisplay(documentDisplaySelectionProperty, cartUuidProperty)
                .setSelectedEntityHandler(entity -> {
                    if (entity != null) {
                        selectedDocument = (Document) entity;
                        selectedWorkingDocument = null;
                        WorkingDocument.load(selectedDocument).setHandler(ar -> {
                            if (ar.succeeded()) {
                                selectedWorkingDocument = ar.result();
                                DocumentPricing.computeDocumentPrice(selectedWorkingDocument);
                                bookingOptionsPanel.syncUiFromModel(selectedWorkingDocument);
                            }
                        });
                    }
                })
                .start();


        // Setting up the payments filter
        createReactiveExpressionFilter("{class: 'MoneyTransfer', orderBy: 'date'}")
                // Condition
                .combine(cartUuidProperty, s -> "{where: 'document.cart.uuid=`" + s + "`'}")
                //.combine("{where: 'document.cart.uuid=?cartUuid'}")
                .setExpressionColumns("[" +
                        "{expression: 'date', format: 'dateTime'}," +
                        "{expression: 'document.ref', label: 'Booking ref'}," +
                        "{expression: 'method.name', label: 'Method'}," +
                        "{expression: 'amount', format: 'price'}," +
                        "{expression: 'pending ? `Pending` : successful ? `Success` : `Failed`', label: 'Status'}" +
                        "]")
                .applyDomainModelRowStyle()
                .displayResultSetInto(paymentDisplayResultSetProperty)
                .start();
    }

    private ReactiveExpressionFilter createReactiveExpressionFilter(Object jsonOrClass) {
        return initializeReactiveExpressionFilter(new ReactiveExpressionFilter(jsonOrClass));
    }

    private ReactiveExpressionFilter initializeReactiveExpressionFilter(ReactiveExpressionFilter reactiveExpressionFilter) {
        return reactiveExpressionFilter
                .setDataSourceModel(getDataSourceModel())
                .setI18n(getI18n())
                .bindActivePropertyTo(activeProperty())
                ;
    }

}
