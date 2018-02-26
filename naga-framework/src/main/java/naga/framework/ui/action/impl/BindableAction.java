package naga.framework.ui.action.impl;

import javafx.beans.property.*;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public class BindableAction extends ActionImpl {

    public BindableAction(EventHandler<ActionEvent> actionHandler) {
        this(new SimpleStringProperty(), new SimpleObjectProperty<>(), new SimpleBooleanProperty(false), new SimpleBooleanProperty(true), actionHandler);
    }

    public BindableAction(ObservableStringValue textProperty, ObservableObjectValue<Node> graphicProperty, ObservableBooleanValue disabledProperty, ObservableBooleanValue visibleProperty, EventHandler<ActionEvent> actionHandler) {
        super(textProperty, graphicProperty, disabledProperty, visibleProperty, actionHandler);
    }

    public StringProperty bindableTextProperty() {
        return (StringProperty) textProperty();
    }

    public void setText(String text) {
        bindableTextProperty().set(text);
    }

    public ObjectProperty<Node> bindableGraphicProperty() {
        return (ObjectProperty<Node>) graphicProperty();
    }

    public void setGraphic(Node graphic) {
        bindableGraphicProperty().set(graphic);
    }

    public BooleanProperty bindableDisabledProperty() {
        return (BooleanProperty) disabledProperty();
    }

    public void setDisabled(boolean disabled) {
        bindableDisabledProperty().set(disabled);
    }

    public BooleanProperty bindableVisibleProperty() {
        return (BooleanProperty) visibleProperty();
    }

    public void setVisible(boolean visible) {
        bindableVisibleProperty().set(visible);
    }
}
