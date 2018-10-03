package webfx.framework.client.activity.impl.elementals.view;

import javafx.scene.Node;
import webfx.framework.client.activity.impl.elementals.activeproperty.ActivePropertyActivity;
import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ViewActivity
        <C extends UiRouteActivityContext<C>>

        extends ActivePropertyActivity<C> {

    Node buildUi();

}
