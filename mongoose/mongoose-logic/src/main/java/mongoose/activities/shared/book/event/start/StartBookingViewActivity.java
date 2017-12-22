package mongoose.activities.shared.book.event.start;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mongoose.actions.MongooseActions;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import naga.framework.ui.layouts.LayoutUtil;
import naga.fx.properties.Properties;
import naga.fx.util.ImageStore;

/**
 * @author Bruno Salmon
 */
public class StartBookingViewActivity extends BookingProcessViewActivity {

    private ImageView eventImageView;
    private BorderPane eventImageViewContainer;
    private VBox buttonsVBox;
    private HBox buttonsHBox;

    public StartBookingViewActivity() {
        super(null);
    }

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        eventImageViewContainer = LayoutUtil.setMinWidth(new BorderPane(eventImageView = new ImageView()), 0);
        Button termsButton = newButton(MongooseActions.newVisitTermsAndConditionsAction(this::onTermsButtonPressed));
        Button programButton = newButton(MongooseActions.newVisitProgramAction(this::onProgramButtonPressed));
        Button feesButton = newButton(MongooseActions.newVisitFeesAction(this::onFeesButtonPressed));
        Button bookButton = newButton(MongooseActions.newVisitBookAction(this::onBookButtonPressed));
        buttonsHBox = new HBox(previousButton, LayoutUtil.createHGrowable(), termsButton, programButton);
        buttonsVBox = new VBox(20, eventImageViewContainer, feesButton, bookButton);
        buttonsVBox.setFillWidth(true);
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(buttonsVBox, null, null, buttonsHBox, null);
    }

    @Override
    protected Node styleUi(Node uiNode) {
        buttonsVBox.setVisible(false);
        onEvent().setHandler(ar -> {
            if (ar.succeeded()) {
                Object url = ar.result().evaluate("buddha.image.url");
                if (url instanceof String) {
                    Image image = ImageStore.getOrCreateImage((String) url);
                    eventImageView.setImage(image);
                    eventImageView.setPreserveRatio(true);
                    eventImageView.fitWidthProperty().bind(Properties.combine(eventImageViewContainer.widthProperty(), image.widthProperty(),
                            (w1, w2) -> Math.min(w1.doubleValue(), w2.doubleValue())));
                }
            }
            buttonsVBox.setVisible(true);
        });
        return super.styleUi(uiNode);
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
