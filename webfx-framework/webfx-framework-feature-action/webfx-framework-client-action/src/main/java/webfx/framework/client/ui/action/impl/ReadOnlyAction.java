package webfx.framework.client.ui.action.impl;

import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import webfx.framework.client.ui.action.Action;

/**
 * A read-only action where properties are observable values.
 *
 * @author Bruno Salmon
 */
public class ReadOnlyAction implements Action {

    public ReadOnlyAction(ObservableStringValue textProperty, ObservableObjectValue<Node> graphicProperty, ObservableBooleanValue disabledProperty, ObservableBooleanValue visibleProperty, EventHandler<ActionEvent> actionHandler) {
        this.textProperty = textProperty;
        this.graphicProperty = graphicProperty;
        this.disabledProperty = disabledProperty;
        this.visibleProperty = visibleProperty;
        this.actionHandler = actionHandler;
    }

    private final ObservableStringValue textProperty;
    @Override
    public ObservableStringValue textProperty() {
        return textProperty;
    }

    private final ObservableObjectValue<Node> graphicProperty;
    @Override
    public ObservableObjectValue<Node> graphicProperty() {
        return graphicProperty;
    }

    private final ObservableBooleanValue disabledProperty;
    @Override
    public ObservableBooleanValue disabledProperty() {
        return disabledProperty;
    }

    private final ObservableBooleanValue visibleProperty;
    @Override
    public ObservableBooleanValue visibleProperty() {
        return visibleProperty;
    }

    private Object userData;

    @Override
    public Object getUserData() {
        return userData;
    }

    @Override
    public void setUserData(Object userData) {
        this.userData = userData;
    }

    private final EventHandler<ActionEvent> actionHandler;
    @Override
    public void handle(ActionEvent event) {
        if (actionHandler != null)
            actionHandler.handle(event);
    }
}
