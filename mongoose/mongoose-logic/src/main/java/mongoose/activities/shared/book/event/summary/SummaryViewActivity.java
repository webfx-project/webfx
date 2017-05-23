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
import javafx.scene.text.TextFlow;
import mongoose.activities.shared.book.event.shared.BookingOptionsPanel;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.book.event.shared.PersonDetailsPanel;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.Cart;
import mongoose.entities.Document;
import naga.commons.util.Strings;
import naga.framework.ui.controls.DialogCallback;
import naga.framework.ui.controls.DialogUtil;
import naga.framework.ui.controls.LayoutUtil;
import naga.framework.ui.filter.ReactiveExpressionFilter;
import naga.framework.ui.i18n.I18n;
import naga.fx.properties.Properties;
import naga.fxdata.cell.collator.GridCollator;
import naga.platform.json.Json;
import naga.platform.spi.Platform;

import java.time.Instant;

import static naga.framework.ui.controls.LayoutUtil.createHGrowable;
import static naga.framework.ui.controls.LayoutUtil.setPrefSizeToInfinite;

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
        I18n i18n = getI18n();
        personDetailsPanel = new PersonDetailsPanel(getEvent(), this, borderPane);
        personDetailsPanel.setEditable(false);
        bookingOptionsPanel = new BookingOptionsPanel(i18n);

        BorderPane commentPanel = HighLevelComponents.createSectionPanel(null, null, "Comment", i18n);
        commentPanel.setCenter(i18n.translatePromptText(commentTextArea = new TextArea(), "CommentPlaceholder"));

        BorderPane termsPanel = HighLevelComponents.createSectionPanel(null, null, "TermsAndConditions", i18n);
        termsPanel.setCenter(termsCheckBox = new CheckBox());
        BorderPane.setAlignment(termsCheckBox, Pos.CENTER_LEFT);
        BorderPane.setMargin(termsCheckBox, new Insets(0, 0, 0, 10));
        agreeTCTranslationProperty = i18n.translationProperty("AgreeTC");
        Properties.runNowAndOnPropertiesChange(p -> setTermsCheckBoxText(Strings.asString(p.getValue())), agreeTCTranslationProperty);

        VBox panelsVBox = new VBox(20, bookingOptionsPanel.getOptionsPanel(), personDetailsPanel.getSectionPanel(), commentPanel, termsPanel);
        borderPane.setCenter(LayoutUtil.createVerticalScrollPane(panelsVBox));

        nextButton.disableProperty().bind(termsCheckBox.selectedProperty().not());
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
            TextFlow textFlow = new TextFlow(leftLabel, hyperlink, rightLabel);
            textFlow.setPrefHeight(hyperlink.getHeight());
            termsCheckBox.setGraphic(textFlow);
        });
    }

    private DialogCallback termsDialogCallback;

    private void showTermsDialog() {
        GridCollator termsLetterCollator = new GridCollator("first", "first");
        BorderPane entityDialogPane = new BorderPane(setPrefSizeToInfinite(termsLetterCollator));
        createReactiveExpressionFilter("{class: 'Letter', where: 'type.terms', limit: '1'}")
                .combine(eventIdProperty(), e -> "{where: 'event=" + e + "'}")
                .combine(getI18n().languageProperty(), lang -> "{columns: '[`html(" + lang + ")`]'}")
                .displayResultSetInto(termsLetterCollator.displayResultSetProperty())
                .start();
        HBox hBox = new HBox(20, createHGrowable(), newOkButton(this::closeTermsDialog), createHGrowable());
        hBox.setPadding(new Insets(20, 0, 0, 0));
        entityDialogPane.setBottom(hBox);
        termsDialogCallback = DialogUtil.showModalNodeInGoldLayout(entityDialogPane, borderPane, 0.9, 0.8);
    }

    private void closeTermsDialog() {
        termsDialogCallback.closeDialog();
        termsCheckBox.setSelected(true);
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
