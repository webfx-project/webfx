package mongoose.activities.shared.book.event.shared;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mongoose.activities.shared.generic.MongooseSectionFactoryMixin;
import mongoose.activities.shared.generic.eventdependent.EventDependentViewDomainActivity;
import naga.framework.ui.graphic.background.BackgroundUtil;
import naga.framework.ui.layouts.LayoutUtil;
import naga.util.Strings;

/**
 * @author Bruno Salmon
 */
public abstract class BookingProcessActivity
        extends EventDependentViewDomainActivity
        implements MongooseSectionFactoryMixin {

    protected Button backButton;
    protected Button nextButton;

    protected BorderPane pageContainer;
    protected ScrollPane verticalScrollPane;
    protected VBox verticalStack;

    @Override
    public Node buildUi() {
        createViewNodes();
        return styleUi(assemblyViewNodes());
    }

    protected void createViewNodes() {
        if (backButton == null)
            backButton = newTransparentButton("<<Back");
        if (nextButton == null)
            nextButton = newLargeGreenButton( "Next>>");
        backButton.setOnAction(this::onPreviousButtonPressed);
        nextButton.setOnAction(this::onNextButtonPressed);

        pageContainer = new BorderPane(verticalScrollPane = LayoutUtil.createVerticalScrollPaneWithPadding(verticalStack = new VBox(10)));
        verticalStack.setAlignment(Pos.TOP_CENTER);
    }

    protected Node assemblyViewNodes() {
        return pageContainer;
    }

    protected Node styleUi(Node uiNode) {
        if (uiNode instanceof Region)
            onEvent().setHandler(ar -> {
                if (ar.succeeded()) {
                    String css = ar.result().getStringFieldValue("cssClass");
                    if (Strings.startsWith(css,"linear-gradient"))
                        ((Region) uiNode).setBackground(BackgroundUtil.newLinearGradientBackground(css));
                }
            });
        return uiNode;
    }

    private void onPreviousButtonPressed(ActionEvent event) {
        getHistory().goBack();
    }

    protected void onNextButtonPressed(ActionEvent event) { // Should be overridden
    }
}
