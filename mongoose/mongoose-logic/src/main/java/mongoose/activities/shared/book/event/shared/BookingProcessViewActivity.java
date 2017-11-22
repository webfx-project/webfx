package mongoose.activities.shared.book.event.shared;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongoose.activities.shared.generic.eventdependent.EventDependentViewDomainActivity;
import naga.framework.ui.layouts.LayoutUtil;

/**
 * @author Bruno Salmon
 */
public abstract class BookingProcessViewActivity extends EventDependentViewDomainActivity {

    private final String nextPage;

    protected Button previousButton;
    protected Button nextButton;

    protected BorderPane borderPane;

    public BookingProcessViewActivity(String nextPage) {
        this.nextPage = nextPage;
    }

    @Override
    public Node buildUi() {
        createViewNodes();
        return assemblyViewNodes();
    }

    protected void createViewNodes() {
        if (previousButton == null)
            previousButton = newButton("<<Back");
        if (nextButton == null)
            nextButton = newButton( "Next>>");
        previousButton.setOnAction(this::onPreviousButtonPressed);
        nextButton.setOnAction(this::onNextButtonPressed);

        borderPane = new BorderPane(null, null, null, new HBox(previousButton, LayoutUtil.createHGrowable(), nextButton), null);
    }

    protected Node assemblyViewNodes() {
        return borderPane;
    }

    private void onPreviousButtonPressed(ActionEvent event) {
        getHistory().goBack();
    }

    protected void onNextButtonPressed(ActionEvent event) {
        goToNextBookingProcessPage(nextPage);
    }

    protected void goToNextBookingProcessPage(String page) {
        getHistory().push("/book/event/" + getEventId() + "/" + page);
    }
}
