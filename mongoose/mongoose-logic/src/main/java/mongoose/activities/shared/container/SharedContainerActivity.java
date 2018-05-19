package mongoose.activities.shared.container;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import mongoose.activities.shared.generic.MongooseButtonFactoryMixin;
import mongoose.i18n.EnglishLanguageRequest;
import mongoose.i18n.FrenchLanguageRequest;
import naga.framework.activity.view.impl.ViewActivityImpl;
import naga.framework.operation.action.OperationActionProducer;
import naga.framework.ui.action.Action;
import naga.framework.ui.action.ActionBinder;
import naga.framework.ui.router.BackwardRoutingRequest;
import naga.framework.ui.router.ForwardRoutingRequest;
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
                   backAction    = newAction(() -> new BackwardRoutingRequest(getHistory()))
                ,  forwardAction = newAction(() -> new ForwardRoutingRequest(getHistory()))
                ,  englishAction = newAction(() -> new EnglishLanguageRequest(getI18n()))
                ,  frenchAction  = newAction(() -> new FrenchLanguageRequest(getI18n()))
        );
    }
}
