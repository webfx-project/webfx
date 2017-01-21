package naga.framework.activity.view;

import javafx.scene.Node;
import naga.framework.activity.uiroute.UiRouteActivity;
import naga.framework.activity.uiroute.UiRouteActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ViewActivity
        <C extends UiRouteActivityContext<C>>

        extends UiRouteActivity<C> {

    Node buildUi();

}
