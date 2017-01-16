package naga.fx.sun.scene.control.skin;

import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import naga.fx.scene.control.Control;
import naga.fx.sun.scene.control.MultiplePropertyChangeListenerHandler;
import naga.fx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import naga.fx.sun.scene.control.behaviour.BehaviorBase;

/**
 *
 */
public abstract class BehaviorSkinBase<C extends Control, BB extends BehaviorBase<C>> extends SkinBase<C> {
    /**
     * A static final reference to whether the platform we are on supports touch.
     */
    protected final static boolean IS_TOUCH_SUPPORTED = false; // Platform.isSupported(ConditionalFeature.INPUT_TOUCH);

    /***************************************************************************
     *                                                                         *
     * Private fields                                                          *
     *                                                                         *
     **************************************************************************/

    /**
     * The {@link BehaviorBase} that encapsulates the interaction with the
     * {@link Control} from this {@code Skin}. The {@code Skin} does not modify
     * the {@code Control} directly, but rather redirects events into the
     * {@code BehaviorBase} which then handles the events by modifying internal state
     * and public state in the {@code Control}. Generally, specific
     * {@code Skin} implementations will require specific {@code BehaviorBase}
     * implementations. For example, a ButtonSkin might require a ButtonBehavior.
     */
    private BB behavior;

    /**
     * This is part of the workaround introduced during delomboking. We probably will
     * want to adjust the way listeners are added rather than continuing to use this
     * map (although it doesn't really do much harm).
     */
    private MultiplePropertyChangeListenerHandler changeListenerHandler;



    /***************************************************************************
     *                                                                         *
     * Event Handlers / Listeners                                              *
     *                                                                         *
     **************************************************************************/


    /**
     * Forwards mouse events received by a MouseListener to the behavior.
     * Note that we use this pattern to remove some of the anonymous inner
     * classes which we'd otherwise have to create. When lambda expressions
     * are supported, we could do it that way instead (or use MethodHandles).
     */
    private final EventHandler<MouseEvent> mouseHandler =
            new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent e) {
                    final EventType<?> type = e.getEventType();

                    if (type == MouseEvent.MOUSE_ENTERED) behavior.mouseEntered(e);
                    else if (type == MouseEvent.MOUSE_EXITED) behavior.mouseExited(e);
                    else if (type == MouseEvent.MOUSE_PRESSED) behavior.mousePressed(e);
                    else if (type == MouseEvent.MOUSE_RELEASED) behavior.mouseReleased(e);
                    else if (type == MouseEvent.MOUSE_DRAGGED) behavior.mouseDragged(e);
                    else { // no op
                        throw new AssertionError("Unsupported event type received");
                    }
                }
            };

/*
    private final EventHandler<ContextMenuEvent> contextMenuHandler =
            new EventHandler<ContextMenuEvent>() {
                @Override public void handle(ContextMenuEvent event) {
                    behavior.contextMenuRequested(event);
                }
            };
*/

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Constructor for all BehaviorSkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     * @param behavior The behavior for which this Skin should defer to.
     */
    protected BehaviorSkinBase(final C control, final BB behavior) {
        super(control);

        if (behavior == null) {
            throw new IllegalArgumentException("Cannot pass null for behavior");
        }

        // Update the control and behavior
        this.behavior = behavior;

        // We will auto-add listeners for wiring up Region mouse events to
        // be sent to the behavior
        control.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseHandler);
        control.addEventHandler(MouseEvent.MOUSE_EXITED, mouseHandler);
        control.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseHandler);
        control.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseHandler);
        control.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseHandler);

        //control.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, contextMenuHandler);
    }



    /***************************************************************************
     *                                                                         *
     * Public API (from Skin)                                                  *
     *                                                                         *
     **************************************************************************/

    /** {@inheritDoc} */
    public final BB getBehavior() {
        return behavior;
    }

    /** {@inheritDoc} */
    @Override public void dispose() {
        // unhook listeners
        if (changeListenerHandler != null) {
            changeListenerHandler.dispose();
        }

        C control = getSkinnable();
        if (control != null) {
            control.removeEventHandler(MouseEvent.MOUSE_ENTERED, mouseHandler);
            control.removeEventHandler(MouseEvent.MOUSE_EXITED, mouseHandler);
            control.removeEventHandler(MouseEvent.MOUSE_PRESSED, mouseHandler);
            control.removeEventHandler(MouseEvent.MOUSE_RELEASED, mouseHandler);
            control.removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseHandler);
        }

        if (behavior != null) {
            behavior.dispose();
            behavior = null;
        }

        super.dispose();
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Subclasses can invoke this method to register that we want to listen to
     * property change events for the given property.
     *
     * @param property
     * @param reference
     */
    protected final void registerChangeListener(ObservableValue<?> property, String reference) {
        if (changeListenerHandler == null) {
            changeListenerHandler = new MultiplePropertyChangeListenerHandler(p -> {
                handleControlPropertyChanged(p);
                return null;
            });
        }
        changeListenerHandler.registerChangeListener(property, reference);
    }

    /**
     * Skin subclasses will override this method to handle changes in corresponding
     * control's properties.
     */
    protected void handleControlPropertyChanged(String propertyReference) {
        // no-op
    }

}

