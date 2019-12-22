package mongoose.frontend.activities.summary;

import javafx.application.Platform;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import mongoose.client.controls.bookingcalendar.BookingCalendar;
import mongoose.client.controls.bookingoptionspanel.BookingOptionsPanel;
import mongoose.client.activity.bookingprocess.BookingProcessActivity;
import mongoose.client.businessdata.workingdocument.WorkingDocument;
import mongoose.client.businessdata.workingdocument.WorkingDocumentSubmitter;
import mongoose.client.controls.personaldetails.PersonalDetailsPanel;
import mongoose.client.controls.sectionpanel.SectionPanelFactory;
import mongoose.client.validation.MongooseValidationSupport;
import mongoose.frontend.operations.cart.RouteToCartRequest;
import mongoose.shared.entities.Cart;
import mongoose.shared.entities.Document;
import webfx.framework.client.services.i18n.I18n;
import webfx.kit.util.properties.Properties;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.Strings;

/**
 * @author Bruno Salmon
 */
final class SummaryActivity extends BookingProcessActivity {

    private BookingOptionsPanel bookingOptionsPanel;
    private Node bookingCalendarSection;
    private BookingCalendar bookingCalendar;
    private PersonalDetailsPanel personalDetailsPanel;
    private TextArea commentTextArea;
    private CheckBox termsCheckBox;
    private ObservableStringValue agreeTCTranslationProperty; // to avoid GC
    private final MongooseValidationSupport validationSupport = new MongooseValidationSupport();

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        bookingOptionsPanel = new BookingOptionsPanel();
        bookingCalendar = new BookingCalendar(false);
        bookingCalendarSection = SectionPanelFactory.createBookingCalendarSection(bookingCalendar);
        personalDetailsPanel = new PersonalDetailsPanel(getEvent(), this, pageContainer);
        personalDetailsPanel.setEditable(false);

        BorderPane commentPanel = SectionPanelFactory.createSectionPanel("Comment");
        commentPanel.setCenter(commentTextArea = newTextArea("Comment")); // Will set the prompt

        BorderPane termsPanel = SectionPanelFactory.createSectionPanel("TermsAndConditions");
        termsPanel.setCenter(termsCheckBox = new CheckBox());
        BorderPane.setAlignment(termsCheckBox, Pos.CENTER_LEFT);
        BorderPane.setMargin(termsCheckBox, new Insets(10));
        agreeTCTranslationProperty = I18n.i18nTextProperty("AgreeTC");
        Properties.runNowAndOnPropertiesChange(p -> setTermsCheckBoxText(Strings.toSafeString(p.getValue())), agreeTCTranslationProperty);

        verticalStack.getChildren().setAll(
                bookingOptionsPanel.getOptionsPanel(),
                bookingCalendarSection,
                personalDetailsPanel.getSectionPanel(),
                commentPanel,
                termsPanel,
                nextButton
        );

        validationSupport.addValidationRule(termsCheckBox.selectedProperty(), termsCheckBox, "Please read and accept the terms and conditions");
    }

    private void setTermsCheckBoxText(String text) {
        Platform.runLater(() -> {
            int aStartPos = text.indexOf("<a");
            int aTextStart = text.indexOf(">", aStartPos) + 1;
            int aTextEnd = text.indexOf("</a>", aTextStart);
            String leftText = text.substring(0, aStartPos - 1);
            String hyperText = text.substring(aTextStart, aTextEnd);
            String rightText = text.substring(aTextEnd + 4);
            Label leftLabel = new Label(leftText);
            Hyperlink hyperlink = new Hyperlink(hyperText);
            hyperlink.setOnAction(e -> showTermsDialog());
            Label rightLabel = new Label(rightText);
/*
            TextFlow textFlow = new TextFlow(leftLabel, hyperlink, rightLabel);
            textFlow.setPrefHeight(hyperlink.getHeight());
            termsCheckBox.setGraphic(textFlow);
*/
            FlowPane textContainer = new FlowPane(leftLabel, hyperlink, rightLabel);
            textContainer.setAlignment(Pos.CENTER_LEFT);
            termsCheckBox.setGraphic(textContainer);
        });
    }

    private void showTermsDialog() {
        //new TermsDialog(getEventId(), getDataSourceModel(), getI18n(), pageContainer).setOnClose(() -> termsCheckBox.setSelected(true)).show();
        // temporary removed new RouteToTermsRequest(getEventId(), getHistory()).execute();
    }

    private void syncUiFromModel() {
        WorkingDocument workingDocument = getEventActiveWorkingDocument();
        if (workingDocument != null) {
            bookingCalendar.createOrUpdateCalendarGraphicFromWorkingDocument(workingDocument, true);
            bookingOptionsPanel.syncUiFromModel(workingDocument);
            personalDetailsPanel.syncUiFromModel(workingDocument.getDocument());
        }
    }

    private void syncModelFromUi() {
        WorkingDocument workingDocument = getEventActiveWorkingDocument();
        if (workingDocument != null)
            personalDetailsPanel.syncModelFromUi(workingDocument.getDocument());
    }

    @Override
    public void onResume() {
        super.onResume();
        syncUiFromModel();
    }

    @Override
    public void onPause() {
        super.onPause();
        syncModelFromUi();
    }

    @Override
    protected void onNextButtonPressed(ActionEvent event) {
        if (validationSupport.isValid())
            WorkingDocumentSubmitter.submit(getEventActiveWorkingDocument(), commentTextArea.getText()).setHandler(ar -> {
                if (ar.failed())
                    Logger.log("Error submitting booking", ar.cause());
                else {
                    Document document = ar.result();
                    Cart cart = document.getCart();
                    if (cart == null) {
                        WorkingDocument workingDocument = getEventActiveWorkingDocument();
                        if (workingDocument.getLoadedWorkingDocument() != null)
                            workingDocument = workingDocument.getLoadedWorkingDocument();
                        document = workingDocument.getDocument();
                        cart = document.getCart();
                    }
                    new RouteToCartRequest(cart.getUuid(), getHistory()).execute();
                }
            });
    }
}
