package naga.framework.ui.action;

import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import naga.framework.ui.action.impl.ActionImpl;

/**
 * @author Bruno Salmon
 */
public interface Action extends EventHandler<ActionEvent> {

    ObservableStringValue textProperty();
    default String getText() {
        return textProperty().get();
    }

    ObservableObjectValue<Node> graphicProperty();
    default Node getGraphic() {
        return graphicProperty().get();
    }

    ObservableBooleanValue disabledProperty();
    default boolean isDisabled() {
        return disabledProperty().get();
    }

    ObservableBooleanValue visibleProperty();
    default boolean isVisible() {
        return visibleProperty().get();
    }

    static Action create(ObservableStringValue textProperty, ObservableObjectValue<Node> graphicProperty, ObservableBooleanValue disabledProperty, ObservableBooleanValue visibleProperty, EventHandler<ActionEvent> actionHandler) {
        return new ActionImpl(textProperty, graphicProperty, disabledProperty, visibleProperty, actionHandler);
    }
}
