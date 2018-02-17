package mongoose.activities.shared.book.event.person;

import javafx.application.Platform;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.book.event.shared.LoginPanel;
import mongoose.activities.shared.book.event.shared.PersonDetailsPanel;
import mongoose.activities.shared.book.event.summary.SummaryRooting;
import mongoose.activities.shared.logic.work.WorkingDocument;
import naga.framework.ui.session.UiSession;
import naga.framework.ui.graphic.background.BackgroundUtil;
import naga.framework.ui.graphic.border.BorderUtil;
import naga.framework.ui.layouts.LayoutUtil;
import naga.fx.properties.Properties;
import naga.util.Numbers;

/**
 * @author Bruno Salmon
 */
class PersonViewActivity extends BookingProcessViewActivity {

    private PersonDetailsPanel personDetailsPanel;

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        BorderPane accountTopNote = new BorderPane();
        Text accountTopText = newText("AccountTopNote");
        accountTopText.setFill(Color.web("#8a6d3b"));
        TextFlow textFlow = new TextFlow(accountTopText);
        textFlow.maxWidthProperty().bind(
                // borderPane.widthProperty().subtract(100) // not yet emulated for GWT
                Properties.compute(pageContainer.widthProperty(), width -> Numbers.toDouble(width.doubleValue() - 100))
        );
        accountTopNote.setLeft(textFlow);
        Button closeButton = newButton(newActionBuilder(REMOVE_ACTION_KEY).removeText().setActionHandler(() -> verticalStack.getChildren().remove(accountTopNote)));
        closeButton.setBorder(BorderUtil.transparentBorder());
        closeButton.setBackground(BackgroundUtil.TRANSPARENT_BACKGROUND);
        accountTopNote.setRight(closeButton);
        accountTopNote.setBackground(BackgroundUtil.newVerticalLinearGradientBackground("rgba(244, 217, 132, 0.8)", "rgba(235, 192, 120, 0.8)", 5));
        accountTopNote.setPadding(new Insets(10));
        accountTopNote.setBorder(BorderUtil.newWebColorBorder("#ebc078", 5));
        ToggleGroup accountToggleGroup = new ToggleGroup();
        FlowPane accountTabs = new FlowPane(new Button(null, newRadioButton("IDontHaveAnAccount", accountToggleGroup)), new Button(null, newRadioButton("IAlreadyHaveAnAccount", accountToggleGroup)));
        UiSession uiSession = getUiSession();
        ObservableBooleanValue loggedInProperty = loggedInProperty();
        ObservableBooleanValue notLoggedIn = BooleanExpression.booleanExpression(loggedInProperty).not();
        LoginPanel loginPanel = new LoginPanel(uiSession, getI18n(), getUiRouter().getAuthenticationService());
        personDetailsPanel = new PersonDetailsPanel(getEvent(), this, pageContainer, uiSession);
        Node[] tabContents = {new VBox(10, personDetailsPanel.getSectionPanel(), nextButton), loginPanel.getNode() };
        BorderPane accountPane = new BorderPane();
        accountToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            Node displayedNode = tabContents[accountToggleGroup.getToggles().indexOf(newValue)];
            accountPane.setCenter(displayedNode);
            if (displayedNode == loginPanel.getNode())
                loginPanel.prepareShowing();
        } );
        accountToggleGroup.selectToggle(accountToggleGroup.getToggles().get(0));
        Properties.runNowAndOnPropertiesChange(() -> {
            if (loggedInProperty.getValue())
                Platform.runLater(() -> accountToggleGroup.selectToggle(accountToggleGroup.getToggles().get(0)));
        }, loggedInProperty);
        verticalStack.getChildren().setAll(
                LayoutUtil.setUnmanagedWhenInvisible(accountTopNote, notLoggedIn),
                new VBox(LayoutUtil.setUnmanagedWhenInvisible(accountTabs, notLoggedIn)
                        , accountPane)
                );
        syncUiFromModel();
    }

    private void syncUiFromModel() {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null)
            personDetailsPanel.syncUiFromModel(workingDocument.getDocument());
    }

    private void syncModelFromUi() {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null)
            personDetailsPanel.syncModelFromUi(workingDocument.getDocument());
    }

    @Override
    protected void onNextButtonPressed(ActionEvent event) {
        if (personDetailsPanel.isValid())
            SummaryRooting.routeUsingEventId(getEventId(), getHistory());
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
        // Clearing the computed price cache on leaving this page in case a personal detail affecting the price (such as age) has been changed
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null)
            workingDocument.clearComputedPrice();
    }
}
