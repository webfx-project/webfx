package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.HasNoChildrenPeers;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.base.ScenePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.HtmlSvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.FxEvents;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.kit.util.properties.Properties;
import dev.webfx.platform.client.services.uischeduler.UiScheduler;
import dev.webfx.platform.shared.util.collection.Collections;
import elemental2.dom.*;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.LayoutFlags;
import javafx.scene.text.TextFlow;

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
        HtmlUtil.setStyleAttribute(container, "width", "100%");
        HtmlUtil.setStyleAttribute(container, "height", "100vh"); // 100% is not good on mobile when the browser navigation bar is hidden, but 100vh works
        Properties.runNowAndOnPropertiesChange(property -> updateContainerFill(), scene.fillProperty());
        installMouseListeners();
        HtmlSvgNodePeer.installKeyboardListeners(DomGlobal.window, scene);
        installStylesheetsListener(scene);
        document.fonts.setOnloadingdone(p0 -> { onCssOrFontLoaded(); return null; });
        // The following code is just to avoid a downgrade in Lighthouse (iframe should have a title)
        NodeList<Element> iframes = document.getElementsByTagName("iframe"); // Looking for the GWT iframe
        if (iframes.length > 0) {
            HTMLIFrameElement iframe = (HTMLIFrameElement) iframes.getAt(0);
            iframe.title = "GWT iframe"; // and set it a title
            iframe.setAttribute("aria-hidden", "true"); // also good to do to avoid confusion with accessibility features
        }
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
                UiScheduler.scheduleInAnimationFrame(() -> scene.impl_processMouseEvent(fxMouseEvent), 1);
            else {
                scene.impl_processMouseEvent(fxMouseEvent);
                if (fxMouseEvent.getEventType() == javafx.scene.input.MouseEvent.MOUSE_PRESSED) {
                    atLeastOneAnimationFrameOccurredSinceLastMousePressed = false;
                    UiScheduler.scheduleInAnimationFrame(() -> atLeastOneAnimationFrameOccurredSinceLastMousePressed = true, 1);
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
            link.onload = e -> onCssOrFontLoaded();
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

    private void onCssOrFontLoaded() {
        clearLayoutCache(scene.getRoot());
    }

    private static void clearLayoutCache(Node node) {
        node.clearCache();
        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            parent.setLayoutFlag(LayoutFlags.NEEDS_LAYOUT);
            parent.getChildren().forEach(HtmlScenePeer::clearLayoutCache);
        }
        node.onPeerSizeChanged();
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
        if (peer != null) {
            Node node = peer.getNode();
            if (node == null || node.getScene() != getScene())
                peer = null;
        }
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
                HtmlUtil.setChildren(childrenContainer, toChildElements(parent.getChildren()));
            else {
                //Logger.log(childrenChange);
                while (childrenChange.next()) {
                    if (childrenChange.wasRemoved())
                        HtmlUtil.removeChildren(childrenContainer, toChildElements(childrenChange.getRemoved()));
                    if (childrenChange.wasAdded())
                        HtmlUtil.appendChildren(childrenContainer, toChildElements(childrenChange.getAddedSubList()));
                }
            }
            //long t1 = System.currentTimeMillis();
            //Logger.log("setChildren() in " + (t1 - t0) + "ms / parent treeVisible = " + parentPeer.isTreeVisible() + ", isAnimationFrame = " + UiScheduler.isAnimationFrameNow());
        }
    }

    private List<Element> toChildElements(List<Node> nodes) {
        return Collections.map(nodes, this::toChildElement);
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
            HTMLElement htmlElement = (HTMLElement) htmlNodePeer.getVisibleContainer();
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
