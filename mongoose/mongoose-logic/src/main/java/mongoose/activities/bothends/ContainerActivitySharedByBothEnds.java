package mongoose.activities.bothends;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import mongoose.activities.bothends.generic.MongooseButtonFactoryMixin;
import mongoose.operations.bothends.i18n.ChangeLanguageToEnglishRequest;
import mongoose.operations.bothends.i18n.ChangeLanguageToFrenchRequest;
import naga.framework.activity.base.elementals.view.impl.ViewDomainActivityBase;
import naga.framework.operation.action.OperationActionFactoryMixin;
import naga.framework.ui.action.Action;
import naga.framework.ui.action.ActionBinder;
import naga.framework.operations.route.RouteBackwardRequest;
import naga.framework.operations.route.RouteForwardRequest;
import naga.util.collection.Collections;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class ContainerActivitySharedByBothEnds extends ViewDomainActivityBase
        implements MongooseButtonFactoryMixin
        , OperationActionFactoryMixin {

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
                ,  englishAction = newAction(ChangeLanguageToEnglishRequest::new)
                ,  frenchAction  = newAction(ChangeLanguageToFrenchRequest::new)
        );
    }
}
