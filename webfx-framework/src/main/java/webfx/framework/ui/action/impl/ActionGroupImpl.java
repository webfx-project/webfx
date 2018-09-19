package webfx.framework.ui.action.impl;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import webfx.framework.ui.action.Action;
import webfx.framework.ui.action.ActionGroup;
import webfx.fxkits.core.util.properties.Properties;
import webfx.platforms.core.util.collection.Collections;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public final class ActionGroupImpl extends ReadOnlyAction implements ActionGroup {

    private final Collection<Action> actions;
    private ObservableList<Action> visibleActions = FXCollections.observableArrayList();

    public ActionGroupImpl(Collection<Action> actions, ObservableStringValue textProperty, ObservableObjectValue<Node> graphicProperty, ObservableBooleanValue disabledProperty, ObservableBooleanValue visibleProperty, EventHandler<ActionEvent> actionHandler) {
        super(textProperty, graphicProperty, disabledProperty, visibleProperty, actionHandler);
        this.actions = actions;
        Properties.runNowAndOnPropertiesChange(this::updateVisibleActions, Collections.map(actions, Action::visibleProperty));
    }

    private void updateVisibleActions() {
        visibleActions.setAll(Collections.filter(actions, Action::isVisible));
        ObservableBooleanValue groupVisibleObservableValue = visibleProperty();
        if (groupVisibleObservableValue instanceof Property)
            Properties.setIfNotBound((Property) groupVisibleObservableValue, !visibleActions.isEmpty());
    }

    @Override
    public Collection<Action> getActions() {
        return actions;
    }

    @Override
    public ObservableList<Action> getVisibleActions() {
        return visibleActions;
    }
}
