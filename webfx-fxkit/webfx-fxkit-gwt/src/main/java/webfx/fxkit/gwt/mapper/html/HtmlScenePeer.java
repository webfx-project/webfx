package webfx.fxkit.gwt.mapper.html;

import elemental2.dom.*;
import emul.javafx.collections.ListChangeListener;
import emul.javafx.event.EventType;
import emul.javafx.scene.Node;
import emul.javafx.scene.Parent;
import emul.javafx.scene.Scene;
import emul.javafx.scene.input.MouseButton;
import emul.javafx.scene.text.TextFlow;
import webfx.fxkit.gwt.mapper.html.peer.HtmlHtmlTextPeer;
import webfx.fxkit.gwt.mapper.html.peer.HtmlNodePeer;
import webfx.fxkit.gwt.mapper.shared.HtmlSvgNodePeer;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkits.core.util.properties.Properties;
import webfx.fxkits.core.mapper.spi.NodePeer;
import webfx.fxkits.core.mapper.spi.impl.peer.ScenePeerBase;
import webfx.fxkits.extra.control.HtmlText;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.client.services.uischeduler.AnimationFramePass;
import webfx.platform.shared.util.collection.Collections;

import static elemental2.dom.DomGlobal.document;

/**
 * @author Bruno Salmon
 */
public final class HtmlScenePeer extends ScenePeerBase {

    private final HTMLElement container = HtmlUtil.createDivElement();

    public HtmlScenePeer(Scene scene) {
        super(scene);
        HtmlUtil.setStyleAttribute(container, "width", "100%");
        Properties.runNowAndOnPropertiesChange(property -> updateContainerWidth(), scene.widthProperty());
        Properties.runNowAndOnPropertiesChange(property -> updateContainerHeight(), scene.heightProperty());
        installMouseListeners();
    }

    public void changedWindowSize(float width, float height) {
        if (listener != null)
            listener.changedSize(width, height);
    }

    private void updateContainerWidth() {
        double width = scene.getWidth();
        HtmlUtil.setStyleAttribute(container, "width",
                (width > 0 ?
                        width :
                        scene.getRoot() != null ?
                                scene.getRoot().prefWidth(-1) :
                                0)
                        + "px");
    }

    private void updateContainerHeight() {
        double height = scene.getHeight();
        HtmlUtil.setStyleAttribute(container, "height",
                (height > 0 ?
                        height :
                        scene.getRoot() != null ?
                                scene.getRoot().prefHeight(-1) :
                                0)
                        + "px");
    }

    public elemental2.dom.Node getSceneNode() {
        return container;
    }

    @Override
    public NodePeer pickPeer(double sceneX, double sceneY) {
        return HtmlSvgNodePeer.getPeerFromElementOrParents(document.elementFromPoint(sceneX, sceneY));
    }

    @Override
    public void onRootBound() {
        HtmlUtil.setChildren(container, HtmlSvgNodePeer.toContainerElement(scene.getRoot(), scene));
    }

    @Override
    public void updateParentAndChildrenPeers(Parent parent, ListChangeListener.Change<Node> childrenChange) {
        if (!(parent instanceof HtmlText)) {
            HtmlSvgNodePeer parentPeer = HtmlSvgNodePeer.toNodePeer(parent, scene);
            //long t0 = System.currentTimeMillis();
            Element childrenContainer = parentPeer.getChildrenContainer();
            if (childrenChange == null)
                HtmlUtil.setChildren(childrenContainer, Collections.map(parent.getChildren(), this::toChildElement));
            else {
                //Logger.log(childrenChange);
                while (childrenChange.next()) {
                    if (childrenChange.wasRemoved())
                        HtmlUtil.removeChildren(childrenContainer, Collections.map(childrenChange.getRemoved(), this::toChildElement));
                    if (childrenChange.wasAdded())
                        HtmlUtil.appendChildren(childrenContainer, Collections.map(childrenChange.getAddedSubList(), this::toChildElement));
                }
            }
            //long t1 = System.currentTimeMillis();
            //Logger.log("setChildren() in " + (t1 - t0) + "ms / parent treeVisible = " + parentPeer.isTreeVisible() + ", isAnimationFrame = " + UiScheduler().isAnimationFrameNow());
        }
    }

    private Element toChildElement(Node node) {
        Element element = HtmlSvgNodePeer.toContainerElement(node, scene);
        // TextFlow special case
        if (node.getParent() instanceof TextFlow && element instanceof HTMLElement) {
            HTMLElement htmlElement = (HTMLElement) element;
            htmlElement.style.whiteSpace = "normal"; // white space are allowed
            htmlElement.style.lineHeight = null; // and line height is default (not 100%)
        }
        return element;
    }

    @Override
    public void onNodePeerCreated(NodePeer<Node> nodePeer) {
        if (nodePeer instanceof HtmlNodePeer) {
            HtmlNodePeer htmlNodePeer = (HtmlNodePeer) nodePeer;
            HTMLElement htmlElement = (HTMLElement) htmlNodePeer.getElement();
            HtmlUtil.absolutePosition(htmlElement);
            CSSStyleDeclaration style = htmlElement.style;
            // Positioned to left top corner by default
            style.left = "0px";
            style.top = "0px";
            if (htmlNodePeer instanceof HtmlHtmlTextPeer)
                style.whiteSpace = "normal";
            else if (htmlElement instanceof HTMLButtonElement || htmlElement instanceof HTMLLabelElement || htmlElement.tagName.equalsIgnoreCase("SPAN"))
                style.whiteSpace = "nowrap";
        }
    }

    private void installMouseListeners() {
        registerMouseListener("mousedown");
        registerMouseListener("mouseup");
        //registerMouseListener("click"); // Not necessary as the JavaFx Scene already generates them based on the mouse pressed and released events
        registerMouseListener("mouseenter");
        registerMouseListener("mouseleave");
        registerMouseListener("mousemove");
    }

    private void registerMouseListener(String type) {
        document.addEventListener(type, e -> passHtmlMouseEventOnToFx((MouseEvent) e, type));
    }

    private boolean atLeastOneAnimationFrameOccurredSinceLastMousePressed = true;

    private void passHtmlMouseEventOnToFx(elemental2.dom.MouseEvent e, String type) {
        emul.javafx.scene.input.MouseEvent fxMouseEvent = toFxMouseEvent(e, type);
        if (fxMouseEvent != null) {
            // We now need to call Scene.impl_processMouseEvent() to pass the event to the JavaFx stack
            Scene scene = getScene();
            // Also fixing a problem: mouse released and mouse pressed are sent very closely on mobiles and might be
            // treated in the same animation frame, which prevents the button pressed state (ex: a background bound to
            // the button pressedProperty) to appear before the action (which might be time consuming) is fired, so the
            // user doesn't know if the button has been successfully pressed or not during the action execution.
            if (fxMouseEvent.getEventType() == emul.javafx.scene.input.MouseEvent.MOUSE_RELEASED && !atLeastOneAnimationFrameOccurredSinceLastMousePressed)
                UiScheduler.scheduleInFutureAnimationFrame(1, () -> scene.impl_processMouseEvent(fxMouseEvent), AnimationFramePass.UI_UPDATE_PASS);
            else {
                scene.impl_processMouseEvent(fxMouseEvent);
                if (fxMouseEvent.getEventType() == emul.javafx.scene.input.MouseEvent.MOUSE_PRESSED) {
                    atLeastOneAnimationFrameOccurredSinceLastMousePressed = false;
                    UiScheduler.scheduleInFutureAnimationFrame(1, () -> atLeastOneAnimationFrameOccurredSinceLastMousePressed = true, AnimationFramePass.UI_UPDATE_PASS);
                }
            }
        }
    }

    private static final boolean[] BUTTON_DOWN_STATES = {false, false, false, false};

    private emul.javafx.scene.input.MouseEvent toFxMouseEvent(elemental2.dom.MouseEvent me, String type) {
        MouseButton button;
        switch (me.button) {
            case 0: button = MouseButton.PRIMARY; break;
            case 1: button = MouseButton.MIDDLE; break;
            case 2: button = MouseButton.SECONDARY; break;
            default: button = MouseButton.NONE;
        }
        EventType<emul.javafx.scene.input.MouseEvent> eventType;
        switch (type) {
            case "mousedown": eventType = emul.javafx.scene.input.MouseEvent.MOUSE_PRESSED; BUTTON_DOWN_STATES[button.ordinal()] = true; break;
            case "mouseup": eventType = emul.javafx.scene.input.MouseEvent.MOUSE_RELEASED; BUTTON_DOWN_STATES[button.ordinal()] = false; break;
            case "mouseenter": eventType = emul.javafx.scene.input.MouseEvent.MOUSE_ENTERED; break;
            case "mouseleave": eventType = emul.javafx.scene.input.MouseEvent.MOUSE_EXITED; break;
            case "mousemove": eventType = BUTTON_DOWN_STATES[button.ordinal()] ? emul.javafx.scene.input.MouseEvent.MOUSE_DRAGGED : emul.javafx.scene.input.MouseEvent.MOUSE_MOVED; break;
            default: return null;
        }
        return new emul.javafx.scene.input.MouseEvent(null, null, eventType, me.pageX, me.pageY, me.screenX, me.screenY, button,
                1, me.shiftKey, me.ctrlKey, me.altKey, me.metaKey,
                BUTTON_DOWN_STATES[MouseButton.PRIMARY.ordinal()],
                BUTTON_DOWN_STATES[MouseButton.MIDDLE.ordinal()],
                BUTTON_DOWN_STATES[MouseButton.SECONDARY.ordinal()],
                false,
                false,
                false,
                null);
    }

}
