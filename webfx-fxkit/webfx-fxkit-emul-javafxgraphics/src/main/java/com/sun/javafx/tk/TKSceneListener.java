package com.sun.javafx.tk;

/**
 * TKSceneListener - Listener for the Scene Peer TKScene to pass updates and events back to the scene
 *
 */
public interface TKSceneListener {

    /**
     * The scenes peer's location have changed so we need to update the scene
     *
     * @param x the new X
     * @param y The new Y
     */
    void changedLocation(float x, float y);

    /**
     * The scenes peer's size have changed so we need to update the scene
     *
     * @param width The new Width
     * @param height The new Height
     */
    void changedSize(float width, float height);

    /**
     * Pass a mouse event to the scene to handle
     */

/*
    void mouseEvent(EventType<MouseEvent> type, double x, double y, double screenX, double screenY,
                    MouseButton button, boolean popupTrigger, boolean synthesized,
                    boolean shiftDown, boolean controlDown, boolean altDown, boolean metaDown,
                    boolean primaryDown, boolean middleDown, boolean secondaryDown);
*/

    /**
     * Pass a key event to the scene to handle
     */
/*
    void keyEvent(KeyEvent keyEvent);
*/

    /**
     * Pass an input method event to the scene to handle
     */
/*
    void inputMethodEvent(EventType<InputMethodEvent> type,
                                 ObservableList<InputMethodTextRun> composed, String committed,
                                 int caretPosition);
*/

/*
    void scrollEvent(
            EventType<ScrollEvent> eventType, double scrollX, double scrollY,
            double totalScrollX, double totalScrollY,
            double xMultiplier, double yMultiplier, int touchCount,
            int scrollTextX, int scrollTextY,
            int defaultTextX, int defaultTextY,
            double x, double y, double screenX, double screenY,
            boolean _shiftDown, boolean _controlDown,
            boolean _altDown, boolean _metaDown,
            boolean _direct, boolean _inertia);
*/

/*
    void menuEvent(double x, double y, double xAbs, double yAbs,
                          boolean isKeyboardTrigger);
*/

/*
    void zoomEvent(
            EventType<ZoomEvent> eventType,
            double zoomFactor, double totalZoomFactor,
            double x, double y, double screenX, double screenY,
            boolean _shiftDown, boolean _controlDown,
            boolean _altDown, boolean _metaDown,
            boolean _direct, boolean _inertia);
*/

/*
    void rotateEvent(
            EventType<RotateEvent> eventType, double angle, double totalAngle,
            double x, double y, double screenX, double screenY,
            boolean _shiftDown, boolean _controlDown,
            boolean _altDown, boolean _metaDown,
            boolean _direct, boolean _inertia);
*/

/*
    void swipeEvent(
            EventType<SwipeEvent> eventType, int touchCount,
            double x, double y, double screenX, double screenY,
            boolean _shiftDown, boolean _controlDown,
            boolean _altDown, boolean _metaDown, boolean _direct);
*/

/*
    void touchEventBegin(
            long time, int touchCount, boolean isDirect,
            boolean _shiftDown, boolean _controlDown,
            boolean _altDown, boolean _metaDown);
*/

/*
    void touchEventNext(
            TouchPoint.State state, long touchId,
            double x, double y, double xAbs, double yAbs);
*/

/*
    void touchEventEnd();
*/

/*
    Accessible getSceneAccessible();
*/
}
