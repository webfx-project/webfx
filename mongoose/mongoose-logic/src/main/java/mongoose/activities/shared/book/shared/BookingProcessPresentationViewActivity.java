package mongoose.activities.shared.book.shared;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import mongoose.activities.shared.generic.MongooseButtonFactoryMixin;
import mongoose.activities.shared.generic.MongooseSectionFactoryMixin;
import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import mongoose.entities.Event;
import mongoose.services.EventService;
import naga.framework.activity.presentation.view.impl.PresentationViewActivityImpl;
import naga.framework.ui.graphic.background.BackgroundUtil;

/**
 * @author Bruno Salmon
 */
public abstract class BookingProcessPresentationViewActivity<PM extends BookingProcessPresentationModel>
        extends PresentationViewActivityImpl<PM>
        implements MongooseButtonFactoryMixin, MongooseSectionFactoryMixin {

    protected Button backButton;
    protected Button nextButton;

    @Override
    protected void createViewNodes(PM pm) {
        if (backButton == null)
            backButton = newTransparentButton("<<Back");
        if (nextButton == null)
            nextButton = newLargeGreenButton("Next>>");
        backButton.onActionProperty().bind(pm.onPreviousActionProperty());
        nextButton.onActionProperty().bind(pm.onNextActionProperty());
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(null, null, null, new HBox(backButton, nextButton), null);
    }

    @Override
    protected Node styleUi(Node uiNode, PM pm) {
        if (uiNode instanceof Region) {
            EventService.getOrCreate(pm.getEventId(), DomainModelSnapshotLoader.getDataSourceModel()).onEvent().setHandler(ar -> {
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
