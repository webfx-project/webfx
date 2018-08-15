package naga.framework.activity.base.elementals.view;

import javafx.beans.property.Property;
import javafx.scene.Node;
import naga.framework.activity.base.elementals.uiroute.UiRouteActivityContextMixin;
import naga.framework.ui.graphic.controls.button.ButtonFactoryMixin;

/**
 * @author Bruno Salmon
 */
public interface ViewActivityContextMixin
        <C extends ViewActivityContext<C>>

        extends UiRouteActivityContextMixin<C>,
        ButtonFactoryMixin,
        ViewActivityContext<C> {

    @Override
    default Property<Node> nodeProperty() { return getActivityContext().nodeProperty(); }

    @Override
    default Property<Node> mountNodeProperty() { return getActivityContext().mountNodeProperty(); }

}
