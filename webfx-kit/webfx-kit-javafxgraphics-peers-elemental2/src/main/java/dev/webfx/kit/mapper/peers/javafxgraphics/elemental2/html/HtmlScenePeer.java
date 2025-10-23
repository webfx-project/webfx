package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import com.sun.javafx.scene.NodeHelper;
import dev.webfx.kit.mapper.peers.javafxgraphics.HasNoChildrenPeers;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.base.ScenePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared.HtmlSvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared.SvgRoot;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared.SvgRootBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.FxEvents;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.SvgUtil;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.util.Strings;
import dev.webfx.platform.util.collection.Collections;
import elemental2.dom.*;
import elemental2.svg.SVGSVGElement;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static elemental2.dom.DomGlobal.document;

/**
 * @author Bruno Salmon
 */
public final class HtmlScenePeer extends ScenePeerBase {

    private static final boolean ENABLE_DEBUG_FOCUS_LOGS = false;
    private static int SCENE_SEQ = 0;
    private static Scene PRIMARY_SCENE;

    private final int sceneNumber = SCENE_SEQ++;
    private final HTMLElement sceneHtmlElement = HtmlUtil.createElement("fx-scene");

    public HtmlScenePeer(Scene scene) {
        super(scene);
        if (PRIMARY_SCENE == null && scene != Node.PHANTOM_SCENE)
            PRIMARY_SCENE = scene;
        // CSS rules: width: 100%; height:100% for popup scenes and 100dvh for the main stage scene (dvh is better than
        // vh on mobiles because it keeps working when the navigation bar hides or reappears).
        FXProperties.runNowAndOnPropertyChange(this::updateContainerFill, scene.fillProperty());
        installPointerListeners();
        HtmlSvgNodePeer.installTouchListeners(sceneHtmlElement, scene);
        installFocusListeners();
        installKeyboardListeners();
        installStylesheetsListener(scene);
        installFontsListener();
        installIconsListener();
        installCursorListener();
        // The following code is just to avoid a downgrade in Lighthouse (iframe should have a title)
        NodeList<Element> iframes = document.getElementsByTagName("iframe"); // Looking for the GWT iframe
        if (iframes.length > 0) {
            HTMLIFrameElement iframe = (HTMLIFrameElement) iframes.getAt(0);
            iframe.title = "GWT iframe"; // and set it a title
            iframe.setAttribute("aria-hidden", "true"); // also good to do to avoid confusion with accessibility features
        }
    }

    private void installPointerListeners() {
        // Important to use pointer events (not just mouse events) to support touch screens. This is how JavaFX
        // setOnMouseXXX() handlers - including setOnMouseDragged() - will also work with touch events (like in JavaFX).
        // Note that setOnTouchXXX() handlers will also be mapped to touch listeners on DOM nodes (via HtmlSvgNodePeer),
        // and such listeners will actually be prioritized, but if they don't consume the event, it will finally be
        // passed to these scene listeners.
        registerPointerListener("pointerdown");
        registerPointerListener("pointerup");
        registerPointerListener("pointerenter");
        registerPointerListener("pointerleave");
        registerPointerListener("pointermove");
        // Note: not necessary to register "clicked", as the JavaFX Scene has its own way to generate mouse click events
        // (based on the pressed and released events).
        sceneHtmlElement.oncontextmenu = e -> {
            if (e instanceof MouseEvent me // For now, we manage only the context menu from the mouse
                // Also checking that we received the mouse up event on that scene before. This is to prevent the
                // following case: when a context menu is already displayed (=> in another popup window/scene) and
                // the user right-clicks on a menu item, the menu action will be triggered on the mouseup within the
                // popup window/scene (so far, so good) but then oncontextmenu is called on this scene by the browser
                // whereas the intention of the user was just to trigger the menu action (which also closes the
                // context menu) but not to display the context menu again. So we prevent this by checking the last
                // mouse event was a mouse up on that scene, which is the correct sequence (in the above case, the
                // last mouse event will be the oncontextmenu event).
                /*&& lastMouseEvent != null && "mouseup".equals(lastMouseEvent.type)*/) {
                int lastSceneSEQ = SCENE_SEQ;
                // Finally, we generate the menu event for JavaFX
                listener.menuEvent(me.x, me.y, me.screenX, me.screenY, false);
                // If the application doesn't show any JavaFX context menu, we display the browser context menu (nothing
                // to do in that case as this is the default browser behavior). But if the application shows a context
                // menu, we prevent the browser context menu from appearing, because not only the user doesn't want it,
                // but in addition, it hides the JavaFX context menu! Detecting the JavaFX context menu is a bit tricky.
                // There are 2 cases:
                // 1) The context menu is shown for the first time, and this involves the creation of a new popup window
                // with a new scene inside, something we can easily detect.
                // 2) The context menu is shown again (no scene is created), but the application code consumed the event
                // after displaying the context menu (a WebFX requirement to make this work). Detecting this is a bit
                // tricky as the call to menuEvent() doesn't return the final event. So we use the specific WebFX field
                // Event.lastFinalFiredEvent for this. A null value indicates that the app consumed the event.
                // Note: for some reason Event.lastFinalFiredEvent is not null the first time the context menu is shown,
                // which is why we need to detect case 1) as well.
                if (SCENE_SEQ > lastSceneSEQ // case 1)
                    || Event.lastFinalFiredEvent == null) { // Case 2)
                    e.preventDefault(); // This will prevent the browser default context menu
                }
            }
            return null;
        };
        // Disabling the default browser drag and drop as JavaFX has its own. Without doing this, the setOnMouseDragged()
        // handler would be called only once (when the drag starts) and not continuously during the drag operation.
        // Also, without doing this, the browser would display a ghost image of the dragged element (which is not the
        // case with JavaFX). Ex of a use case: Modality user profile picture drag (in ChangePictureUI).
        sceneHtmlElement.setAttribute("ondragstart", "return false;");
        sceneHtmlElement.setAttribute("ondrop", "return false;"); // TODO check if this is necessary
    }

    private void registerPointerListener(String type) {
        sceneHtmlElement.addEventListener(type, e -> passHtmlPointerEventOnToFx((MouseEvent) e, type));
    }

    private boolean atLeastOneAnimationFrameOccurredSinceLastMousePressed = true;

    private void passHtmlPointerEventOnToFx(MouseEvent e, String type) { // PointerEvent extends MouseEvent (will be available in elemental2 1.2.3)
        javafx.scene.input.MouseEvent fxMouseEvent = FxEvents.toFxMouseEvent(e, type);
        if (fxMouseEvent != null) {
            EventType<? extends javafx.scene.input.MouseEvent> fxType = fxMouseEvent.getEventType();
            boolean isMousePressed = fxType == javafx.scene.input.MouseEvent.MOUSE_PRESSED;
            boolean isMouseReleased = fxType == javafx.scene.input.MouseEvent.MOUSE_RELEASED;
            UserInteraction.setUserInteracting(isMousePressed || isMouseReleased);
            // We now need to call Scene.impl_processMouseEvent() to pass the event to the JavaFX stack
            // Also fixing a problem: mouse released and mouse pressed are sent very closely on mobiles and might be
            // treated in the same animation frame, which prevents the button `pressed` state (ex: a background bound to
            // the button pressedProperty) to appear before the action (which might be time-consuming) is fired, so the
            // user doesn't know if the button has been successfully pressed or not during the action execution.
            if (isMouseReleased && !atLeastOneAnimationFrameOccurredSinceLastMousePressed)
                UiScheduler.scheduleInAnimationFrame(() -> scene.impl_processMouseEvent(fxMouseEvent), 1);
            else {
                scene.impl_processMouseEvent(fxMouseEvent);
                if (isMousePressed) {
                    atLeastOneAnimationFrameOccurredSinceLastMousePressed = false;
                    UiScheduler.scheduleInAnimationFrame(() -> atLeastOneAnimationFrameOccurredSinceLastMousePressed = true, 1);
                }
            }
            UserInteraction.setUserInteracting(false);
            // Stopping propagation if JavaFX has consumed the event
            // Note: important to not stop propagation for third-party js components (ex: perfect-scrollbar)
            if (fxMouseEvent.isConsumed())
                e.stopPropagation();
        }
    }

    private void installStylesheetsListener(Scene scene) {
        mapObservableList(scene.getStylesheets(), s -> addStyleSheets(s), s -> removeStyleSheets(s));
    }

    private final Map<String /* href  => */, Element /* link */> stylesheetLinks = new HashMap<>();

    private void addStyleSheets(List<? extends String> hrefs) {
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

    private void removeStyleSheets(List<? extends String> hrefs) {
        hrefs.forEach(href -> {
            Element link = stylesheetLinks.remove(href);
            if (link != null)
                link.parentNode.removeChild(link);
        });
    }

    private static boolean BROWSER_SUPPORTS_FONT_LOADING_NOTIFICATION;

    private void installFontsListener() {
        // Transferring the fonts programmatically requested by the application code to the browser
        mapObservableList(Font.getRequestedFonts(), // this list is managed by WebFX when app code calls Font.loadFont()
            fonts -> addToDocumentFonts(fonts), // new requested fonts -> + document.fonts -> load
            fonts -> removeFromDocumentFonts(fonts) // removed fonts -> - document.fonts
        );

        // Once a font is loaded (either programmatically or via CSS), the browser will apply it to all text elements
        // styled with that font, and this will probably change their size, but JavaFX won't detect that change, and
        // this will create layout issues (ex: a text supposed to be centered won't appear centered anymore with the new
        // font). To fix those issues, we call onCssOrFontLoaded() which forces a layout of the whole scene graph.

        // Best way to detect when a font is loaded = via document.fonts.onloadingdone but not all browsers support it
        document.fonts.setOnloadingdone(e -> { // called back by Chrome & FF, but not Safari
            onCssOrFontLoaded(); // forces the whole scene graph layout to correct wrong text positions
            BROWSER_SUPPORTS_FONT_LOADING_NOTIFICATION = true;
            return null;
        });
        // Fallback for browsers not supporting fonts loading notification: calling onCssOrFontLoaded() every second for
        // 10 sec, starting when fonts are ready
        document.fonts.getReady().then(fontFaceSet -> {
            int[] fontsLoadingDoneFallbackCount = { 0 };
            UiScheduler.scheduleNowAndPeriodic(1000, scheduled -> {
                // Stopping this timer after 10 sec (assuming it's enough to load all fonts), or if the browser supports font loading notification
                if (++fontsLoadingDoneFallbackCount[0] > 10 || BROWSER_SUPPORTS_FONT_LOADING_NOTIFICATION)
                    scheduled.cancel();
                else {
                    onCssOrFontLoaded();
                }
            });
            return null;
        });
    }

    private final Map<String /* url  => */, FontFace> fontFaces = new HashMap<>();

    private void addToDocumentFonts(List<? extends Font> fonts) {
        fonts.forEach(font -> {
            FontFace fontFace = new FontFace(font.getFamily(), "url(" + font.getUrl() + ")");
            fontFaces.put(font.getUrl(), fontFace);
            document.fonts.add(fontFace);
            fontFace.load().then(p0 -> {
                onCssOrFontLoaded();
                Font.getLoadingFonts().remove(font);
                return null;
            });
        });
    }

    private void removeFromDocumentFonts(List<? extends Font> fonts) {
        fonts.forEach(font -> {
            FontFace fontFace = fontFaces.remove(font.getUrl());
            if (fontFace != null)
                document.fonts.delete(fontFace);
        });
    }

    private void onCssOrFontLoaded() {
        // TODO: reset all caches in HtmlLayoutMeasurable
        NodeHelper.webfx_forceLayoutOnThisNodeAndChildren(scene.getRoot());
    }

    private void installIconsListener() {
        FXProperties.onPropertySet(scene.windowProperty(), window -> {
            if (window instanceof Stage) {
                mapObservableList(((Stage) window).getIcons(), icons -> addIcons(icons), icons -> removeIcons(icons));
            }
        });
    }

    private final Map<String /* icon  => */, Element /* link */> iconLinks = new HashMap<>();

    private void addIcons(List<? extends Image> icons) {
        icons.forEach(icon -> {
            String url = icon.getUrl();
            Element link = document.createElement("link");
            link.setAttribute("rel", "icon");
            link.setAttribute("type", "image");
            link.setAttribute("href", url);
            document.head.appendChild(link);
            iconLinks.put(url, link); // Keeping a reference to the link for eventual removal
        });
    }

    private void removeIcons(List<? extends Image> icons) {
        icons.forEach(icon -> {
            Element link = iconLinks.remove(icon.getUrl());
            if (link != null)
                link.parentNode.removeChild(link);
        });
    }

    private void installCursorListener() {
        FXProperties.runNowAndOnPropertyChange(cursor ->
            HtmlUtil.setStyleAttribute(getSceneHtmlElement(), "cursor", HtmlSvgNodePeer.toCssCursor(cursor)), scene.cursorProperty()
        );
    }


    private void updateContainerFill() {
        sceneHtmlElement.style.background = HtmlPaints.toHtmlCssPaint(scene.getFill());
    }

    public elemental2.dom.Node getSceneHtmlElement() {
        return sceneHtmlElement;
    }

    @Override
    public NodePeer<?> pickPeer(double sceneX, double sceneY) {
        Element element = document.elementFromPoint(sceneX, sceneY);
        NodePeer<?> peer = HtmlSvgNodePeer.getPeerFromElementOrParents(element, true);
        // Checking that we pick it from the right scene (in case there are several windows/scenes within the DOM)
        if (peer != null) {
            Node node = peer.getNode();
            if (node == null || node.getScene() != scene)
                peer = null;
        }
        return peer;
    }

    @Override
    public void onRootBound() {
        HtmlUtil.setChildren(sceneHtmlElement, HtmlSvgNodePeer.toContainerElement(scene.getRoot()));
    }

    @Override
    public void updateParentAndChildrenPeers(Parent parent, ListChangeListener.Change<? extends Node> childrenChange) {
        HtmlSvgNodePeer<?, ?, ?, ?> parentPeer = HtmlSvgNodePeer.toNodePeer(parent);
        // HasNoChildrenPeers have no JavaFX fxChildren (mapped to HTML), but they do have HTML fxChildren (generated by the peer)
        if (parentPeer instanceof HasNoChildrenPeers) // Ex: HtmlHtmlTextPeer, HtmlHtmlTextEditorPeer, HtmlVisualGridPeer
            return; // => we don't want to clear the HTML fxChildren generated by the peer
        Element childrenContainer = parentPeer.getChildrenContainer();
        ObservableList<Node> fxChildren = parent.getChildren();
        if (childrenChange == null) { // Indicates we need to map children (not just a partial change)
            List<Element> childElements = toChildElements(fxChildren, null);
            HtmlUtil.setChildren(childrenContainer, childElements);
        } else { // Partial change of children. The above code (remap all children) would also work, but the code below
            // is more optimized because it applies only the partial changes to the DOM.
            while (childrenChange.next()) {
                List<? extends Node> removed = childrenChange.getRemoved();
                List<? extends Node> addedSubList = childrenChange.getAddedSubList();
                // When fxChildren have been removed from the scene graph, we remove their associated elements from the DOM
                if (!removed.isEmpty()) {
                    HtmlUtil.removeChildren(childrenContainer, toChildElements(removed, addedSubList));
                }
                // When children have been added to the scene graph, we add their associated elements to the DOM
                if (!addedSubList.isEmpty()) {
                    // Getting the associated elements that we will need to add into the DOM
                    List<Element> childElementsToAdd = toChildElements(addedSubList, null);
                    // Getting the index of the node which is now just after these added nodes (changes have already been applied in fxChildren)
                    int fxNodeAfterAddedIndex = childrenChange.getTo();
                    // Case when the nodes have been appended at the end => we need to append the elements to the DOM too
                    if (fxNodeAfterAddedIndex >= fxChildren.size()) {
                        HtmlUtil.appendChildren(childrenContainer, childElementsToAdd);
                    } else { // Case when they are inserted at a specific index, before the end
                        // Getting the JavaFX node just after the added nodes
                        Node fxNodeAfterAdded = fxChildren.get(fxNodeAfterAddedIndex);
                        // Getting its associated DOM element
                        elemental2.dom.Node insertBeforeDomNode = toChildElement(fxNodeAfterAdded);
                        // All added elements need to be inserted before that node
                        childElementsToAdd.forEach(elementToAdd -> childrenContainer.insertBefore(elementToAdd, insertBeforeDomNode));
                    }
                }
            }
        }
    }

    private List<Element> toChildElements(List<? extends Node> nodes, List<? extends Node> nodesToIgnore) {
        return Collections.filterMap(nodes, node -> nodesToIgnore == null || !nodesToIgnore.contains(node), this::toChildElement);
    }

    private Element toChildElement(Node node) {
        Element element = HtmlSvgNodePeer.toContainerElement(node);
        // TextFlow special case
        if (node.getParent() instanceof TextFlow && element instanceof HTMLElement htmlElement) {
            htmlElement.style.whiteSpace = "normal"; // white spaces are allowed
            htmlElement.style.lineHeight = null; // and line height is default (not 100%)
        }
        return element;
    }

    @Override
    public void onNodePeerCreated(NodePeer<Node> nodePeer) {
        if (nodePeer instanceof HtmlNodePeer<?, ?, ?> htmlNodePeer) {
            HTMLElement htmlElement = (HTMLElement) htmlNodePeer.getVisibleContainer();
            HtmlUtil.absolutePosition(htmlElement);
            CSSStyleDeclaration style = htmlElement.style;
            // Positioned to the left-top corner by default
            style.left = "0px";
            style.top = "0px";
            if (htmlNodePeer instanceof NormalWhiteSpacePeer)
                style.whiteSpace = "normal";
            else if (htmlNodePeer instanceof NoWrapWhiteSpacePeer)
                style.whiteSpace = "nowrap";
        }
    }


    private void installFocusListeners() {
        // The purpose here is to map any HTML focus change back to JavaFX.
        // So when the HTML element gains focus, we set its associated JavaFX node as the focus owner of the scene.
        sceneHtmlElement.addEventListener("focusin", e -> {
            if (e.target instanceof Element target) {
                NodePeer<?> peer = HtmlSvgNodePeer.getPeerFromElementOrParents(target, true);
                if (peer instanceof HtmlSvgNodePeer<?,?,?,?> htmlSvgNodePeer) {
                    if (ENABLE_DEBUG_FOCUS_LOGS)
                        Console.log("[Scene-" + sceneNumber + "] " + (htmlSvgNodePeer.isJavaFxFocusOwner() ? "🟢🟢🟢🟢🟢" : "🟠🟠🟠🟠🟠") + " focusin node = " + peer.getNode() + (peer.getNode() == htmlSvgNodePeer.getJavaFxFocusableNode() ? "" : " [" + htmlSvgNodePeer.getJavaFxFocusableNode() + "]"));
                    htmlSvgNodePeer.setJavaFxFocusOwner();
                }
            }
        }, true);

        // When it loses focus, it's often because another HTML element gained focus (so onfocus will be called on
        // that new element and this will update the focus owner in JavaFX). However, if this doesn't happen for any
        // reason, it's better to keep the JavaFX focus state synced with HTML by setting it to null.
        sceneHtmlElement.addEventListener("focusout", e -> {
            // We defer the code to check if the focus was moved to another target (most probably case).
            UiScheduler.scheduleDelay(100, () -> { // Added 100 ms breathing delay (safer in case of focus loop)
                // But if that focus change didn't happen (this node is still the focus owner for JavaFX), then we
                // reset the JavaFX focus state.
                if (e.target instanceof Element target) {
                    NodePeer<?> peer = HtmlSvgNodePeer.getPeerFromElementOrParents(target, false);
                    if (peer instanceof HtmlSvgNodePeer<?,?,?,?> htmlSvgNodePeer) {
                        NodePeer<?> focusinNodePeer = HtmlSvgNodePeer.getPeerFromElementOrParents(document.activeElement, false);
                        if (focusinNodePeer == null) {
                            if (ENABLE_DEBUG_FOCUS_LOGS)
                                Console.log("[Scene-" + sceneNumber + "] " + "🤷🤷🤷🤷🤷 focusout to a non-webfx node (maybe in an iFrame), focusout node = " + peer.getNode() + (peer.getNode() == htmlSvgNodePeer.getJavaFxFocusableNode() ? "" : " [" + htmlSvgNodePeer.getJavaFxFocusableNode() + "]"));
                        } else if (focusinNodePeer.getNode().getScene() != scene) {
                            if (ENABLE_DEBUG_FOCUS_LOGS)
                                Console.log("[Scene-" + sceneNumber + "] " + "🤷🤷🤷🤷🤷 focusout to another scene, focusin node = " + focusinNodePeer.getNode() + ", focusout node = " + peer.getNode() + (peer.getNode() == htmlSvgNodePeer.getJavaFxFocusableNode() ? "" : " [" + htmlSvgNodePeer.getJavaFxFocusableNode() + "]"));
                        } else if (focusinNodePeer == peer) { // Note: this happens with the HtmlTextEditor and its peer HtmlHtmlTextEditorPeer
                            if (ENABLE_DEBUG_FOCUS_LOGS)
                                Console.log("[Scene-" + sceneNumber + "] " + "🤷🤷🤷🤷🤷 Same focus in and out node = " + focusinNodePeer.getNode() + " [" + htmlSvgNodePeer.getJavaFxFocusableNode() + "]");
                        } else {
                            // If the focus is gained inside a seamless div (which may contain a payment gateway asking
                            // for CC details, for example), we don't try to reestablish the JavaFX focus node.
                            boolean seamlessDiv = focusinNodePeer.getNode().getProperties().containsKey("webfx-htmlTag");
                            if (seamlessDiv) {
                                if (ENABLE_DEBUG_FOCUS_LOGS)
                                    Console.log("[Scene-" + sceneNumber + "] " + "🤷🤷🤷🤷🤷 focusout to a seamless node, focusin node = " + focusinNodePeer.getNode() + ", focusout node = " + peer.getNode() + (peer.getNode() == htmlSvgNodePeer.getJavaFxFocusableNode() ? "" : " [" + htmlSvgNodePeer.getJavaFxFocusableNode() + "]"));
                            } else {
                                boolean javaFxFocusOwner = htmlSvgNodePeer.isJavaFxFocusOwner();
                                if (ENABLE_DEBUG_FOCUS_LOGS)
                                    Console.log("[Scene-" + sceneNumber + "] " + (javaFxFocusOwner ? "🔴🔴🔴🔴🔴" : "🔵🔵🔵🔵🔵") + " focusin node = " + focusinNodePeer.getNode() + ", focusout node = " + peer.getNode() + (peer.getNode() == htmlSvgNodePeer.getJavaFxFocusableNode() ? "" : " [" + htmlSvgNodePeer.getJavaFxFocusableNode() + "]"));
                                if (javaFxFocusOwner)
                                    htmlSvgNodePeer.requestFocus();
                            }
                        }
                    }
                }
            });
        }, true);
    }

    private void installKeyboardListeners() {
        registerKeyboardListener("keydown");
        registerKeyboardListener("keyup");
    }

    private void registerKeyboardListener(String type) {
        // For the primary scene, it's better to install the listener on the document; otherwise, if the application
        // doesn't request the initial focus on any element of the scene, we are not getting the key events at all
        // (observed with the game FoodDice).
        elemental2.dom.Node sceneKeyboardNode = scene == PRIMARY_SCENE ? document : sceneHtmlElement;
        sceneKeyboardNode.addEventListener(type, e -> {
            Node focusOwner = scene.getFocusOwner();
            // Resetting the focus owner to scene initial when it's not valid, so in the following cases:
            if (focusOwner == null // 1) No focus set yet
                || focusOwner.getScene() != scene // 2) focus doesn't match the scene (probably removed from the scene)
                || !focusOwner.impl_isTreeVisible()) { // 3) the focus is not visible in the scene graph (works also as a workaround when the scene is not reset to null by WebFX)
                scene.focusInitial();
                focusOwner = scene.getFocusOwner();
            }
            javafx.event.EventTarget fxTarget = focusOwner != null ? focusOwner : scene;
            UserInteraction.setUserInteracting(true);
            boolean fxConsumed = passHtmlKeyEventOnToFx((KeyboardEvent) e, type, fxTarget);
            UserInteraction.setUserInteracting(false);
            if (fxConsumed) {
                e.stopPropagation();
                e.preventDefault();
            }
        });
    }

    private static boolean passHtmlKeyEventOnToFx(KeyboardEvent e, String type, javafx.event.EventTarget fxTarget) {
        return HtmlSvgNodePeer.passOnToFx(fxTarget, toFxKeyEvent(e, type));
    }

    private static KeyEvent toFxKeyEvent(KeyboardEvent e, String type) {
        // Trying first to get the JavaFX code from e.code = logical key (takes into account selected system keyboard)
        KeyCode keyCode = toFxKeyCode(e.code);
        // Otherwise from e.key = physical code
        if (keyCode == KeyCode.UNDEFINED)
            keyCode = toFxKeyCode(e.key);
        EventType<KeyEvent> eventType = switch (type) {
            case "keydown" -> KeyEvent.KEY_PRESSED;
            case "keyup" -> KeyEvent.KEY_RELEASED;
            default -> KeyEvent.KEY_TYPED;
        };
        return new KeyEvent(eventType, e.key, e.key, keyCode, e.shiftKey, e.ctrlKey, e.altKey, e.metaKey);
    }

    private static KeyCode toFxKeyCode(String htmlKey) {
        if (htmlKey == null)
            return KeyCode.UNDEFINED;
        // See https://developer.mozilla.org/fr/docs/Web/API/KeyboardEvent/code
        switch (htmlKey) {
            case "Escape": return KeyCode.ESCAPE; // 0x0001
            case "Minus": return KeyCode.MINUS; // 0x000C
            case "Equal": return KeyCode.EQUALS; // 0x000D
            case "Backspace": return KeyCode.BACK_SPACE; // 0x000E
            case "Tab": return KeyCode.TAB; // 0x000F
            // KeyQ (0x0010) to KeyP (0x0019) -> default
            case "BracketLeft": return KeyCode.OPEN_BRACKET; // 0x001A
            case "BracketRight": return KeyCode.CLOSE_BRACKET; // 0x001B
            case "Enter": return KeyCode.ENTER; // 0x001C
            case "ControlLeft": return KeyCode.CONTROL; // 0x001D
            // KeyA (0x001E) to KeyL (0x0026) -> default
            case "Semicolon": return KeyCode.SEMICOLON; // 0x0027
            case "Quote": return KeyCode.QUOTE; // 0x0028
            case "Backquote": return KeyCode.BACK_QUOTE; // 0x0029
            case "ShiftLeft": return KeyCode.SHIFT; // 0x002A
            case "Backslash": return KeyCode.BACK_SLASH; // 0x002B
            // KeyZ (0x002C) to KeyM (0x0032) -> default
            case "Comma": return KeyCode.COMMA; // 0x0033
            case "Period": return KeyCode.PERIOD; // 0x0034
            case "Slash": return KeyCode.SLASH; // 0x0035
            case "ShiftRight" : return KeyCode.SHIFT; // 0x0036
            case "NumpadMultiply": return KeyCode.MULTIPLY; // 0x0037
            case "AltLeft": return KeyCode.ALT; // 0x0038
            case " ":
            case "Space": return KeyCode.SPACE; // 0x0039
            case "CapsLock": return KeyCode.CAPS; // 0x003A
            // F1 (0x003B) to F10 (0x0044) -> default
            case "Pause": return KeyCode.PAUSE; // 0x0045
            case "ScrollLock": return KeyCode.SCROLL_LOCK; // 0x0046
            // Numpad7 (0x0047) to Numpad9 (0x0049) -> default
            case "NumpadSubtract": return KeyCode.SUBTRACT; // 0x004A
            // Numpad4 (0x004B) to Numpad6 (0x004D) -> default
            case "NumpadAdd": return KeyCode.ADD; // 0x004E
            // Numpad1 (0x004F) to Numpad0 (0x0052) -> default
            case "NumpadDecimal": return KeyCode.DECIMAL; // 0x0053
            case "PrintScreen": return KeyCode.PRINTSCREEN; // 0x0054
            case "IntlBackslash": return KeyCode.BACK_SLASH; // 0x0056
            // F11 (0x0057) to F12 (0x0058) -> default
            case "NumpadEqual": return KeyCode.EQUALS; // 0x0059
            // F13 (0x0057) to F23 (0x006E) -> default
            case "KanaMode": return KeyCode.KANA; // 0x0070
            case "Convert": return KeyCode.CONVERT; // 0x0079
            case "NonConvert": return KeyCode.NONCONVERT; // 0x007B
            case "NumpadComma": return KeyCode.COMMA; // 0x007E
            case "Paste": return KeyCode.PASTE; // 0xE00A
            case "MediaTrackPrevious": return KeyCode.TRACK_PREV; // 0xE010
            case "Cut": return KeyCode.CUT; // 0xE018
            case "Copy": return KeyCode.COPY; // 0xE018
            case "MediaTrackNext": return KeyCode.TRACK_NEXT; // 0xE019
            case "NumpadEnter": return KeyCode.ENTER; // 0xE01C
            case "ControlRight": return KeyCode.CONTROL; // 0xE01D
            case "VolumeMute":
            case "AudioVolumeMute": return KeyCode.MUTE; // 0xE020
            case "MediaPlayPause": return KeyCode.PAUSE; // 0xE022
            case "MediaStop": return KeyCode.STOP; // 0xE024
            case "Eject": return KeyCode.EJECT_TOGGLE; // 0xE02D
            case "VolumeDown":
            case "AudioVolumeDown": return KeyCode.VOLUME_DOWN; // 0xE02E
            case "VolumeUp":
            case "AudioVolumeUp": return KeyCode.VOLUME_UP; // 0xE030
            case "BrowserHome": return KeyCode.HOME; // 0xE032
            case "NumpadDivide": return KeyCode.DIVIDE; // 0xE035
            case "AltRight": return KeyCode.ALT_GRAPH; // 0xE038
            case "Help": return KeyCode.HELP; // 0xE03B
            case "NumLock": return KeyCode.NUM_LOCK; // 0xE045
            case "Home": return KeyCode.HOME; // 0xE047
            case "ArrowUp": return KeyCode.UP; // 0xE048
            case "PageUp": return KeyCode.PAGE_UP; // 0xE049
            case "ArrowLeft": return KeyCode.LEFT; // 0xE04B
            case "ArrowRight": return KeyCode.RIGHT; // 0xE04D
            case "End": return KeyCode.END; // 0xE04F
            case "ArrowDown": return KeyCode.DOWN; // 0xE050
            case "PageDown": return KeyCode.PAGE_DOWN; // 0xE051
            case "Insert": return KeyCode.INSERT; // 0xE052
            case "Delete": return KeyCode.DELETE; // 0xE053
            case "OSLeft":
            case "MetaLeft": return KeyCode.META; // 0xE05B
            case "OSRight":
            case "MetaRight": return KeyCode.META; // 0xE05C
            case "ContextMenu": return KeyCode.CONTEXT_MENU; // 0xE05D
            case "Power": return KeyCode.POWER; // 0xE05E
            default: {
                String fxKeyName = htmlKey;
                int length = htmlKey.length();
                if (length == 6 && htmlKey.startsWith("Digit")) // Digit0 to Digit9
                    fxKeyName = String.valueOf(htmlKey.charAt(5)); // -> 0 to 9
                else if (length == 4 && htmlKey.startsWith("Key")) // KeyQ, etc...
                    fxKeyName = String.valueOf(htmlKey.charAt(3)); // -> Q, etc...
                else if (htmlKey.startsWith("Numpad"))
                    fxKeyName = Strings.replaceAll(htmlKey, "Numpad", "Numpad ");
                KeyCode keyCode = KeyCode.getKeyCode(fxKeyName);
                if (keyCode == null)
                    keyCode = KeyCode.getKeyCode(fxKeyName.toUpperCase());
                if (keyCode == null)
                    keyCode = KeyCode.UNDEFINED;
                return keyCode;
            }
        }
    }

    private SvgRoot svgRoot;

    public SvgRoot getSvgRoot() {
        if (svgRoot == null) {
            SVGSVGElement svgRootBaseSvg = SvgUtil.createSvgElement();
            svgRootBaseSvg.setAttribute("width", "0");
            svgRootBaseSvg.setAttribute("height", "0");
            svgRoot = new SvgRootBase();
            svgRootBaseSvg.append(svgRoot.getDefsElement());
            document.body.appendChild(svgRootBaseSvg);
            Console.log("svgRootBaseSvg added to document.body");
        }
        return svgRoot;
    }

    // Utility method to help map observable lists

    private static <T> void mapObservableList(ObservableList<T> ol, Consumer<List<? extends T>> adder, Consumer<List<? extends T>> remover) {
        adder.accept(ol);
        ol.addListener((ListChangeListener<T>) change -> {
            while (change.next()) {
                if (change.wasRemoved() || change.wasUpdated())
                    remover.accept(change.getRemoved());
                if (change.wasAdded() || change.wasUpdated())
                    adder.accept(change.getAddedSubList());
            }
        });
    }
}
