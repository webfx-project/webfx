package mongoose.activities.bothends.container;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import mongoose.activities.bothends.generic.MongooseButtonFactoryMixin;
import mongoose.operations.bothends.i18n.ChangeLanguageToEnglishRequest;
import mongoose.operations.bothends.i18n.ChangeLanguageToFrenchRequest;
import naga.framework.activity.view.impl.ViewActivityImpl;
import naga.framework.operation.action.OperationActionProducer;
import naga.framework.ui.action.Action;
import naga.framework.ui.action.ActionBinder;
import naga.framework.operations.route.RouteBackwardRequest;
import naga.framework.operations.route.RouteForwardRequest;
import naga.util.collection.Collections;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class SharedContainerActivity extends ViewActivityImpl
        implements MongooseButtonFactoryMixin
        , OperationActionProducer {

    protected Action backAction;
    protected Action forwardAction;
    protected Action englishAction;
    protected Action frenchAction;

    @Override
    public Node buildUi() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(ActionBinder.bindChildrenToVisibleActions(new FlowPane(), navigationActions(), this::newButton));
        borderPane.centerProperty().bind(mountNodeProperty());
        return borderPane;
    }

    protected Collection<Action> navigationActions() {
        return Collections.listOf(
                   backAction    = newAction(() -> new RouteBackwardRequest(getHistory()))
                ,  forwardAction = newAction(() -> new RouteForwardRequest(getHistory()))
                ,  englishAction = newAction(() -> new ChangeLanguageToEnglishRequest())
                ,  frenchAction  = newAction(() -> new ChangeLanguageToFrenchRequest())
        );
    }
}
