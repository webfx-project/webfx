package mongoose.activities.shared.book.event.start;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mongoose.actions.MongooseActions;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import naga.framework.ui.layouts.LayoutUtil;

/**
 * @author Bruno Salmon
 */
public class StartBookingViewActivity extends BookingProcessViewActivity {

    private VBox buttonsVBox;
    private HBox buttonsHBox;

    public StartBookingViewActivity() {
        super(null);
    }

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        Button termsButton = newButton(MongooseActions.newVisitTermsAndConditionsAction(this::onTermsButtonPressed));
        Button programButton = newButton(MongooseActions.newVisitProgramAction(this::onProgramButtonPressed));
        Button feesButton = newButton(MongooseActions.newVisitFeesAction(this::onFeesButtonPressed));
        Button bookButton = newButton(MongooseActions.newVisitBookAction(this::onBookButtonPressed));
        buttonsHBox = new HBox(previousButton, LayoutUtil.createHGrowable(), termsButton, programButton);
        buttonsVBox = new VBox(20, feesButton, bookButton);
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(buttonsVBox, null, null, buttonsHBox, null);
    }

    private void onProgramButtonPressed() {
        goToNextBookingProcessPage("program");
    }

    private void onTermsButtonPressed() {
        goToNextBookingProcessPage("terms");
    }

    private void onFeesButtonPressed() {
        goToNextBookingProcessPage("fees");
    }

    private void onBookButtonPressed() {
        goToNextBookingProcessPage("options");
    }
}
