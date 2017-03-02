package naga.framework.activity.view;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.Button;
import naga.framework.activity.uiroute.UiRouteActivityContextMixin;
import naga.framework.ui.action.Action;
import naga.framework.ui.action.ActionRegistry;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public interface ViewActivityContextMixin
        <C extends ViewActivityContext<C>>

        extends UiRouteActivityContextMixin<C>,
        ViewActivityContext<C> {

    @Override
    default Property<Node> nodeProperty() { return getActivityContext().nodeProperty(); }

    @Override
    default Property<Node> mountNodeProperty() { return getActivityContext().mountNodeProperty(); }

    @Override
    default I18n getI18n() { return getActivityContext().getI18n(); }

    default Button newButton(Action action) {
        return action.toButton(getI18n());
    }

    default Button newOkButton(Runnable handler) {
        return newButton(ActionRegistry.newOkAction(handler));
    }

    default Button newCancelButton(Runnable handler) {
        return newButton(ActionRegistry.newCancelAction(handler));
    }

    default Button newRemoveButton(Runnable handler) {
        return newButton(ActionRegistry.newRemoveAction(handler));
    }

}
