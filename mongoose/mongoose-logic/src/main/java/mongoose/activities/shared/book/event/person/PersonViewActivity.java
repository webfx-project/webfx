package mongoose.activities.shared.book.event.person;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
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
import mongoose.actions.MongooseIcons;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.book.event.shared.LoginPanel;
import mongoose.activities.shared.book.event.shared.PersonDetailsPanel;
import mongoose.activities.shared.logic.work.WorkingDocument;
import naga.util.Numbers;
import naga.framework.ui.action.Action;
import naga.framework.ui.auth.UiUser;
import naga.framework.ui.controls.BackgroundUtil;
import naga.framework.ui.controls.BorderUtil;
import naga.framework.ui.layouts.LayoutUtil;
import naga.fx.properties.Properties;

/**
 * @author Bruno Salmon
 */
public class PersonViewActivity extends BookingProcessViewActivity {

    public PersonViewActivity() {
        super("summary");
    }

    private PersonDetailsPanel personDetailsPanel;

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        BorderPane accountTopNote = new BorderPane();
        Text accountTopText = newText("AccountTopNote");
        accountTopText.setFill(Color.web("#8a6d3b"));
        TextFlow textFlow = new TextFlow(accountTopText);
        textFlow.maxWidthProperty().bind(
                // borderPane.widthProperty().subtract(100) // doesn't compile with GWT
                Properties.compute(pageContainer.widthProperty(), width -> Numbers.toDouble(width.doubleValue() - 100))
        );
        accountTopNote.setLeft(textFlow);
        Button closeButton = Action.create(null, MongooseIcons.removeIcon16JsonUrl, e -> verticalStack.getChildren().remove(accountTopNote)).toButton(getI18n());
        closeButton.setBorder(BorderUtil.transparentBorder());
        closeButton.setBackground(BackgroundUtil.TRANSPARENT_BACKGROUND);
        accountTopNote.setRight(closeButton);
        accountTopNote.setBackground(BackgroundUtil.newVerticalLinearGradientBackground("rgba(244, 217, 132, 0.8)", "rgba(235, 192, 120, 0.8)", 5));
        accountTopNote.setPadding(new Insets(10));
        accountTopNote.setBorder(BorderUtil.newWebColorBorder("#ebc078", 5));
        ToggleGroup accountToggleGroup = new ToggleGroup();
        FlowPane accountTabs = new FlowPane(new Button(null, newRadioButton("IDontHaveAnAccount", accountToggleGroup)), new Button(null, newRadioButton("IAlreadyHaveAnAccount", accountToggleGroup)));
        UiUser uiUser = getUiRouter().getUiUser();
        ObservableValue<Boolean> loggedInProperty = uiUser.loggedInProperty();
        ObservableValue<Boolean> notLoggedIn = Properties.not(loggedInProperty);
        LoginPanel loginPanel = new LoginPanel(uiUser, getI18n(), getUiRouter().getAuthService());
        personDetailsPanel = new PersonDetailsPanel(getEvent(), this, pageContainer, uiUser);
        Node[] tabContents = {new VBox(10, personDetailsPanel.getSectionPanel(), nextButton), loginPanel.getNode() };
        BorderPane accountPane = new BorderPane();
        accountToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            Node displayedNode = tabContents[accountToggleGroup.getToggles().indexOf(newValue)];
            accountPane.setCenter(displayedNode);
            if (displayedNode == loginPanel.getNode())
                loginPanel.prepareShowing();
        } );
        accountToggleGroup.selectToggle(accountToggleGroup.getToggles().get(0));
        Properties.runNowAndOnPropertiesChange(p -> {
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
            super.onNextButtonPressed(event);
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
