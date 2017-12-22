package mongoose.activities.shared.book.event.shared;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import mongoose.activities.shared.generic.MongooseButtonFactoryMixin;
import mongoose.activities.shared.generic.MongooseSectionFactoryMixin;
import mongoose.entities.Event;
import mongoose.services.EventService;
import naga.framework.activity.presentation.view.impl.PresentationViewActivityImpl;
import naga.framework.ui.controls.BackgroundUtil;

/**
 * @author Bruno Salmon
 */
public abstract class BookingProcessPresentationViewActivity<PM extends BookingProcessPresentationModel>
        extends PresentationViewActivityImpl<PM>
        implements MongooseButtonFactoryMixin, MongooseSectionFactoryMixin {

    protected Button previousButton;
    protected Button nextButton;

    @Override
    protected void createViewNodes(PM pm) {
        if (previousButton == null)
            previousButton = newButton("<<Back");
        if (nextButton == null)
            nextButton = newButton("Next>>");
        previousButton.onActionProperty().bind(pm.onPreviousActionProperty());
        nextButton.onActionProperty().bind(pm.onNextActionProperty());
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(null, null, null, new HBox(previousButton, nextButton), null);
    }

    @Override
    protected Node styleUi(Node uiNode, PM pm) {
        if (uiNode instanceof Region) {
            EventService.get(pm.getEventId()).onEvent().setHandler(ar -> {
                if (ar.succeeded()) {
                    Event event = ar.result();
                    String css = event.getStringFieldValue("cssClass");
                    if (css.startsWith("linear-gradient")) {
                        Background eventBackground = BackgroundUtil.newLinearGradientBackground(css);
                        ((Region) uiNode).setBackground(eventBackground);
                    }
                }
            });
        }
        return uiNode;
    }
}
