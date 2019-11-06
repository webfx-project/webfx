package webfx.kit.mapper.peers.javafxgraphics.gwt.util;

import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import webfx.platform.shared.util.collection.Collections;

public final class FxEvents {

    private static final boolean[] BUTTON_DOWN_STATES = {false, false, false, false};

    public static MouseEvent toFxMouseEvent(elemental2.dom.MouseEvent me, String type) {
        MouseButton button;
        switch (me.button) {
            case 0: button = MouseButton.PRIMARY; break;
            case 1: button = MouseButton.MIDDLE; break;
            case 2: button = MouseButton.SECONDARY; break;
            default: button = MouseButton.NONE;
        }
        EventType<MouseEvent> eventType;
        switch (type) {
            case "mousedown": eventType = MouseEvent.MOUSE_PRESSED; BUTTON_DOWN_STATES[button.ordinal()] = true; break;
            case "mouseup": eventType = MouseEvent.MOUSE_RELEASED; BUTTON_DOWN_STATES[button.ordinal()] = false; break;
            case "mouseenter": eventType = MouseEvent.MOUSE_ENTERED; break;
            case "mouseleave": eventType = MouseEvent.MOUSE_EXITED; break;
            case "mousemove": eventType = BUTTON_DOWN_STATES[button.ordinal()] ? MouseEvent.MOUSE_DRAGGED : MouseEvent.MOUSE_MOVED; break;
            default: return null;
        }
        return new MouseEvent(null, null, eventType, me.pageX, me.pageY, me.screenX, me.screenY, button,
                1, me.shiftKey, me.ctrlKey, me.altKey, me.metaKey,
                BUTTON_DOWN_STATES[MouseButton.PRIMARY.ordinal()],
                BUTTON_DOWN_STATES[MouseButton.MIDDLE.ordinal()],
                BUTTON_DOWN_STATES[MouseButton.SECONDARY.ordinal()],
                false,
                false,
                false,
                null);
    }

    public static DragEvent toDragEvent(elemental2.dom.MouseEvent me, EventType<DragEvent> dragEventType, Node fxNode) {
        Scene.DnDGesture dndGesture = fxNode.getScene().getOrCreateDndGesture();
        return new DragEvent(
                null // source
                , fxNode // target
                , dragEventType // eventType
                , dndGesture.getOrCreateDragboard() // Dragboard
                , me.pageX // x
                , me.pageY // y
                , me.screenX // screenX
                , me.screenY // screenY
                , dndGesture.acceptedTransferMode != null ? dndGesture.acceptedTransferMode : Collections.first(dndGesture.sourceTransferModes) // transferMode
                , dndGesture.source // gestureSource
                , dndGesture.target // gestureTarget
                , null // pickResult
        );
    }

}
