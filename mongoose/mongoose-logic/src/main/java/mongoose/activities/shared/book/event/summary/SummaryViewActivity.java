package mongoose.activities.shared.book.event.summary;

import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mongoose.activities.shared.book.event.shared.BookingOptionsPanel;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.book.event.shared.PersonDetailsPanel;
import mongoose.activities.shared.book.event.shared.TermsDialog;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.Cart;
import mongoose.entities.Document;
import naga.util.Strings;
import naga.framework.ui.controls.LayoutUtil;
import naga.fx.properties.Properties;
import naga.platform.json.Json;
import naga.platform.spi.Platform;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public class SummaryViewActivity extends BookingProcessViewActivity {

    private PersonDetailsPanel personDetailsPanel;
    private BookingOptionsPanel bookingOptionsPanel;
    private TextArea commentTextArea;
    private CheckBox termsCheckBox;
    private Property<String> agreeTCTranslationProperty; // to avoid GC

    public SummaryViewActivity() {
        super(null);
    }

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        personDetailsPanel = new PersonDetailsPanel(getEvent(), this, borderPane);
        personDetailsPanel.setEditable(false);
        bookingOptionsPanel = new BookingOptionsPanel(getI18n());

        BorderPane commentPanel = createSectionPanel("Comment");
        commentPanel.setCenter(commentTextArea = newTextAreaWithPrompt("CommentPlaceholder"));

        BorderPane termsPanel = createSectionPanel("TermsAndConditions");
        termsPanel.setCenter(termsCheckBox = new CheckBox());
        BorderPane.setAlignment(termsCheckBox, Pos.CENTER_LEFT);
        BorderPane.setMargin(termsCheckBox, new Insets(0, 0, 0, 10));
        agreeTCTranslationProperty = translationProperty("AgreeTC");
        Properties.runNowAndOnPropertiesChange(p -> setTermsCheckBoxText(Strings.toSafeString(p.getValue())), agreeTCTranslationProperty);

        VBox panelsVBox = new VBox(20, bookingOptionsPanel.getOptionsPanel(), personDetailsPanel.getSectionPanel(), commentPanel, termsPanel);
        borderPane.setCenter(LayoutUtil.createVerticalScrollPaneWithPadding(panelsVBox));

        nextButton.disableProperty().bind(
                // termsCheckBox.selectedProperty().not() // Doesn't compile with GWT
                Properties.compute(termsCheckBox.selectedProperty(), value -> !value) // GWT compatible
        );
    }

    private void setTermsCheckBoxText(String text) {
        javafx.application.Platform.runLater(() -> {
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
            termsCheckBox.setGraphic(new HBox(leftLabel, hyperlink, rightLabel));
        });
    }

    private void showTermsDialog() {
        new TermsDialog(getEventId(), getDataSourceModel(), getI18n(), borderPane).setOnClose(() -> termsCheckBox.setSelected(true)).show();
    }

    private void syncUiFromModel() {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null) {
            bookingOptionsPanel.syncUiFromModel(workingDocument);
            personDetailsPanel.syncUiFromModel(workingDocument.getDocument());
        }
    }

    private void syncModelFromUi() {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null)
            personDetailsPanel.syncModelFromUi(workingDocument.getDocument());
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
        getWorkingDocument().submit(commentTextArea.getText()).setHandler(ar -> {
            if (ar.failed())
                Platform.log("Error submitting booking", ar.cause());
            else {
                Document document = ar.result();
                Cart cart = document.getCart();
                if (cart == null) {
                    WorkingDocument workingDocument = getWorkingDocument();
                    if (workingDocument.getLoadedWorkingDocument() != null)
                        workingDocument = workingDocument.getLoadedWorkingDocument();
                    document = workingDocument.getDocument();
                    cart = document.getCart();
                }
                String path = cart != null ? "/book/cart/" + cart.getUuid() : "/event/" + getEvent().getPrimaryKey() + "/bookings";
                getHistory().push(path, Json.createObject().set("refresh", Instant.now()));
            }
        });
    }
}
