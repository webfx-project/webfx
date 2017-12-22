package mongoose.activities.shared.book.event.shared;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import mongoose.activities.shared.generic.MongooseSectionFactoryMixin;
import mongoose.activities.shared.generic.eventdependent.EventDependentViewDomainActivity;
import mongoose.entities.Event;
import naga.framework.ui.controls.BackgroundUtil;
import naga.framework.ui.layouts.LayoutUtil;

/**
 * @author Bruno Salmon
 */
public abstract class BookingProcessViewActivity
        extends EventDependentViewDomainActivity
        implements MongooseSectionFactoryMixin {

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
        return styleUi(assemblyViewNodes());
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

    protected Node styleUi(Node uiNode) {
        if (uiNode instanceof Region)
            onEvent().setHandler(ar -> {
                if (ar.succeeded()) {
                    Event event = ar.result();
                    String css = event.getStringFieldValue("cssClass");
                    if (css != null && css.startsWith("linear-gradient")) {
                        Background eventBackground = BackgroundUtil.newLinearGradientBackground(css);
                        ((Region) uiNode).setBackground(eventBackground);
                    }
                }
            });
        return uiNode;
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
