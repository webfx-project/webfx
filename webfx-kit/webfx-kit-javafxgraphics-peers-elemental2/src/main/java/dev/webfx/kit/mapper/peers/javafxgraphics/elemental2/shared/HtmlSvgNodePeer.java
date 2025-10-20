package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared;

import com.sun.javafx.cursor.CursorType;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.event.EventUtil;
import com.sun.javafx.tk.quantum.GestureRecognizers;
import com.sun.javafx.tk.quantum.SwipeGestureRecognizer;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerImpl;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.measurable.Measurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.ScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.UserInteraction;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.svg.SvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.*;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.util.Booleans;
import dev.webfx.platform.util.Strings;
import dev.webfx.platform.util.collection.Collections;
import elemental2.dom.*;
import elemental2.dom.MouseEvent;
import elemental2.dom.TouchEvent;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.input.*;
import javafx.scene.input.DragEvent;
import javafx.scene.text.Font;
import javafx.scene.transform.Transform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlSvgNodePeer
        <E extends Element, N extends Node, NB extends NodePeerBase<N, NB, NM>, NM extends NodePeerMixin<N, NB, NM>>
        extends NodePeerImpl<N, NB, NM> {

    private static final String DISABLED_CSS_CLASS = "disabled";

    private final E element;
    private Element container;
    private boolean containerInvisible;
    private Element childrenContainer;
    protected DomType containerType;

    public HtmlSvgNodePeer(NB base, E element) {
        super(base);
        this.element = element;
        setContainer(element);
        setChildrenContainer(element);
    }

    public final E getElement() {
        return element;
    }

    public void setContainer(Element container) {
        this.container = container;
        containerType = "SVG".equalsIgnoreCase(container.tagName) || this instanceof SvgNodePeer ? DomType.SVG : DomType.HTML;
        storePeerInElement(container);
    }

    public void makeContainerInvisible() {
        // Applying display = "contents" on the container to make sure it has no visible impact (ex: no extra space
        // around the box) inherited from the user agent box style model
        if (container instanceof HTMLElement && !container.tagName.startsWith("fx-")) // No need for fx-xxx custom elements as they don't inherit any box style
            ((HTMLElement) container).style.display = "contents";
        containerInvisible = true;
    }

    protected void storePeerInElement(Object element) {
        HtmlUtil.setJsJavaObjectAttribute(element, "nodePeer", this);
    }

    protected static NodePeer getPeerFromElement(Object element) {
        return element == null ? null : (NodePeer) HtmlUtil.getJsJavaObjectAttribute(element, "nodePeer");
    }

    public static NodePeer getPeerFromElementOrParents(Element element, boolean checkParentsIfRemovedFromScene) {
        NodePeer nodePeer = null;
        // Retrieving the node peer from the element (if not found, we search in parents)
        for (elemental2.dom.Node n = element; nodePeer == null && n != null; n = n.parentNode)
            nodePeer = getPeerFromElement(n);
        // If we found it, we need to check that it's still an active peer bound to the scene graph.
        if (checkParentsIfRemovedFromScene && nodePeer != null) {
            Node node = nodePeer.getNode();
            Scene scene = node.getScene();
            // If the node has been removed from the scene graph, we try to search again from its highest possible element
            if (scene == null) {
                while (true) {
                    Parent parent = node.getParent();
                    if (parent == null)
                        return getPeerFromElementOrParents((Element) ((HtmlSvgNodePeer) node.getNodePeer()).container.parentNode, true);
                    node = parent;
                }
            }
        }
        return nodePeer;
    }

    public Element getContainer() {
        return container;
    }

    public Element getVisibleContainer() {
        return containerInvisible ? element : container;
    }

    public Element getChildrenContainer() {
        return childrenContainer;
    }

    public void setChildrenContainer(Element childrenContainer) {
        this.childrenContainer = childrenContainer;
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        ObservableMap<Object, Object> nodeProperties = getNodeProperties();
        onNodePropertiesChanged(nodeProperties);
        nodeProperties.addListener((InvalidationListener) observable -> onNodePropertiesChanged(nodeProperties));
    }

    protected ObservableMap<Object, Object> getNodeProperties() {
        return getNode().getProperties();
    }

    protected void onNodePropertiesChanged(ObservableMap<Object, Object> nodeProperties) {
    }

    protected ScenePeer getScenePeer() {
        Scene scene = getNode().getScene();
        return scene == null ? null : scene.impl_getPeer();
    }

    /******************************************** Drag & drop support *************************************************/

    private EventListener dragStartListener;
    @Override
    public void updateOnDragDetected(EventHandler<? super javafx.scene.input.MouseEvent> eventHandler) {
        setElementAttribute("draggable", eventHandler == null ? null : "true");
        dragStartListener = updateHtmlEventListener(dragStartListener, "dragstart", eventHandler, evt -> {
            MouseEvent me = (MouseEvent) evt;
            DragboardDataTransferHolder.setDragboardDataTransfer(me.dataTransfer);
            javafx.scene.input.MouseEvent fxMouseEvent = FxEvents.toFxMouseEvent(me, "mousemove");
            eventHandler.handle(fxMouseEvent);
            if (fxMouseEvent.isConsumed())
                evt.stopPropagation();
            // If no drag done handler has been registered, registering a fake one to detect when the drag ends
            if (dragEndListener == null)
                updateOnDragDone(e -> {}); // This is to ensure that clearDndGesture() will be called in callFxDragEventHandler()
        });
    }

    private EventListener dragEnterListener;
    @Override
    public void updateOnDragEntered(EventHandler<? super DragEvent> eventHandler) {
        dragEnterListener = updateHtmlDragEventListener(dragEnterListener, "dragenter", DragEvent.DRAG_ENTERED, eventHandler);
    }

    private EventListener dragOverListener;
    @Override
    public void updateOnDragOver(EventHandler<? super DragEvent> eventHandler) {
        dragOverListener = updateHtmlEventListener(dragOverListener, "dragover", eventHandler, evt -> callFxDragEventHandler(evt, DragEvent.DRAG_OVER, dragEvent -> {
            eventHandler.handle(dragEvent);
            if (dragEvent.isAccepted())
                evt.preventDefault();
        }));
    }

    private EventListener dropListener;
    @Override
    public void updateOnDragDropped(EventHandler<? super DragEvent> eventHandler) {
        dropListener = updateHtmlEventListener(dropListener, "drop", eventHandler, evt -> {
            evt.preventDefault();
            callFxDragEventHandler(evt, DragEvent.DRAG_DROPPED, eventHandler);
            // Also simulating JavaFX behavior which calls drag exit after dropped
            if (dragLeaveListener != null)
                dragLeaveListener.handleEvent(evt);
        });
    }

    private EventListener dragLeaveListener;
    @Override
    public void updateOnDragExited(EventHandler<? super DragEvent> eventHandler) {
        dragLeaveListener = updateHtmlDragEventListener(dragLeaveListener, "dragleave", DragEvent.DRAG_EXITED, eventHandler);
    }

    private EventListener dragEndListener;
    @Override
    public void updateOnDragDone(EventHandler<? super DragEvent> eventHandler) {
        dragEndListener = updateHtmlDragEventListener(dragEndListener, "dragend", DragEvent.DRAG_DONE, eventHandler);
    }

    private EventListener updateHtmlEventListener(EventListener previousListener, String type, Object eventHandler, EventListener newListener) {
        if (previousListener != null) // Otherwise TeaVM JS output raises an exception
            element.removeEventListener(type, previousListener);
        if (eventHandler == null)
            return null;
        element.addEventListener(type, newListener);
        return newListener;
    }

    private EventListener updateHtmlDragEventListener(EventListener previousListener, String type, EventType<DragEvent> dragEventType, EventHandler<? super DragEvent> eventHandler) {
        return updateHtmlEventListener(previousListener, type, eventHandler, evt -> callFxDragEventHandler(evt, dragEventType, eventHandler));
    }

    private void callFxDragEventHandler(Event evt, EventType<DragEvent> dragEventType, EventHandler<? super DragEvent> eventHandler) {
        MouseEvent me = (MouseEvent) evt;
        // HTML also triggers dragenter and dragleave events on children as opposed to JavaFX, so we just ignore them
        if (!element.contains((elemental2.dom.Node) me.relatedTarget)) { // thanks to this condition,
            // Otherwise we call the JavaFX handler
            DragboardDataTransferHolder.setDragboardDataTransfer(me.dataTransfer);
            DragEvent dragEvent = FxEvents.toDragEvent(me, dragEventType, getNode());
            eventHandler.handle(dragEvent);
            if (dragEvent.isConsumed())
                evt.stopPropagation();
            // Clearing DndGesture to ensure that if a new drag starts (especially from an external application), we don't keep wrong information (such as previous gestureSource)
            if (dragEventType == DragEvent.DRAG_DONE)
                getNode().getScene().clearDndGesture();
        }
    }

    /************************************* End of "Drag & drop support" section ***************************************/

    public static boolean passOnToFx(javafx.event.EventTarget eventTarget, javafx.event.Event fxEvent) {
        // Ensuring that propagateToPeerEvent is reset to null before passing the event to JavaFX
        javafx.event.Event.setPropagateToPeerEvent(null); // see Event comments for more explanation
        // Passing the event to JavaFX and checking if it has been consumed by a JavaFX event handler
        boolean stopPropagation = isFxEventConsumed(EventUtil.fireEvent(eventTarget, fxEvent));
        // The value returned by this method indicates if we should stop the propagation of the event, or not (if not,
        // it will be passed to the default browser event handling). By default, we stop the propagation of any event
        // consumed by JavaFX. However, in some cases (see Event comments), a control can ask to bypass this default
        // behavior and to not stop the propagation of an event that it consumed; but pass it back to the browser and
        // therefore eventually to the peer.
        if (javafx.event.Event.getPropagateToPeerEvent() != null)
            stopPropagation = false;
        return stopPropagation;
    }

    private static boolean isFxEventConsumed(javafx.event.Event fxEvent) {
        // Note: the event returned by JavaFX EventUtil.fireEvent() may be null, and in that case, this means that the
        // passed event has been consumed. When it returns a non-null event, we need to call isConsumed() to know.
        return fxEvent == null || fxEvent.isConsumed();
    }

    /************************************************* Focus mapping **************************************************/

    @Override
    public void requestFocus() { // Called when the JavaFX application code requests the node to have the focus.
        // We transmit that request in HTML.
        boolean focusAccepted = requestHtmlFocus();
        // We check if that request has been accepted.
        if (!focusAccepted) { // Sometimes, it can be refused!
            // One of the possible reasons is that the DOM element is not visible, which can happen because JavaFX
            // accepts focus requests on invisible nodes, but not HTML. 2 issues were observed, for example, on the
            // Modality login window: 1) the password field was not getting the focus after entering the email and
            // pressing the button or Enter because initially hidden 2) the email field was not getting back the focus
            // after flipping back from SSO login

            // Ex: the email TextField in Modality login window
            // while flipping back from SSO login, or even the password field which appears under the Email field quickly after
            // entering the email but the first focus request fails in the browser).
            // ).
            // To fix this difference between JavaFX and HTML focus states, we retry periodically
            // until either the JavaFX focus changes (=> initial request is not relevant anymore), or the HTML request
            // is finally accepted (ex: when the Email/Password flipping side becomes visible).
            UiScheduler.schedulePeriodic(250, scheduled -> {
                if (!isJavaFxFocusOwner() || requestHtmlFocus()) {
                    scheduled.cancel();
                }
            });
        }
    }

    private boolean requestHtmlFocus() {
        // We call focus() on the focus element associated with this node
        Element focusableElement = getHtmlFocusableElement();
        focusableElement.focus();
        // We check if that element now has the focus and return the test result.
        boolean success = focusableElement.ownerDocument.activeElement == focusableElement;
        if (!success && !focusableElement.hasAttribute("tabindex")) {
            focusableElement.setAttribute("tabindex", "0");
            focusableElement.focus();
            if (focusableElement.ownerDocument.activeElement == focusableElement) {
                Console.log("⚠️ tabindex='0' has automatically been added to fulfill requestHtmlFocus() on node = " + getNode());
                return true;
            }
        }
        //Console.log((success ? "❤️❤️❤️❤️❤️" : "😱😱😱😱😱") + " requestHtmlFocus(), node = " + getNode());
        return success;
    }

    protected Element getHtmlFocusableElement() {
        return getElement();
    }

    public Node getJavaFxFocusableNode() {
        // The node we return as focusable is the first one that is focus-traversable from this node or its parents.
        for (Node node = getNode(); node != null; node = node.getParent()) {
            if (node.isFocusTraversable()) { // indicates that this node accepts focus
                return node;
            }
        }
        return null;
    }

    public boolean isJavaFxFocusOwner() {
        Node focusableNode = getJavaFxFocusableNode();
        Scene scene = focusableNode == null ? null : focusableNode.getScene();
        return scene != null && scene.getFocusOwner() == focusableNode;
    }

    public void setJavaFxFocusOwner() {
        Node focusableNode = getJavaFxFocusableNode();
        if (focusableNode != null)
            setJavaFxFocusOwner(focusableNode);
    }

    protected void setJavaFxFocusOwner(Node focusOwner) {
        getNode().getScene().focusOwnerProperty().setValue(focusOwner);
    }

    /**************************************** End of "Focus mapping" section ******************************************/


    @Override
    public void updateLayoutX(Number layoutX) {
        updateAllNodeTransforms();
    }

    static {
        EventHandlerManager.setEventSourcesListener((eventType, eventSource) -> {
            if (eventSource instanceof Node) {
                EventType<?> superType = eventType.getSuperType();
                if (superType == ScrollEvent.ANY) // registering for scroll events
                    callPeerWhenReady(eventSource, HtmlSvgNodePeer::installScrollListeners);
                else if  (superType == SwipeEvent.ANY) // registering for swipe events
                    callPeerWhenReady(eventSource, HtmlSvgNodePeer::installSwipeListeners);
                else if (superType == javafx.scene.input.TouchEvent.ANY || eventType == javafx.scene.input.MouseEvent.MOUSE_DRAGGED)
                    callPeerWhenReady(eventSource, peer -> peer.installTouchListeners(false));
            }
        });
    }

    static void callPeerWhenReady(Object nodeSource, Consumer<HtmlSvgNodePeer> peerCaller) {
        ((Node) nodeSource).onNodePeerReady(peer -> {
            if (peer instanceof HtmlSvgNodePeer)
                peerCaller.accept(((HtmlSvgNodePeer) peer));
        });
    }

    private void installScrollListeners() {
        // Listening mouse wheel scroll only for now
        element.onwheel = e -> {
            WheelEvent we = (WheelEvent) e;
            N node = getNode();
            ScrollEvent fxEvent = new ScrollEvent(node, node, ScrollEvent.SCROLL, we.pageX, we.pageY, we.screenX, we.screenY,
                    we.shiftKey, we.ctrlKey, we.altKey, we.metaKey, true,false, we.deltaX, we.deltaY, we.deltaX, we.deltaY,
                    ScrollEvent.HorizontalTextScrollUnits.NONE, 0, ScrollEvent.VerticalTextScrollUnits.NONE, 0, 0, new PickResult(node, we.pageX, we.pageY));
            boolean stopPropagation = passOnToFx(node, fxEvent);
            if (stopPropagation) {
                e.stopPropagation();
                e.preventDefault();
            }
            return false;
        };
    }

    private void installSwipeListeners() {
        installTouchListeners(true);
    }

    private boolean touchListenersInstalled;

    private void installTouchListeners(boolean swipe) {
        if (swipe)
            getNode().getProperties().put("webfx-swipe", true);
        if (!touchListenersInstalled) {
            installTouchListeners(element, getNode());
            touchListenersInstalled = true;
        }
    }

    public static void installTouchListeners(EventTarget htmlTarget, javafx.event.EventTarget fxTarget) {
        registerTouchListener(htmlTarget, "touchstart", fxTarget);
        registerTouchListener(htmlTarget, "touchmove", fxTarget);
        registerTouchListener(htmlTarget, "touchend", fxTarget);
        registerTouchListener(htmlTarget, "touchcancel", fxTarget);
    }

    private static void registerTouchListener(EventTarget htmlTarget, String type, javafx.event.EventTarget fxTarget) {
        // We don't enable the browsers' built-in touch scrolling features because this is not a standard behavior in
        // JavaFX, and this can interfere with the user experience, especially with games.
        // Note that this will cause a downgrade in Lighthouse.
        boolean passive = false; // May be set to true in some cases to improve Lighthouse score
        AddEventListenerOptions passiveOption = AddEventListenerOptions.create();
        passiveOption.setPassive(passive);
        htmlTarget.addEventListener(type, e -> {
            UserInteraction.setUserInteracting(true);
            boolean fxConsumed = passHtmlTouchEventOnToFx((TouchEvent) e, type, fxTarget);
            if (fxConsumed) {
                e.stopPropagation();
                if (!UserInteraction.nextUserRunnableRequiresTouchEventDefault()) {
                    if (passive) {
                        Console.log("Couldn't prevent event default in passive mode");
                    } else {
                        e.preventDefault(); // doesn't work in passive mode
                    }
                }
            }
            UserInteraction.setUserInteracting(false);
        }, passiveOption);
    }

    protected static boolean passHtmlTouchEventOnToFx(TouchEvent e, String type, javafx.event.EventTarget fxTarget) {
        javafx.scene.input.TouchEvent fxTouchEvent = toFxTouchEvent(e, type, fxTarget);
        return passOnToFx(fxTarget, fxTouchEvent);
    }

    private static javafx.scene.input.TouchEvent toFxTouchEvent(TouchEvent e, String type, javafx.event.EventTarget fxTarget) {
        EventType<javafx.scene.input.TouchEvent> eventType;
        switch (type) {
            case "touchstart": eventType = javafx.scene.input.TouchEvent.TOUCH_PRESSED; break;
            case "touchmove": eventType = javafx.scene.input.TouchEvent.TOUCH_MOVED; break;
            case "touchend":
            case "touchcancel":
            default : eventType = javafx.scene.input.TouchEvent.TOUCH_RELEASED; break;
        }
        List<TouchPoint> touchPoints = e.changedTouches.asList().stream().map(t -> toFxTouchPoint(t, e, fxTarget)).collect(Collectors.toList());
        return new javafx.scene.input.TouchEvent(null, fxTarget, eventType, touchPoints.get(0),
                touchPoints,
                0, e.shiftKey, e.ctrlKey, e.altKey, e.metaKey);
    }

    private static final GestureRecognizers GESTURE_RECOGNIZERS = new GestureRecognizers();

    private static TouchPoint toFxTouchPoint(Touch touch, TouchEvent e, javafx.event.EventTarget fxTarget) {
        TouchPoint.State state = TouchPoint.State.STATIONARY;
        //if (e.changedTouches.asList().contains(touch)) {
        switch (e.type) {
            case "touchstart": state = TouchPoint.State.PRESSED; break;
            case "touchend": state = TouchPoint.State.RELEASED; break;
            case "touchmove": state = TouchPoint.State.MOVED; break;
        }
        //}
        double touchX = touch.pageX;
        double touchY = touch.pageY;
        int touchId = touch.identifier;
        boolean swipe = fxTarget instanceof Node && Boolean.TRUE.equals(((Node) fxTarget).getProperties().get("webfx-swipe"));
        if (swipe) {
            long time = (long) (e.timeStamp * 1_000_000);
            SwipeGestureRecognizer.CURRENT_TARGET = fxTarget;
            if (state == TouchPoint.State.PRESSED)
                GESTURE_RECOGNIZERS.notifyBeginTouchEvent(time, 0, false, 1);
            GESTURE_RECOGNIZERS.notifyNextTouchEvent(time, state.name(), touchId, (int) touchX, (int) touchY, (int) touch.screenX, (int) touch.screenY);
            if (state == TouchPoint.State.RELEASED)
                GESTURE_RECOGNIZERS.notifyEndTouchEvent(time);
        }
        PickResult pickResult = new PickResult(fxTarget, touchX, touchY);
        return new TouchPoint(touchId, state, touchX, touchY, touch.screenX, touch.screenY, fxTarget, pickResult);
    }

    @Override
    public void updateLayoutY(Number layoutY) {
        updateAllNodeTransforms();
    }

    @Override
    public void updateTranslateX(Number translateX) {
        updateAllNodeTransforms();
    }

    @Override
    public void updateTranslateY(Number translateY) {
        updateAllNodeTransforms();
    }

    @Override
    public void updateScaleX(Number scaleX) {
        updateAllNodeTransforms();
    }

    @Override
    public void updateScaleY(Number scaleX) {
        updateAllNodeTransforms();
    }

    @Override
    public void updateRotate(Number rotate) {
        updateAllNodeTransforms();
    }

    @Override
    public void updateTransforms(List<Transform> transforms, ListChangeListener.Change<Transform> change) {
        updateAllNodeTransforms();
    }

    private void updateAllNodeTransforms() {
        updateAllNodeTransforms(getNodePeerBase().getNode().getAllNodeTransforms());
    }

    @Override
    public void updateAllNodeTransforms(List<Transform> localToParentTransforms) {
        boolean isSvg = containerType == DomType.SVG;
        setElementAttribute("transform", isSvg ? SvgTransforms.toSvgTransforms(localToParentTransforms) : HtmlTransforms.toHtmlTransforms(localToParentTransforms));
    }

    @Override
    public boolean isTreeVisible() {
        if (container instanceof HTMLElement)
            return ((HTMLElement) container).offsetParent != null;
        return true;
    }

    protected String getStyleAttribute(String name) {
        if (containerType == DomType.HTML) {
            switch (name) {
                case "pointer-events": return "pointerEvents";
                case "clip-path": return "clipPath";
                case "font-family": return "fontFamily";
                case "font-style": return "fontStyle";
                case "font-weight": return "fontWeight";
                case "font-size": return "fontSize";
                case "mix-blend-mode": return "mixBlendMode";
                case "visibility":
                case "opacity":
                case "filter":
                case "transform":
                    return name;
            }
        }
        return null;
    }

    protected void setElementStyleAttribute(String name, Object value) {
        HtmlUtil.setStyleAttribute(getVisibleContainer(), name, value);
    }

    @Override
    public void updateMouseTransparent(Boolean mouseTransparent) {
        setElementAttribute("pointer-events", mouseTransparent ? "none" : null);
    }

    @Override
    public void updateId(String id) {
        setElementAttribute("id", id);
    }

    @Override
    public void updateVisible(Boolean visible) {
        setElementAttribute("visibility", visible ? null : "hidden");
    }

    @Override
    public void updateOpacity(Double opacity) {
        setElementAttribute("opacity", opacity == 1d ? null : opacity);
    }

    @Override
    public void updateDisabled(Boolean disabled) {
        // Here we just tag/untag the element with the "disabled" CSS class, so CSS can manage the disabled appearance.
        DOMTokenList classList = getElement().classList;
        boolean disabledCssClassPresent = classList.contains(DISABLED_CSS_CLASS);
        if (Booleans.isTrue(disabled)) {
            if (!disabledCssClassPresent)
                classList.add(DISABLED_CSS_CLASS);
            // We also set the disabled attribute
            getElement().setAttribute("disabled", "true");
        } else {
            if (disabledCssClassPresent)
                classList.remove(DISABLED_CSS_CLASS);
            getElement().removeAttribute("disabled");
        }
    }

    @Override
    public void updateStyle(String style) {

    }

    private HtmlSvgNodePeer clipPeer;

    @Override
    public void updateClip(Node clip) {
        if (clipPeer != null) {
            clipPeer.clipNodes.remove(getNode());
            clipPeer.cleanClipMaskIfUnused();
        }
        if (clip == null) {
            applyClipPath(null);
            applyClipMask(null);
        } else {
            clipPeer = (HtmlSvgNodePeer) clip.getOrCreateAndBindNodePeer();
            clipPeer.bindAsClip(getNode());
        }
    }

    protected boolean clip; // true when this node is actually used as a clip (=> not part of the scene graph)
    protected String clipPath;
    protected Element clipMask;
    private static int clipMaskSeq;
    protected List<Node> clipNodes; // Contains the list of nodes that use this node as a clip

    private void bindAsClip(Node clipNode) {
        clip = true;
        if (clipNodes == null)
            clipNodes = new ArrayList<>();
        if (!clipNodes.contains(clipNode))
            clipNodes.add(clipNode);
        applyClipToClipNode(clipNode);
    }

    protected final boolean isClip() {
        return clip;
    }

    protected final void applyClipClipNodes() { // Should be called when this node is a clip and that its properties have changed
        clipPath = null; // To force computation
        N thisClip = getNode();
        for (Iterator<Node> it = clipNodes.iterator(); it.hasNext(); ) {
            Node clipNode = it.next();
            if (clipNode.getClip() == thisClip) // checking the node is still using that clip
                applyClipToClipNode(clipNode);
            else // Otherwise we remove that node from the clip nodes
                it.remove();
        }
        cleanClipMaskIfUnused();
    }

    private void cleanClipMaskIfUnused() {
        if (clipMask != null && clipNodes.isEmpty()) {
            getSvgRoot().getDefsElement().removeChild(clipMask);
            clipMask = null;
        }
    }

    private void applyClipToClipNode(Node clipNode) {
        getNode().setScene(clipNode.getScene()); // Ensuring this clip node as the same scene as the node it is applied
        // A clip can be applied either through a clip path or through a svg mask
        HtmlSvgNodePeer clipPeer = (HtmlSvgNodePeer) clipNode.getNodePeer();
        clipPeer.applyClipPath(getClipPath());
        clipPeer.applyClipMask(getClipMask());
    }

    private String getClipPath() {
        if (clipPath == null)
            clipPath = computeClipPath();
        return clipPath;
    }

    public String computeClipPath() { // To override for nodes that can be used as clip (ex: rectangle, circle, etc...)
        return null;
    }

    protected void applyClipPath(String clipPah) {
        setElementAttribute("clip-path", clipPah);
    }

    private Element getClipMask() {
        if (clipMask == null) {
            clipMask = computeClipMask();
            if (clipMask != null) {
                clipMask.setAttribute("id", "mask-" + ++clipMaskSeq);
                getSvgRoot().addDef(clipMask);
            }
        }
        return clipMask;
    }

    protected abstract SvgRoot getSvgRoot();

    public Element computeClipMask() { // To override for nodes that can be used as clip (ex: rectangle, circle, etc...)
        return null;
    }

    protected void applyClipMask(Element clipMask) {
        setElementStyleAttribute("mask", SvgUtil.getDefUrl(clipMask));
    }

    @Override
    public void updateCursor(Cursor cursor) {
        setElementStyleAttribute("cursor", toCssCursor(cursor));
    }

    @Override
    public void updateBlendMode(BlendMode blendMode) {
        setElementStyleAttribute("mix-blend-mode", toSvgBlendMode(blendMode));
    }

    @Override
    public void updateEffect(Effect effect) {
        setElementAttribute("filter", effect == null ? null : toFilter(effect));
    }

    protected abstract String toFilter(Effect effect);

    @Override
    public void updateStyleClass(List<String> styleClass, ListChangeListener.Change<String> change) {
        if (change == null)
            addToElementClassList(styleClass);
        else while (change.next()) {
            if (change.wasRemoved())
                removeFromElementClassList(change.getRemoved());
            if (change.wasAdded())
                addToElementClassList(change.getAddedSubList());
        }
    }

    private void addToElementClassList(List<String> styleClass) {
        if (!Collections.isEmpty(styleClass))
            element.classList.add(Collections.toArray(styleClass, String[]::new));
    }

    private void removeFromElementClassList(List<String> styleClass) {
        if (!Collections.isEmpty(styleClass))
            element.classList.remove(Collections.toArray(styleClass, String[]::new));
    }

    protected void setElementTextContent(String textContent) {
        String text = Strings.toSafeString(textContent);
        if (!Objects.equals(element.textContent, text)) {
            element.textContent = text; // Using a safe string to avoid "undefined" with IE
            clearLayoutCache();
        }
    }

    protected void clearLayoutCache() {
        if (this instanceof Measurable)
            ((Measurable) this).clearCache();
    }

    /* String attributes */

    protected void setElementAttribute(String name, String value, String skipValue) {
        if (skipValue != null && Objects.equals(value, skipValue))
            value = null;
        setElementAttribute(name, value);
    }

    protected void setElementAttribute(String name, String value) {
        String styleAttribute = getStyleAttribute(name);
        if (styleAttribute != null)
            setElementStyleAttribute(styleAttribute, value);
        else
            setElementAttribute(getVisibleContainer(), name, value);
    }

    protected static void setElementAttribute(Element e, String name, String value) {
        if (value == null)
            e.removeAttribute(name);
        else
            e.setAttribute(name, value);
    }

    /* Double attributes */

    protected void setElementAttribute(String name, Number value, Number skipValue) {
        if (skipValue != null && Objects.equals(value, skipValue))
            value = null;
        setElementAttribute(name, value);
    }

    protected void setElementAttribute(String name, Number value) {
        Element topVisibleElement = getVisibleContainer();
        String styleAttribute = getStyleAttribute(name);
        if (styleAttribute != null) // Note: previous code was excluding this case when topVisibleElement == element (can't remember the reason), but this was preventing opacity working on buttons (which are embed in a <span> container)
            HtmlUtil.setStyleAttribute(topVisibleElement, name, value);
        else
            setElementAttribute(topVisibleElement, name, value);
    }

    private void setElementAttribute(Element e, String name, Number value) {
        if (value == null)
            e.removeAttribute(name);
        else
            e.setAttribute(name, value.doubleValue());
    }

    protected void setFontAttributes(Font font) {
        setFontAttributes(font, getVisibleContainer());
    }

    protected void setFontAttributes(Font font, Element element) {
        HtmlFonts.setHtmlFontStyleAttributes(font, element);
    }

    private static String toSvgBlendMode(BlendMode blendMode) {
        // JavaFX use the same names as SVG for ADD, MULTIPLY, SCREEN, OVERLAY, DARKEN, COLOR_DODGE, COLOR_BURN, HARD_LIGHT, SOFT_LIGHT, DIFFERENCE, EXCLUSION
        // SVG doesn't support (so far): SRC_OVER, SRC_ATOP, RED, GREEN, BLUE, but we return them as is just in case it is supported in the future
        return blendMode == null ? null : enumNameToCss(blendMode.name());
    }

    private static String enumNameToCss(String enumName) {
        return enumName.toLowerCase().replace('_', '-');
    }

    public static String toCssCursor(Cursor cursor) {
        if (cursor == null)
            return null;
        CursorType cursorType = cursor.getCurrentFrame().getCursorType();
        switch (cursorType) {
            // Starting with differences of names between JavaFX and CSS:
            case HAND: return "pointer";
            case OPEN_HAND: return "grab";
            case CLOSED_HAND: return "grabbing";
            case H_RESIZE: return "ew-resize";
            case V_RESIZE: return "ns-resize";
            case DISAPPEAR: return "no-drop";
            case IMAGE: // TODO: extract url from ImageCursorFrame
                // Then all other cursors have the same name: DEFAULT, CROSSHAIR, TEXT, WAIT, SW_RESIZE, SE_RESIZE, NW_RESIZE, NE_RESIZE, N_RESIZE, S_RESIZE, W_RESIZE, E_RESIZE, MOVE, NONE
            default: return enumNameToCss(cursorType.name());
        }
    }

    public static HtmlSvgNodePeer toNodePeer(Node node) {
        return (HtmlSvgNodePeer) node.getOrCreateAndBindNodePeer();
    }

    public static Element toContainerElement(Node node) {
        return node == null ? null : toNodePeer(node).getContainer();
    }
}
