package naga.framework.ui.action.impl;

import javafx.beans.property.*;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

/**
 * An writable action where properties (text, graphic, disabled, visible) can be set later (ie after constructor call)
 * either by calling the setters or by binding these properties (ex: writableTextProperty().bind(myTextProperty))
 *
 * @author Bruno Salmon
 */
public class WritableAction extends ReadOnlyAction {

    public WritableAction(EventHandler<ActionEvent> actionHandler) {
        this(new SimpleStringProperty(), new SimpleObjectProperty<>(), new SimpleBooleanProperty(true /* disabled until it is bound */), new SimpleBooleanProperty(false /* invisible until it is bound */), actionHandler);
    }

    public WritableAction(ObservableStringValue textProperty, ObservableObjectValue<Node> graphicProperty, ObservableBooleanValue disabledProperty, ObservableBooleanValue visibleProperty, EventHandler<ActionEvent> actionHandler) {
        super(textProperty, graphicProperty, disabledProperty, visibleProperty, actionHandler);
    }

    public StringProperty writableTextProperty() {
        return (StringProperty) textProperty();
    }

    public void setText(String text) {
        writableTextProperty().set(text);
    }

    public ObjectProperty<Node> writableGraphicProperty() {
        return (ObjectProperty<Node>) graphicProperty();
    }

    public void setGraphic(Node graphic) {
        writableGraphicProperty().set(graphic);
    }

    public BooleanProperty writableDisabledProperty() {
        return (BooleanProperty) disabledProperty();
    }

    public void setDisabled(boolean disabled) {
        writableDisabledProperty().set(disabled);
    }

    public BooleanProperty writableVisibleProperty() {
        return (BooleanProperty) visibleProperty();
    }

    public void setVisible(boolean visible) {
        writableVisibleProperty().set(visible);
    }
}
