package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util;

import dev.webfx.platform.util.collection.Collections;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public final class FxEvents {

    private static MouseButton lastDownButton = MouseButton.NONE;

    public static MouseEvent toFxMouseEvent(elemental2.dom.MouseEvent me, String type) {
        EventType<MouseEvent> eventType;
        boolean newPrimaryButtonDown =   (me.buttons & 1) != 0;
        boolean newMiddleButtonDown =    (me.buttons & 2) != 0;
        boolean newSecondaryButtonDown = (me.buttons & 4) != 0;
        MouseButton downButton = newPrimaryButtonDown ? MouseButton.PRIMARY :
            newMiddleButtonDown ? MouseButton.MIDDLE :
                newSecondaryButtonDown ? MouseButton.SECONDARY : MouseButton.NONE;
        switch (type) {
            case "mousedown":
            case "pointerdown":
                eventType = MouseEvent.MOUSE_PRESSED;
                break;
            case "mouseup":
            case "pointerup":
                eventType = MouseEvent.MOUSE_RELEASED;
                // JavaFX differs from HTML in that on mouse released, the down button refers to the last button that
                // was down, and its new state must be false (more explanation below).
                downButton = lastDownButton;
                if (downButton == MouseButton.PRIMARY)
                    newPrimaryButtonDown = false;
                else if (downButton == MouseButton.MIDDLE)
                    newMiddleButtonDown = false;
                else if (downButton == MouseButton.SECONDARY)
                    newSecondaryButtonDown = false;
                // MOUSE_PRESSED is triggering a gestureStarted event in the scene mouse handler which will make it enter
                // in pdr (press-drag-release) mode. To exit that mode on MOUSE_RELEASED, it's important that the original
                // button is down in the generated mouse event (this is how the mouse handler will detect the end of pdr).
                break;
            case "mouseenter":
            case "pointerenter":
                eventType = MouseEvent.MOUSE_ENTERED;
                break;
            case "mouseleave":
            case "pointerleave":
                eventType = MouseEvent.MOUSE_EXITED;
                break;
            case "mousemove":
            case "pointermove":
                eventType = downButton != MouseButton.NONE ? MouseEvent.MOUSE_DRAGGED : MouseEvent.MOUSE_MOVED;
                break;
            default:
                return null;
        }
        lastDownButton = downButton;
        return new MouseEvent(null, null, eventType, me.pageX, me.pageY, me.screenX, me.screenY, downButton,
            1, me.shiftKey, me.ctrlKey, me.altKey, me.metaKey,
            newPrimaryButtonDown,
            newMiddleButtonDown,
            newSecondaryButtonDown,
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
