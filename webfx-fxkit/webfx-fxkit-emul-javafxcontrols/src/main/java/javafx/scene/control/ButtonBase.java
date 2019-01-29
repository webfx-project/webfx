package javafx.scene.control;

import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public abstract class ButtonBase extends Labeled {

    public ButtonBase() {
        super();
    }

    public ButtonBase(String text) {
        super(text);
    }

    public ButtonBase(String text, Node graphic) {
        super(text, graphic);
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Indicates that the button has been "armed" such that a mouse release
     * will cause the button's action to be invoked. This is subtly different
     * from pressed. Pressed indicates that the mouse has been
     * pressed on a Node and has not yet been released. {@code arm} however
     * also takes into account whether the mouse is actually over the
     * button and pressed.
     */
    public final ReadOnlyProperty<Boolean> armedProperty() { return armed/*.getReadOnlyProperty()*/; }
    private void setArmed(boolean value) { armed.setValue(value); }
    public final boolean isArmed() { return armedProperty().getValue(); }
    private Property<Boolean> armed = new SimpleObjectProperty<>(false)/* {
        @Override protected void invalidated() {
            pseudoClassStateChanged(ARMED_PSEUDOCLASS_STATE, get());
        }

        @Override
        public Object getBean() {
            return ButtonBase.this;
        }

        @Override
        public String getName() {
            return "armed";
        }
    }*/;

    /**
     * The button's action, which is invoked whenever the button is fired. This
     * may be due to the user clicking on the button with the mouse, or by
     * a touch event, or by a key press, or if the developer programmatically
     * invokes the {@link #fire()} method.
     */
    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() { return onAction; }
    public final void setOnAction(EventHandler<ActionEvent> value) { onActionProperty().set(value); }
    public final EventHandler<ActionEvent> getOnAction() { return onActionProperty().get(); }
    private ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() {
        @Override protected void invalidated() {
            setEventHandler(ActionEvent.ACTION, get());
        }

        @Override
        public Object getBean() {
            return ButtonBase.this;
        }

        @Override
        public String getName() {
            return "onAction";
        }
    };


    /***************************************************************************
     *                                                                         *
     * Methods                                                                 *
     *                                                                         *
     **************************************************************************/

    /**
     * Arms the button. An armed button will fire an action (whether that be
     * the action of a {@link Button} or toggling selection on a
     * {@link CheckBox} or some other behavior) on the next expected UI
     * gesture.
     *
     * @expert This function is intended to be used by experts, primarily
     *         by those implementing new Skins or Behaviors. It is not common
     *         for developers or designers to access this function directly.
     */
    public void arm() {
        setArmed(true);
    }

    /**
     * Disarms the button. See {@link #arm()}.
     *
     * @expert This function is intended to be used by experts, primarily
     *         by those implementing new Skins or Behaviors. It is not common
     *         for developers or designers to access this function directly.
     */
    public void disarm() {
        setArmed(false);
    }

    /**
     * Invoked when a user gesture indicates that an event for this
     * {@code ButtonBase} should occur.
     * <p>
     * If invoked, this method will be executed regardless of the status of
     * {@link #arm}.
     * </p>
     */
    public abstract void fire();
}
