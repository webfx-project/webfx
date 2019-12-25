package mongoose.frontend.activities.cart;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import mongoose.client.controls.bookingoptionspanel.BookingOptionsPanel;
import mongoose.client.businessdata.workingdocument.ActiveWorkingDocumentsByEventStore;
import mongoose.client.businessdata.workingdocument.WorkingDocument;
import mongoose.client.businessdata.workingdocument.WorkingDocumentsByCartStore;
import mongoose.client.controls.sectionpanel.SectionPanelFactory;
import mongoose.client.util.functions.TranslateFunction;
import mongoose.frontend.activities.cart.base.CartBasedActivity;
import mongoose.frontend.operations.contactus.RouteToContactUsRequest;
import mongoose.frontend.operations.options.RouteToOptionsRequest;
import mongoose.frontend.operations.payment.RouteToPaymentRequest;
import mongoose.frontend.operations.startbooking.RouteToStartBookingRequest;
import mongoose.shared.entities.formatters.EventPriceFormatter;
import mongoose.shared.domainmodel.formatters.PriceFormatter;
import mongoose.shared.entities.Document;
import mongoose.shared.entities.Event;
import webfx.framework.client.services.i18n.I18n;
import webfx.framework.client.ui.action.Action;
import webfx.framework.client.ui.action.ActionBinder;
import webfx.framework.client.ui.action.impl.WritableAction;
import webfx.framework.client.ui.controls.dialog.DialogCallback;
import webfx.framework.client.ui.controls.dialog.DialogUtil;
import webfx.framework.client.ui.controls.dialog.GridPaneBuilder;
import webfx.extras.flexbox.FlexBox;
import webfx.framework.client.ui.util.layout.LayoutUtil;
import webfx.framework.shared.orm.expression.lci.DomainReader;
import webfx.framework.shared.orm.expression.terms.function.Function;
import webfx.framework.shared.orm.entity.Entities;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.UpdateStore;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.EntitiesToVisualResultMapper;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.extras.visual.controls.grid.SkinnedVisualGrid;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualSelection;
import webfx.extras.type.PrimType;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.Arrays;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.collection.Collections;

import java.util.List;

import static webfx.framework.shared.orm.domainmodel.formatter.FormatterRegistry.registerFormatter;

/**
 * @author Bruno Salmon
 */
final class CartActivity extends CartBasedActivity {

    // Instantiating the different actions
    private final WritableAction modifyBookingAction = new WritableAction(newAction("<<Modify", "{url: 'images/svg/mono/pen.svg', width: 16, height: 16}", this::modifyBooking), "*");
    private final WritableAction cancelBookingAction = new WritableAction(newAction("Cancel", "{url: 'images/svg/mono/cancel-red.svg', width: 16, height: 16}", this::cancelBooking), "*");
    private final WritableAction contactUsAction     = new WritableAction(newAction("ContactUs>>", "{url: 'images/svg/mono/mail.svg', width: 16, height: 16}", this::contactUs), "*");
    //private final Action termsAction                 = MongooseActions.newVisitTermsAndConditionsAction(this::readTerms);
    private final WritableAction showPaymentsAction  = new WritableAction(newAction("YourPayments", this::showPayments), "*");
    private final Action addAnotherBookingAction     = newAction("<<AddAnotherBooking", "{url: 'images/svg/mono/plus-circle-green.svg', width: 32, height: 32}", this::addAnotherBooking);
    private final Action makePaymentAction           = newAction("MakePayment>>", "{url: 'images/svg/mono/pay-circle.svg', width: 32, height: 32}", this::makePayment);
    private final Action explainStatusAction         = newAction(null, "{url: 'images/svg/mono/help-circle-blue.svg', width: 32, height: 32}", this::explainStatus);

    private final Property<VisualResult> documentVisualResultProperty = new SimpleObjectProperty<>();
    private final Property<VisualResult> paymentVisualResultProperty = new SimpleObjectProperty<>();
    // Display input & output
    private final Property<VisualSelection> documentVisualSelectionProperty = new SimpleObjectProperty<>();

    private Label bookingLabel;
    private BookingOptionsPanel bookingOptionsPanel;
    private WorkingDocument selectedWorkingDocument;
    private BorderPane optionsPanel;
    private BorderPane paymentsPanel;
    private FlexBox bottomButtonBar;

    @Override
    public Node buildUi() {
        BorderPane bookingsPanel = SectionPanelFactory.createSectionPanel("YourBookings");
        VisualGrid documentTable = new SkinnedVisualGrid();
        documentTable.setFullHeight(true);
        bookingsPanel.setCenter(documentTable);
        optionsPanel = SectionPanelFactory.createSectionPanelWithHeaderNodes(bookingLabel = new Label(), LayoutUtil.createHGrowable(), ActionBinder.getAndBindActionIcon(explainStatusAction));
        bookingOptionsPanel = new BookingOptionsPanel();
        optionsPanel.setCenter(bookingOptionsPanel.getGrid());
        paymentsPanel = SectionPanelFactory.createSectionPanel("YourPayments");
        VisualGrid paymentTable = new VisualGrid();
        paymentTable.setFullHeight(true);
        paymentsPanel.setCenter(paymentTable);

        FlexBox bookingButtonBar = createFlexButtonBar(modifyBookingAction, cancelBookingAction, contactUsAction);

        optionsPanel.setBottom(LayoutUtil.createPadding(bookingButtonBar));

        bottomButtonBar = createFlexButtonBar(addAnotherBookingAction, showPaymentsAction, makePaymentAction);

        LayoutUtil.setUnmanagedWhenInvisible(optionsPanel).setVisible(false);
        LayoutUtil.setUnmanagedWhenInvisible(paymentsPanel).setVisible(false);
        LayoutUtil.setUnmanagedWhenInvisible(bottomButtonBar).setVisible(false);

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        documentTable.visualSelectionProperty().bindBidirectional(documentVisualSelectionProperty);
        // User outputs: the presentation model changes are transferred in the UI
        documentTable.visualResultProperty().bind(documentVisualResultProperty);
        paymentTable.visualResultProperty().bind(paymentVisualResultProperty);

        // Applying the css background of the event if provided and if ui is ready
        UiScheduler.scheduleDeferred(this::applyEventCssBackgroundIfProvided);

        return new BorderPane(LayoutUtil.createVerticalScrollPaneWithPadding(new VBox(20, bookingsPanel, optionsPanel, paymentsPanel, bottomButtonBar)));
    }

    private FlexBox createFlexButtonBar(Action... actions) {
        // not compiling with GWT return new FlexBox(20, 10, Arrays.map(actions, action -> LayoutUtil.setMaxWidthToInfinite(LayoutUtil.setMinWidthToPref(newButton(action))), Node[]::new));
        FlexBox flexButtonBar = new FlexBox(20, 10);
        Arrays.forEach(actions, action -> flexButtonBar.getChildren().add(createFlexButton(action)));
        return flexButtonBar;
    }

    private Button createFlexButton(Action action) {
        return LayoutUtil.setMaxWidthToInfinite(LayoutUtil.setMinWidthToPref(action == makePaymentAction ? newGreenButton(action) : action == addAnotherBookingAction ? newTransparentButton(action) : newButton(action)));
    }

    @Override
    protected void startLogic() {
        super.startLogic();
        new TranslateFunction().register();
        new Function<Document>("documentStatus", PrimType.STRING, true, false) {
            @Override
            public Object evaluate(Document document, DomainReader<Document> domainReader) {
                return I18n.getI18nText(getDocumentStatus(document));
            }
        }.register();
        documentVisualSelectionProperty.addListener((observable, oldValue, selection) -> {
            int selectedRow = selection == null ? -1 : selection.getSelectedRow();
            if (selectedRow != -1) {
                onCartWorkingDocuments().setHandler(ar -> UiScheduler.runInUiThread(() -> {
                    setSelectedWorkingDocument(Collections.get(getCartWorkingDocuments(), selectedRow));
                    displayBookingOptions();
                }));
            }
        });
    }

    private Future<List<WorkingDocument>> onCartWorkingDocuments() {
        return cartAggregate().onCartDocuments().map(this::getCartWorkingDocuments);
    }

    private List<WorkingDocument> getCartWorkingDocuments() {
        return WorkingDocumentsByCartStore.getCartWorkingDocuments(cartAggregate());
    }

    private void setSelectedWorkingDocument(WorkingDocument selectedWorkingDocument) {
        this.selectedWorkingDocument = selectedWorkingDocument;
        if (bottomButtonBar != null) {
            boolean visible = selectedWorkingDocument != null;
            optionsPanel.setVisible(visible);
            bottomButtonBar.setVisible(visible);
        }
    }

    private void autoSelectWorkingDocument() {
        UiScheduler.runInUiThread(() -> {
            int selectedIndex = indexOfWorkingDocument(selectedWorkingDocument);
            if (selectedIndex == -1 && eventAggregate() != null)
                selectedIndex = indexOfWorkingDocument(ActiveWorkingDocumentsByEventStore.getEventActiveWorkingDocument(getEvent()));
            documentVisualSelectionProperty.setValue(VisualSelection.createSingleRowSelection(Math.max(0, selectedIndex)));
            updatePaymentsVisibility();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        autoSelectWorkingDocument();
    }

    @Override
    protected void onCartLoaded() {
        super.onCartLoaded(); // Applying the css background of the event if provided
        Event event = getEvent();
        if (event != null) {
            PriceFormatter priceFormatter = new EventPriceFormatter(event);
            registerFormatter("priceWithCurrency", priceFormatter);
            new Function("formatPrice", PrimType.STRING, true, false) {
                @Override
                public Object evaluate(Object argument, DomainReader domainReader) {
                    return priceFormatter.formatValue(argument);
                }
            }.register();
            displayCartDocuments();
            displayCartPayments();
            if (selectedWorkingDocument == null)
                autoSelectWorkingDocument();
        }
    }

    private void displayCartDocuments() {
        displayEntities(cartAggregate().getCartDocuments(), "[" +
                        "{expression: 'ref', prefWidth: null}," +
                        "'person_name'," +
                        "{expression: 'formatPrice(price_deposit) + ` / ` + formatPrice(price_net)', label: 'Deposit', textAlign: 'right'}," +
/*
                        "{expression: 'price_net', format: 'priceWithCurrency'}," +
                        "{expression: 'price_deposit', format: 'priceWithCurrency'}," +
                        "{expression: 'price_balance', format: 'priceWithCurrency'}," +
*/
                        "{expression: 'documentStatus(this)', label: 'Status', textAlign: 'center'}" +
                        "]"
                , "Document", documentVisualResultProperty);
    }

    private void displayCartPayments() {
        displayEntities(cartAggregate().getCartPayments(), "[" +
                        "{expression: 'date', format: 'dateTime'}," +
                        "{expression: 'document.ref', label: 'Booking ref'}," +
                        "{expression: 'translate(method)', label: 'Method', textAlign: 'center'}," +
                        "{expression: 'amount', format: 'priceWithCurrency'}," +
                        "{expression: 'translate(pending ? `PendingStatus` : successful ? `SuccessfulStatus` : `FailedStatus`)', label: 'Status', textAlign: 'center'}" +
                        "]"
                , "MoneyTransfer", paymentVisualResultProperty);
    }

    private void displayEntities(List<? extends Entity> entities, String columnsDefinition, Object classId, Property<VisualResult> visualResultProperty) {
        visualResultProperty.setValue(EntitiesToVisualResultMapper.mapEntitiesToVisualResult(entities, columnsDefinition
                , getDataSourceModel().getDomainModel(), classId));
    }

    private void displayBookingOptions() {
        if (bookingOptionsPanel != null && selectedWorkingDocument != null) {
            bookingOptionsPanel.syncUiFromModel(selectedWorkingDocument);
            Document selectedDocument = selectedWorkingDocument.getDocument();
            bookingLabel.setText(selectedDocument.getFullName() + " - " + I18n.getI18nText("Status:") + " " + I18n.getI18nText(getDocumentStatus(selectedDocument)));
            disableBookinOptionsButtons(selectedDocument.isCancelled());
            updatePaymentsVisibility();
        }
    }

    private int indexOfWorkingDocument(WorkingDocument workingDocument) {
        if (workingDocument == null)
            return -1;
        return Collections.indexOf(getCartWorkingDocuments(), wd -> Entities.sameId(wd.getDocument(), workingDocument.getDocument()));
    }

    private void disableBookinOptionsButtons(boolean disable) {
        cancelBookingAction.setDisabled(disable);
        modifyBookingAction.setDisabled(disable);
        contactUsAction.setDisabled(disable || selectedWorkingDocument == null || selectedWorkingDocument.getDocument().getEmail() == null);
    }

    private void updatePaymentsVisibility() {
        if (paymentsPanel != null) {
            if (Collections.isEmpty(cartAggregate().getCartPayments())) {
                showPaymentsAction.setVisible(false);
                paymentsPanel.setVisible(false);
            } else
                showPaymentsAction.setVisible(!paymentsPanel.isVisible());
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
        return Collections.anyMatch(cartAggregate().getCartPayments(), mt -> mt.getDocument() == document && mt.isPending());
    }

    private void explainStatus() {
        Logger.log("Explaining status");
    }

    private void modifyBooking() {
        new RouteToOptionsRequest(selectedWorkingDocument, getHistory()).execute();
        setSelectedWorkingDocument(null);
    }

    private void cancelBooking() {
        DialogUtil.showModalNodeInGoldLayout(new GridPaneBuilder()
                        .addNodeFillingRow(newLabel("BookingCancellation"))
                        .addNodeFillingRow(newLabel("ConfirmBookingCancellation"))
                        .addButtons("YesBookingCancellation", dialogCallback -> {
                                    disableBookinOptionsButtons(true);
                                    Document selectedDocument = selectedWorkingDocument.getDocument();
                                    UpdateStore updateStore = UpdateStore.createAbove(selectedDocument.getStore());
                                    Document updatedDocument = updateStore.updateEntity(selectedDocument);
                                    updatedDocument.setCancelled(true);
                                    updateStore.submitChanges().setHandler(ar -> {
                                        if (ar.succeeded()) {
                                            reloadCart();
                                            dialogCallback.closeDialog();
                                        }
                                    });
                                },
                                "NoBookingCancellation", DialogCallback::closeDialog)
                , (Pane) getNode());
    }

    private void contactUs() {
        new RouteToContactUsRequest(selectedWorkingDocument.getDocument(), getHistory()).execute();
    }

/*
    private void readTerms() {
        //new TermsDialog(getEventId(), getDataSourceModel(), (Pane) getNode()).show();
        new RouteToTermsRequest(getEventId(), getHistory()).execute();
    }
*/

    private void addAnotherBooking() {
        new RouteToStartBookingRequest(getEventId(), getHistory()).execute();
    }

    private void makePayment() {
        new RouteToPaymentRequest(getCartUuid(), getHistory()).execute();
    }

    private void showPayments() {
        paymentsPanel.setVisible(true);
        showPaymentsAction.setVisible(false);
    }
}
