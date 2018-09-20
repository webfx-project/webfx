package mongooses.core.activities.sharedends.book.shared;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import mongooses.core.activities.sharedends.generic.MongooseButtonFactoryMixin;
import mongooses.core.activities.sharedends.generic.MongooseSectionFactoryMixin;
import mongooses.core.aggregates.EventAggregate;
import mongooses.core.domainmodel.loader.DomainModelSnapshotLoader;
import mongooses.core.entities.Event;
import webfx.framework.activity.impl.elementals.presentation.view.impl.PresentationViewActivityImpl;
import webfx.framework.ui.util.background.BackgroundUtil;
import webfx.fxkits.core.util.properties.Properties;
import webfx.platforms.core.util.Strings;

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
        if (uiNode instanceof Region)
            Properties.runNowAndOnPropertiesChange(() -> EventAggregate.getOrCreate(pm.getEventId(), DomainModelSnapshotLoader.getDataSourceModel()).onEvent().setHandler(ar -> {
                Event event = ar.result();
                if (event != null) {
                    String css = event.getStringFieldValue("cssClass");
                    if (Strings.startsWith(css, "linear-gradient")) {
                        Background eventBackground = BackgroundUtil.newLinearGradientBackground(css);
                        ((Region) uiNode).setBackground(eventBackground);
                    }
                }
            }), pm.eventIdProperty());
        return uiNode;
    }
}
