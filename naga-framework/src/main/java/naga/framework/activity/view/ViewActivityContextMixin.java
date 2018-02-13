package naga.framework.activity.view;

import javafx.beans.property.Property;
import javafx.scene.Node;
import naga.framework.activity.uiroute.UiRouteActivityContextMixin;
import naga.framework.ui.graphic.controls.button.ButtonFactoryMixin;
import naga.framework.ui.i18n.I18n;

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

    @Override
    default I18n getI18n() { return getActivityContext().getI18n(); }

}
