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
import naga.framework.ui.action.ActionGroup;
import naga.framework.ui.action.ActionGroupBuilder;
import naga.framework.ui.router.BackwardRoutingRequest;
import naga.framework.ui.router.ForwardRoutingRequest;
import naga.fx.properties.ObservableLists;
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
        FlowPane navigationFlowPane = new FlowPane();
        ActionGroup navigationActionGroup = new ActionGroupBuilder().setActions(navigationActions()).build();
        ObservableLists.bindConverted(navigationFlowPane.getChildren(), navigationActionGroup.getVisibleActions(), this::newButton);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(navigationFlowPane);
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
