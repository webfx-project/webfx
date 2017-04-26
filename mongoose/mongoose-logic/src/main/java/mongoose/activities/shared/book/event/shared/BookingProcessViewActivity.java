package mongoose.activities.shared.book.event.shared;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongoose.activities.shared.generic.eventdependent.EventDependentViewDomainActivity;
import naga.framework.ui.i18n.I18n;

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
        I18n i18n = getI18n();
        if (previousButton == null)
            previousButton = i18n.translateText(new Button(), "Back");
        if (nextButton == null)
            nextButton = i18n.translateText(new Button(), "Next");
        previousButton.setOnAction(this::onPreviousButtonPressed);
        nextButton.setOnAction(this::onNextButtonPressed);

        borderPane = new BorderPane(null, null, null, new HBox(previousButton, nextButton), null);
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
        getHistory().push("/event/" + getEventId() + "/" + page);
    }
}
