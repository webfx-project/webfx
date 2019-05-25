package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html;

import elemental2.dom.*;
import javafx.collections.ListChangeListener;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.LayoutFlags;
import javafx.scene.text.TextFlow;
import webfx.fxkit.javafxgraphics.mapper.highcoupling.spi.impl.ScenePeerBase;
import webfx.fxkit.javafxgraphics.mapper.spi.HasNoChildrenPeers;
import webfx.fxkit.javafxgraphics.mapper.spi.NodePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.shared.HtmlSvgNodePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.HtmlPaints;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.HtmlUtil;
import webfx.fxkit.util.properties.Properties;
import webfx.platform.client.services.uischeduler.AnimationFramePass;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.util.collection.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static elemental2.dom.DomGlobal.document;

/**
 * @author Bruno Salmon
 */
public final class HtmlScenePeer extends ScenePeerBase {

    private final HTMLElement container = HtmlUtil.createDivElement();

    public HtmlScenePeer(Scene scene) {
        super(scene);
        Properties.runNowAndOnPropertiesChange(property -> updateContainerWidth(), scene.widthProperty());
        Properties.runNowAndOnPropertiesChange(property -> updateContainerHeight(), scene.heightProperty());
        Properties.runNowAndOnPropertiesChange(property -> updateContainerFill(), scene.fillProperty());
        installMouseListeners();
        installStylesheetsListener(scene);
    }

    private void installStylesheetsListener(Scene scene) {
        scene.getStylesheets().addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                if (change.wasRemoved() || change.wasUpdated())
                    removeStyleSheet(change.getRemoved());
                if (change.wasAdded() || change.wasUpdated())
                    addStyleSheet(change.getAddedSubList());
            }
        });
    }

    private Map<String /* href  => */, Element /* link */> stylesheetLinks = new HashMap<>();

    private void addStyleSheet(List<? extends String> hrefs) {
        hrefs.forEach(href -> {
            Element link = document.createElement("link");
            link.setAttribute("rel", "stylesheet");
            link.setAttribute("type", "text/css");
            link.setAttribute("href", href);
            link.onload = e -> {
                updateSceneGraphLayout();
                return null;
            };
            document.body.appendChild(link);
            stylesheetLinks.put(href, link); // Keeping a reference to the link for eventual removal
        });
    }

    private void removeStyleSheet(List<? extends String> hrefs) {
        hrefs.forEach(href -> {
            Element link = stylesheetLinks.remove(href);
            if (link != null)
                link.parentNode.removeChild(link);
        });
    }

    private void updateSceneGraphLayout() {
        Parent root = scene.getRoot();
        clearLayoutCache(root);
        root.onPeerSizeChanged();
    }

    private static void clearLayoutCache(Node node) {
        node.clearCache();
        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            parent.setLayoutFlag(LayoutFlags.NEEDS_LAYOUT);
            parent.getChildren().forEach(HtmlScenePeer::clearLayoutCache);
        }
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

    private void updateContainerFill() {
        container.style.background = HtmlPaints.toHtmlCssPaint(scene.getFill());
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
        if (!(parent instanceof HasNoChildrenPeers)) {
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
            if (htmlNodePeer instanceof NormalWhiteSpacePeer)
                style.whiteSpace = "normal";
            else if (htmlNodePeer instanceof NoWrapWhiteSpacePeer || htmlElement.tagName.equalsIgnoreCase("SPAN"))
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
        javafx.scene.input.MouseEvent fxMouseEvent = toFxMouseEvent(e, type);
        if (fxMouseEvent != null) {
            // We now need to call Scene.impl_processMouseEvent() to pass the event to the JavaFx stack
            Scene scene = getScene();
            // Also fixing a problem: mouse released and mouse pressed are sent very closely on mobiles and might be
            // treated in the same animation frame, which prevents the button pressed state (ex: a background bound to
            // the button pressedProperty) to appear before the action (which might be time consuming) is fired, so the
            // user doesn't know if the button has been successfully pressed or not during the action execution.
            if (fxMouseEvent.getEventType() == javafx.scene.input.MouseEvent.MOUSE_RELEASED && !atLeastOneAnimationFrameOccurredSinceLastMousePressed)
                UiScheduler.scheduleInFutureAnimationFrame(1, () -> scene.impl_processMouseEvent(fxMouseEvent), AnimationFramePass.UI_UPDATE_PASS);
            else {
                scene.impl_processMouseEvent(fxMouseEvent);
                if (fxMouseEvent.getEventType() == javafx.scene.input.MouseEvent.MOUSE_PRESSED) {
                    atLeastOneAnimationFrameOccurredSinceLastMousePressed = false;
                    UiScheduler.scheduleInFutureAnimationFrame(1, () -> atLeastOneAnimationFrameOccurredSinceLastMousePressed = true, AnimationFramePass.UI_UPDATE_PASS);
                }
            }
        }
    }

    private static final boolean[] BUTTON_DOWN_STATES = {false, false, false, false};

    private javafx.scene.input.MouseEvent toFxMouseEvent(elemental2.dom.MouseEvent me, String type) {
        MouseButton button;
        switch (me.button) {
            case 0: button = MouseButton.PRIMARY; break;
            case 1: button = MouseButton.MIDDLE; break;
            case 2: button = MouseButton.SECONDARY; break;
            default: button = MouseButton.NONE;
        }
        EventType<javafx.scene.input.MouseEvent> eventType;
        switch (type) {
            case "mousedown": eventType = javafx.scene.input.MouseEvent.MOUSE_PRESSED; BUTTON_DOWN_STATES[button.ordinal()] = true; break;
            case "mouseup": eventType = javafx.scene.input.MouseEvent.MOUSE_RELEASED; BUTTON_DOWN_STATES[button.ordinal()] = false; break;
            case "mouseenter": eventType = javafx.scene.input.MouseEvent.MOUSE_ENTERED; break;
            case "mouseleave": eventType = javafx.scene.input.MouseEvent.MOUSE_EXITED; break;
            case "mousemove": eventType = BUTTON_DOWN_STATES[button.ordinal()] ? javafx.scene.input.MouseEvent.MOUSE_DRAGGED : javafx.scene.input.MouseEvent.MOUSE_MOVED; break;
            default: return null;
        }
        return new javafx.scene.input.MouseEvent(null, null, eventType, me.pageX, me.pageY, me.screenX, me.screenY, button,
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
