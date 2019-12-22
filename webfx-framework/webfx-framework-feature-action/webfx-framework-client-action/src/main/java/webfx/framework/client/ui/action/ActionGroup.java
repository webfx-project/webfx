package webfx.framework.client.ui.action;

import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import webfx.framework.client.ui.action.impl.ActionGroupImpl;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface ActionGroup extends Action {

    Collection<Action> getActions();

    ObservableList<Action> getVisibleActions();

    boolean hasSeparators();

    static ActionGroup create(Collection<Action> actions, ObservableStringValue textProperty, ObservableObjectValue<Node> graphicProperty, ObservableBooleanValue disabledProperty, ObservableBooleanValue visibleProperty, boolean hasSeparators, EventHandler<ActionEvent> actionHandler) {
        return new ActionGroupImpl(actions, textProperty, graphicProperty, disabledProperty, visibleProperty, hasSeparators, actionHandler);
    }

}
