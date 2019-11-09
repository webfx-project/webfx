package webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import elemental2.dom.MouseEvent;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.LayoutFlags;
import javafx.scene.text.TextFlow;
import webfx.kit.mapper.peers.javafxgraphics.gwt.shared.HtmlSvgNodePeer;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import webfx.kit.mapper.peers.javafxgraphics.emul_coupling.base.ScenePeerBase;
import webfx.kit.mapper.peers.javafxgraphics.HasNoChildrenPeers;
import webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.FxEvents;
import webfx.kit.util.properties.Properties;
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

    private final HTMLElement container = HtmlUtil.absolutePosition(HtmlUtil.createElement("fx-scene"));

    public HtmlScenePeer(Scene scene) {
        super(scene);
        Properties.runNowAndOnPropertiesChange(property -> updateContainerWidth(),  scene.widthProperty());
        Properties.runNowAndOnPropertiesChange(property -> updateContainerHeight(), scene.heightProperty());
        Properties.runNowAndOnPropertiesChange(property -> updateContainerFill(),   scene.fillProperty());
        installMouseListeners();
        installStylesheetsListener(scene);
    }

    private void installMouseListeners() {
        registerMouseListener("mousedown");
        registerMouseListener("mouseup");
        //registerMouseListener("click"); // Not necessary as the JavaFx Scene already generates them based on the mouse pressed and released events
        registerMouseListener("mouseenter");
        registerMouseListener("mouseleave");
        registerMouseListener("mousemove");
        container.oncontextmenu = e -> {
            //e.stopPropagation();
            e.preventDefault(); // To prevent the browser default context menu
            if (e instanceof MouseEvent // For now we manage only context menu from the mouse
                    // Also checking that we received the mouse up event on that scene before. This is to prevent the
                    // following case: when a context menu is already displayed (=> in another popup window/scene) and
                    // the user right click on a menu item, the menu action will be triggered on the mouseup within the
                    // popup window/scene (so far, so good) but then oncontextmenu is called on this scene by the browser
                    // whereas the intention of the user was just to trigger the menu action (which also closes the
                    // context menu) but not to display the context menu again. So we prevent this by checking the last
                    // mouse event was a mouse up on that scene which is the correct sequence (in the above case, the
                    // last mouse event will be the oncontextmenu event).
                    /*&& lastMouseEvent != null && "mouseup".equals(lastMouseEvent.type)*/) {
                MouseEvent me = (MouseEvent) e;
                listener.menuEvent(me.x, me.y, me.pageX, me.pageY, false);
                //lastMouseEvent = me;
            }
            return null;
        };
        // Disabling default browser drag & drop as JavaFx has its own
        //container.setAttribute("ondragstart", "return false;");
        //container.setAttribute("ondrop", "return false;");
    }

    private void registerMouseListener(String type) {
        container.addEventListener(type, e -> passHtmlMouseEventOnToFx((MouseEvent) e, type));
    }

    private boolean atLeastOneAnimationFrameOccurredSinceLastMousePressed = true;

    //private MouseEvent lastMouseEvent;
    private void passHtmlMouseEventOnToFx(MouseEvent e, String type) {
        javafx.scene.input.MouseEvent fxMouseEvent = FxEvents.toFxMouseEvent(e, type);
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
            // Stopping propagation if the event has been consumed by JavaFx
            if (fxMouseEvent.isConsumed())
                e.stopPropagation();
            // Note: important to not stop propagation for third-party js components (ex: perfect-scrollbar)
        }
        //lastMouseEvent = e;
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
            link.onload = e -> updateSceneGraphLayout();
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
        Element element = document.elementFromPoint(sceneX, sceneY);
        NodePeer peer = HtmlSvgNodePeer.getPeerFromElementOrParents(element);
        // Checking that the we pick it from the right scene (in case there are several windows/scenes within the DOM)
        if (peer != null && peer.getNode().getScene() != getScene())
            peer = null;
        return peer;
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

}
