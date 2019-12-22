package mongoose.frontend.activities.startbooking;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import mongoose.client.activity.bookingprocess.BookingProcessActivity;
import mongoose.frontend.operations.options.RouteToOptionsRequest;
import mongoose.frontend.operations.terms.RouteToTermsRequest;
import mongoose.frontend.operations.fees.RouteToFeesRequest;
import mongoose.frontend.operations.program.RouteToProgramRequest;
import mongoose.client.actions.MongooseActions;
import mongoose.client.entities.util.Labels;
import webfx.framework.client.ui.action.Action;
import webfx.framework.client.ui.util.layout.LayoutUtil;
import webfx.framework.client.ui.util.anim.Animations;
import webfx.kit.util.properties.Properties;
import webfx.extras.imagestore.ImageStore;
import webfx.platform.client.services.uischeduler.UiScheduler;

/**
 * @author Bruno Salmon
 */
final class StartBookingActivity extends BookingProcessActivity {

    private final Action bookAction = MongooseActions.newVisitBookAction(this::onBookButtonPressed);
    private final Action feesAction = MongooseActions.newVisitFeesAction(this::onFeesButtonPressed);
    private final Action termsAction = MongooseActions.newVisitTermsAndConditionsAction(this::onTermsButtonPressed);

    private ImageView eventImageView;
    private BorderPane eventImageViewContainer;
    private Label eventTitle;

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        eventImageViewContainer = LayoutUtil.setMinWidth(new BorderPane(eventImageView = new ImageView()), 0);
        eventTitle = new Label();
        eventTitle.setTextFill(Color.WHITE);
        Button bookButton = newTransparentButton(bookAction);
        Button feesButton = newTransparentButton(feesAction);
        Button termsButton = newTransparentButton(termsAction);
        //Button programButton = newTransparentButton(MongooseActions.newVisitProgramAction(this::onProgramButtonPressed));
        Font eventFont = Font.font("Verdana", 24);
        Font bookButtonFont = Font.font("Verdana", 18);
        Font otherButtonFont = Font.font("Verdana", 12);
        eventTitle.setFont(eventFont);
        bookButton.setFont(bookButtonFont);
        feesButton.setFont(otherButtonFont);
        termsButton.setFont(otherButtonFont);
        //programButton.setFont(otherButtonFont);
        double vGap = 20;
        FlowPane flowPane = new FlowPane(5, vGap, feesButton, termsButton/*, programButton*/);
        flowPane.setAlignment(Pos.CENTER);
        verticalStack.setSpacing(vGap);
        verticalStack.getChildren().setAll(eventImageViewContainer, eventTitle, bookButton, flowPane);
        GridPane goldLayout = LayoutUtil.createGoldLayout(verticalStack, 1.0, 0, null);
        pageContainer.setCenter(verticalScrollPane = LayoutUtil.createVerticalScrollPane(goldLayout));
        goldLayout.minHeightProperty().bind(verticalScrollPane.heightProperty());
    }

    @Override
    protected Node styleUi(Node uiNode) {
        fadeOut();
        onEvent().setHandler(ar -> {
            onEventOptions(); // Anticipating event options loading now (required for options and fees pages)
            UiScheduler.runInUiThread(() -> {
                String imageUrl = null;
                if (ar.succeeded()) {
                    Labels.translateLabel(eventTitle, Labels.bestLabelOrName(ar.result()));
                    imageUrl = (String) ar.result().evaluate("buddha.image.url");
                }
                if (imageUrl == null)
                    runFadeInAnimation();
                else {
                    Image image = ImageStore.getOrCreateImage(imageUrl);
                    eventImageView.setImage(image);
                    eventImageView.setPreserveRatio(true);
                    if (image != null)
                        eventImageView.fitWidthProperty().bind(Properties.combine(eventImageViewContainer.widthProperty(), image.widthProperty(),
                                (w1, w2) -> Math.min(w1.doubleValue(), w2.doubleValue())));
                    if (image == null || !image.isBackgroundLoading())
                        runFadeInAnimation();
                    else
                        image.heightProperty().addListener(observable -> runFadeInAnimation());
                }
            });
        });
        return super.styleUi(uiNode);
    }

    private void fadeOut() {
        verticalStack.setOpacity(0d);
    }

    private void runFadeInAnimation() {
        Animations.animateProperty(verticalStack.opacityProperty(), 1d);
    }

    private void onProgramButtonPressed() {
        new RouteToProgramRequest(getEventId(), getHistory()).execute();
    }

    private void onTermsButtonPressed() {
        new RouteToTermsRequest(getEventId(), getHistory()).execute();
    }

    private void onFeesButtonPressed() {
        new RouteToFeesRequest(getEventId(), getHistory()).execute();
    }

    private void onBookButtonPressed() {
        new RouteToOptionsRequest(getEventId(), getHistory()).execute();
    }
}
