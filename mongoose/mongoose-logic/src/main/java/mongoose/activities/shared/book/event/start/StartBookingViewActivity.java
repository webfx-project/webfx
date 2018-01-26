package mongoose.activities.shared.book.event.start;

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
import mongoose.actions.MongooseActions;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.util.Labels;
import naga.framework.ui.anim.Animations;
import naga.framework.ui.layouts.LayoutUtil;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.fx.util.ImageStore;

/**
 * @author Bruno Salmon
 */
public class StartBookingViewActivity extends BookingProcessViewActivity {

    private ImageView eventImageView;
    private BorderPane eventImageViewContainer;
    private Label eventTitle;

    public StartBookingViewActivity() {
        super(null);
    }

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        eventImageViewContainer = LayoutUtil.setMinWidth(new BorderPane(eventImageView = new ImageView()), 0);
        eventTitle = new Label();
        eventTitle.setTextFill(Color.WHITE);
        Button bookButton = newTransparentButton(MongooseActions.newVisitBookAction(this::onBookButtonPressed));
        Button feesButton = newTransparentButton(MongooseActions.newVisitFeesAction(this::onFeesButtonPressed));
        Button termsButton = newTransparentButton(MongooseActions.newVisitTermsAndConditionsAction(this::onTermsButtonPressed));
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
            Toolkit.get().scheduler().runInUiThread(() -> {
                String imageUrl = null;
                if (ar.succeeded()) {
                    Labels.translateLabel(eventTitle, Labels.bestLabelOrName(ar.result()), getI18n());
                    imageUrl = (String) ar.result().evaluate("buddha.image.url");
                }
                if (imageUrl == null)
                    runFadeInAnimation();
                else {
                    Image image = ImageStore.getOrCreateImage(imageUrl);
                    eventImageView.setImage(image);
                    eventImageView.setPreserveRatio(true);
                    eventImageView.fitWidthProperty().bind(Properties.combine(eventImageViewContainer.widthProperty(), image.widthProperty(),
                            (w1, w2) -> Math.min(w1.doubleValue(), w2.doubleValue())));
                    if (!image.isBackgroundLoading())
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
