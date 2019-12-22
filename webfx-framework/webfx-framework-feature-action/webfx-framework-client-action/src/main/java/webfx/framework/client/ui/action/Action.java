package webfx.framework.client.ui.action;

import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import webfx.framework.client.ui.action.impl.ReadOnlyAction;

/**
 * An action compatible with standard JavaFx API (ex: can be passed to Button.setOnAction()) but enriched with graphical
 * properties (ie text, graphic, disabled and visible properties). The ActionBinder utility class can be used to help
 * binding graphical components (such as buttons) to actions. The ActionBuilder utility class can be used to
 *
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

    void setUserData(Object userData);

    Object getUserData();

    static Action create(ObservableStringValue textProperty, ObservableObjectValue<Node> graphicProperty, ObservableBooleanValue disabledProperty, ObservableBooleanValue visibleProperty, EventHandler<ActionEvent> actionHandler) {
        return new ReadOnlyAction(textProperty, graphicProperty, disabledProperty, visibleProperty, actionHandler);
    }
}
