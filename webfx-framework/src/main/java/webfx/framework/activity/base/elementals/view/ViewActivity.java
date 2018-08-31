package webfx.framework.activity.base.elementals.view;

import javafx.scene.Node;
import webfx.framework.activity.base.elementals.activeproperty.ActivePropertyActivity;
import webfx.framework.activity.base.elementals.uiroute.UiRouteActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ViewActivity
        <C extends UiRouteActivityContext<C>>

        extends ActivePropertyActivity<C> {

    Node buildUi();

}
