package webfx.framework.client.ui.action.impl;

import javafx.beans.property.*;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import webfx.framework.client.ui.action.Action;
import webfx.platform.shared.util.Arrays;

/**
 * An writable action where properties (text, graphic, disabled, visible) can be set later (ie after constructor call)
 * either by calling the setters or by binding these properties (ex: writableTextProperty().bind(myTextProperty))
 *
 * @author Bruno Salmon
 */
public class WritableAction extends ReadOnlyAction {

    public WritableAction(Action action, String... writablePropertyNames) {
        this(createStringProperty(action.textProperty(), "text", writablePropertyNames), createObjectProperty(action.graphicProperty(), "graphic", writablePropertyNames), createBooleanProperty(action.disabledProperty(), "disabled", writablePropertyNames), createBooleanProperty(action.visibleProperty(), "visible", writablePropertyNames), action);
    }

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

    private static StringProperty createStringProperty(ObservableStringValue readOnlyProperty, String propertyName, String... writablePropertyNames) {
        if (readOnlyProperty instanceof StringProperty)
            return (StringProperty) readOnlyProperty;
        SimpleStringProperty writableProperty = new SimpleStringProperty() {
            @Override
            public void set(String newValue) {
                unbindPropertyIfWritable(this, propertyName, writablePropertyNames);
                super.set(newValue);
            }
        };
        writableProperty.bind(readOnlyProperty);
        return writableProperty;
    }

    private static <T> ObjectProperty<T> createObjectProperty(ObservableObjectValue readOnlyProperty, String propertyName, String... writablePropertyNames) {
        if (readOnlyProperty instanceof ObjectProperty)
            return (ObjectProperty<T>) readOnlyProperty;
        SimpleObjectProperty<T> writableProperty = new SimpleObjectProperty<T>() {
            @Override
            public void set(T newValue) {
                unbindPropertyIfWritable(this, propertyName, writablePropertyNames);
                super.set(newValue);
            }
        };
        writableProperty.bind(readOnlyProperty);
        return writableProperty;
    }

    private static BooleanProperty createBooleanProperty(ObservableBooleanValue readOnlyProperty, String propertyName, String... writablePropertyNames) {
        if (readOnlyProperty instanceof BooleanProperty)
            return (BooleanProperty) readOnlyProperty;
        SimpleBooleanProperty writableProperty = new SimpleBooleanProperty() {
            @Override
            public void set(boolean newValue) {
                unbindPropertyIfWritable(this, propertyName, writablePropertyNames);
                super.set(newValue);
            }
        };
        writableProperty.bind(readOnlyProperty);
        return writableProperty;
    }

    private static void unbindPropertyIfWritable(Property property, String propertyName, String... writablePropertyNames) {
        if (Arrays.contains(writablePropertyNames, propertyName) || Arrays.contains(writablePropertyNames, "*"))
            property.unbind();
    }
}
