package mongoose.frontend.activities.cart;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import mongoose.client.activities.shared.TermsDialog;
import mongoose.client.activities.shared.TranslateFunction;
import mongoose.client.aggregates.CartAggregate;
import mongoose.client.bookingoptionspanel.BookingOptionsPanel;
import mongoose.client.businesslogic.workingdocument.WorkingDocument;
import mongoose.client.sectionpanel.SectionPanelFactory;
import mongoose.shared.domainmodel.formatters.PriceFormatter;
import mongoose.shared.entities.Document;
import mongoose.shared.entities.History;
import mongoose.shared.entities.Mail;
import webfx.framework.expression.lci.DataReader;
import webfx.framework.expression.terms.function.Function;
import webfx.framework.orm.entity.Entities;
import webfx.framework.orm.entity.Entity;
import webfx.framework.orm.entity.UpdateStore;
import webfx.framework.orm.mapping.EntityListToDisplayResultMapper;
import webfx.framework.services.i18n.I18n;
import webfx.framework.ui.controls.dialog.DialogCallback;
import webfx.framework.ui.controls.dialog.DialogUtil;
import webfx.framework.ui.controls.dialog.GridPaneBuilder;
import webfx.framework.ui.layouts.LayoutUtil;
import webfx.fxkits.extra.control.DataGrid;
import webfx.fxkits.extra.displaydata.DisplayResult;
import webfx.fxkits.extra.displaydata.DisplaySelection;
import webfx.fxkits.extra.type.PrimType;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.uischeduler.UiScheduler;
import webfx.platforms.core.util.Strings;
import webfx.platforms.core.util.collection.Collections;

import java.util.List;

import static webfx.framework.ui.util.formatter.FormatterRegistry.registerFormatter;

/**
 * @author Bruno Salmon
 */
final class CartActivity extends CartBasedActivity {

    private final Property<DisplayResult> documentDisplayResultProperty = new SimpleObjectProperty<>();
    private final Property<DisplayResult> paymentDisplayResultProperty = new SimpleObjectProperty<>();
    // Display input & output
    private final Property<DisplaySelection> documentDisplaySelectionProperty = new SimpleObjectProperty<>();

    private Label bookingLabel;
    private BookingOptionsPanel bookingOptionsPanel;
    private WorkingDocument selectedWorkingDocument;
    private Button cancelBookingButton;
    private Button modifyBookingButton;
    private Button contactUsButton;
    private Button showPaymentsButton;
    private BorderPane optionsPanel;
    private BorderPane paymentsPanel;
    private HBox bottomButtonBar;

    @Override
    public Node buildUi() {
        BorderPane bookingsPanel = SectionPanelFactory.createSectionPanel("YourBookings");
        DataGrid documentTable = new DataGrid(); //LayoutUtil.setMinMaxHeightToPref(new DataGrid());
        documentTable.setFullHeight(true);
        bookingsPanel.setCenter(documentTable);
        optionsPanel = SectionPanelFactory.createSectionPanel(null, bookingLabel = new Label());
        bookingOptionsPanel = new BookingOptionsPanel();
        optionsPanel.setCenter(bookingOptionsPanel.getGrid());
        paymentsPanel = SectionPanelFactory.createSectionPanel("YourPayments");
        DataGrid paymentTable = new DataGrid();
        paymentTable.setFullHeight(true);
        paymentsPanel.setCenter(paymentTable);

        HBox bookingButtonBar = new HBox(20,
                LayoutUtil.createHGrowable()
                , cancelBookingButton = newCancelButton(this::cancelBooking)
                , modifyBookingButton = newButton("Modify", this::modifyBooking)
                , contactUsButton = newButton("ContactUs", this::contactUs)
                , newButton("TermsAndConditions", this::readTerms)
                , LayoutUtil.createHGrowable());
        optionsPanel.setBottom(LayoutUtil.createPadding(bookingButtonBar));

        bottomButtonBar = new HBox(20
                , newButton("AddAnotherBooking", this::addBooking)
                , LayoutUtil.createHGrowable()
                , showPaymentsButton = newButton("YourPayments", this::showPayments)
                , LayoutUtil.createHGrowable()
                , newButton("MakePayment", this::makePayment));

        LayoutUtil.setUnmanagedWhenInvisible(optionsPanel).setVisible(false);
        LayoutUtil.setUnmanagedWhenInvisible(paymentsPanel).setVisible(false);
        LayoutUtil.setUnmanagedWhenInvisible(bottomButtonBar).setVisible(false);

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        documentTable.displaySelectionProperty().bindBidirectional(documentDisplaySelectionProperty);
        // User outputs: the presentation model changes are transferred in the UI
        documentTable.displayResultProperty().bind(documentDisplayResultProperty);
        paymentTable.displayResultProperty().bind(paymentDisplayResultProperty);

        syncBookingOptionsPanelIfReady();

        return new BorderPane(LayoutUtil.createVerticalScrollPaneWithPadding(new VBox(20, bookingsPanel, optionsPanel, paymentsPanel, bottomButtonBar)));
    }

    @Override
    protected void startLogic() {
        super.startLogic();
        new TranslateFunction().register();
        new Function<Document>("documentStatus", null, null, PrimType.STRING, true) {
            @Override
            public Object evaluate(Document document, DataReader<Document> dataReader) {
                return I18n.instantTranslate(getDocumentStatus(document));
            }
        }.register();
        documentDisplaySelectionProperty.addListener((observable, oldValue, selection) -> {
            int selectedRow = selection.getSelectedRow();
            if (selectedRow != -1) {
                setSelectedWorkingDocument(Collections.get(cartService().getCartWorkingDocuments(), selectedRow));
                syncBookingOptionsPanelIfReady();
            }
        });
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
            CartAggregate cartAggregate = cartService();
            if (selectedIndex == -1 && cartAggregate.getEventAggregate() != null)
                selectedIndex = indexOfWorkingDocument(cartAggregate.getEventAggregate().getWorkingDocument());
            documentDisplaySelectionProperty.setValue(DisplaySelection.createSingleRowSelection(Math.max(0, selectedIndex)));
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
        CartAggregate cartAggregate = cartService();
        if (cartAggregate.getEventAggregate() != null)
            registerFormatter("priceWithCurrency", new PriceFormatter(getEvent()));
        displayEntities(cartAggregate.getCartDocuments(), "[" +
                        "'ref'," +
                        "'person_firstName'," +
                        "'person_lastName'," +
                        "{expression: 'price_net', format: 'priceWithCurrency'}," +
                        "{expression: 'price_deposit', format: 'priceWithCurrency'}," +
                        "{expression: 'price_balance', format: 'priceWithCurrency'}," +
                        "{expression: 'documentStatus(this)', label: 'Status', textAlign: 'center'}" +
                        "]"
                , "Document", documentDisplayResultProperty);
        displayEntities(cartAggregate.getCartPayments(), "[" +
                        "{expression: 'date', format: 'dateTime'}," +
                        "{expression: 'document.ref', label: 'Booking ref'}," +
                        "{expression: 'translate(method)', label: 'Method', textAlign: 'center'}," +
                        "{expression: 'amount', format: 'priceWithCurrency'}," +
                        "{expression: 'translate(pending ? `PendingStatus` : successful ? `SuccessfulStatus` : `FailedStatus`)', label: 'Status', textAlign: 'center'}" +
                        "]"
                , "MoneyTransfer", paymentDisplayResultProperty);
        autoSelectWorkingDocument();
    }

    private int indexOfWorkingDocument(WorkingDocument workingDocument) {
        if (workingDocument == null)
            return -1;
        return Collections.indexOf(cartService().getCartWorkingDocuments(), wd -> Entities.sameId(wd.getDocument(), workingDocument.getDocument()));
    }

    private void displayEntities(List<? extends Entity> entities, String columnsDefinition, Object classId, Property<DisplayResult> displayResultProperty) {
        displayResultProperty.setValue(EntityListToDisplayResultMapper.createDisplayResult(entities, columnsDefinition
                , getDataSourceModel().getDomainModel(), classId));
    }

    private void syncBookingOptionsPanelIfReady() {
        if (bookingOptionsPanel != null && selectedWorkingDocument != null) {
            bookingOptionsPanel.syncUiFromModel(selectedWorkingDocument);
            Document selectedDocument = selectedWorkingDocument.getDocument();
            bookingLabel.setText(selectedDocument.getFullName() + " - " + I18n.instantTranslate("Status:") + " " + I18n.instantTranslate(getDocumentStatus(selectedDocument)));
            disableCancelModifyButton(selectedDocument.isCancelled());
            updatePaymentsVisibility();
        }
    }

    private void disableCancelModifyButton(boolean disable) {
        cancelBookingButton.setDisable(disable);
        modifyBookingButton.setDisable(disable);
        contactUsButton.setDisable(disable || selectedWorkingDocument == null || selectedWorkingDocument.getDocument().getEmail() == null);
    }

    private void updatePaymentsVisibility() {
        if (showPaymentsButton != null) {
            if (Collections.isEmpty(cartService().getCartPayments())) {
                showPaymentsButton.setVisible(false);
                paymentsPanel.setVisible(false);
            } else
                showPaymentsButton.setVisible(!paymentsPanel.isVisible());
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
        return Collections.anyMatch(cartService().getCartPayments(), mt -> mt.getDocument() == document && mt.isPending());
    }

    private void modifyBooking() {
        // Commented TODO new RouteToOptionsRequest(selectedWorkingDocument, getHistory()).execute();
        setSelectedWorkingDocument(null);
    }

    private void cancelBooking() {
        DialogUtil.showModalNodeInGoldLayout(new GridPaneBuilder()
                        .addNodeFillingRow(newLabel("BookingCancellation"))
                        .addNodeFillingRow(newLabel("ConfirmBookingCancellation"))
                        .addButtons("YesBookingCancellation", dialogCallback -> {
                                    disableCancelModifyButton(true);
                                    Document selectedDocument = selectedWorkingDocument.getDocument();
                                    UpdateStore updateStore = UpdateStore.createAbove(selectedDocument.getStore());
                                    Document updatedDocument = updateStore.updateEntity(selectedDocument);
                                    updatedDocument.setCancelled(true);
                                    updateStore.executeUpdate().setHandler(ar -> {
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
        TextField subjectTextField = newTextFieldWithPrompt("SubjectPlaceholder");
        TextArea bodyTextArea = newTextAreaWithPrompt("YourMessagePlaceholder");
        DialogUtil.showModalNodeInGoldLayout(new GridPaneBuilder()
                        .addNodeFillingRow(SectionPanelFactory.createSectionPanel("Subject", subjectTextField))
                        .addNodeFillingRowAndHeight(SectionPanelFactory.createSectionPanel("YourMessage", bodyTextArea))
                        .addButtons("Send", dialogCallback -> {
                                    Document doc = selectedWorkingDocument.getDocument();
                                    UpdateStore updateStore = UpdateStore.createAbove(doc.getStore());
                                    Mail mail = updateStore.insertEntity(Mail.class);
                                    mail.setDocument(doc);
                                    mail.setFromName(doc.getFullName());
                                    mail.setFromEmail(doc.getEmail());
                                    mail.setSubject("[" + doc.getRef() + "] " + subjectTextField.getText());
                                    String cartUrl = getHistory().getCurrentLocation().getPath();
                                    // building mail content
                                    String content = bodyTextArea.getText()
                                            + "\n-----\n"
                                            + doc.getEvent().getName() + " - #" + doc.getRef()
                                            + " - <a href=mailto:'" + doc.getEmail() + "'>" + doc.getFullName() + "</a>\n"
                                            + "<a href='" + cartUrl + "'>" + cartUrl + "</a>";
                                    content = Strings.replaceAll(content, "\r", "<br/>");
                                    content = Strings.replaceAll(content, "\n", "<br/>");
                                    content = "<html>" + content + "</html>";
                                    // setting mail content
                                    mail.setContent(content);
                                    mail.setOut(false); // indicate that this mail is not an outgoing email (sent to booker) but an ingoing mail (sent to registration team)
                                    History history = updateStore.insertEntity(History.class); // new server history entry
                                    history.setDocument(doc);
                                    history.setMail(mail);
                                    history.setUsername("online");
                                    history.setComment("Sent '" + subjectTextField.getText() + "'");
                                    updateStore.executeUpdate().setHandler(ar -> {
                                        if (ar.failed())
                                            Logger.log("Error", ar.cause());
                                        else {
                                            dialogCallback.closeDialog();
                                        }
                                    });
                                },
                                "Cancel", DialogCallback::closeDialog)
                , (Pane) getNode(), 0.9, 0.8);
    }

    private void readTerms() {
        new TermsDialog(getEventId(), getDataSourceModel(), (Pane) getNode()).show();
    }

    private void addBooking() {
        // temporary commented (dependency cycle) TODO new RouteToFeesRequest(getEventId(), getHistory()).execute();
    }

    private void showPayments() {
        paymentsPanel.setVisible(true);
        showPaymentsButton.setVisible(false);
    }

    private void makePayment() {
        // Commented for now TODO new RouteToPaymentRequest(getCartUuid(), getHistory()).execute();
    }
}
