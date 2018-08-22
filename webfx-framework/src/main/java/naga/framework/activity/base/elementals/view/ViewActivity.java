package naga.framework.activity.base.elementals.view;

import javafx.scene.Node;
import naga.framework.activity.base.elementals.activeproperty.ActivePropertyActivity;
import naga.framework.activity.base.elementals.uiroute.UiRouteActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ViewActivity
        <C extends UiRouteActivityContext<C>>

        extends ActivePropertyActivity<C> {

    Node buildUi();

}
