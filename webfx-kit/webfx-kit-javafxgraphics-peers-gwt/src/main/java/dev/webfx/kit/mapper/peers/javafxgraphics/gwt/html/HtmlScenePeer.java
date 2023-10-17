package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.HasNoChildrenPeers;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.base.ScenePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.HtmlSvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.FxEvents;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.util.Strings;
import dev.webfx.platform.util.collection.Collections;
import elemental2.dom.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.LayoutFlags;
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

    private final HTMLElement container = HtmlUtil.absolutePosition(HtmlUtil.createElement("fx-scene"));

    public HtmlScenePeer(Scene scene) {
        super(scene);
        HtmlUtil.setStyleAttribute(container, "width", "100%");
        HtmlUtil.setStyleAttribute(container, "height", "100vh"); // 100% is not good on mobile when the browser navigation bar is hidden, but 100vh works
        FXProperties.runNowAndOnPropertiesChange(property -> updateContainerFill(), scene.fillProperty());
        installMouseListeners();
        HtmlSvgNodePeer.installTouchListeners(container, scene);
        installKeyboardListeners(scene);
        installStylesheetsListener(scene);
        installFontsListener();
        installIconsListener();
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
        //registerMouseListener("click"); // Not necessary as the JavaFX Scene already generates them based on the mouse pressed and released events
        registerMouseListener("mouseenter");
        registerMouseListener("mouseleave");
        registerMouseListener("mousemove");
        container.oncontextmenu = e -> {
            //e.stopPropagation();
            e.preventDefault(); // To prevent the browser default context menu
            if (e instanceof MouseEvent // For now, we manage only context menu from the mouse
                // Also checking that we received the mouse up event on that scene before. This is to prevent the
                // following case: when a context menu is already displayed (=> in another popup window/scene) and
                // the user right-click on a menu item, the menu action will be triggered on the mouseup within the
                // popup window/scene (so far, so good) but then oncontextmenu is called on this scene by the browser
                // whereas the intention of the user was just to trigger the menu action (which also closes the
                // context menu) but not to display the context menu again. So we prevent this by checking the last
                // mouse event was a mouse up on that scene which is the correct sequence (in the above case, the
                // last mouse event will be the oncontextmenu event).
                /*&& lastMouseEvent != null && "mouseup".equals(lastMouseEvent.type)*/) {
                MouseEvent me = (MouseEvent) e;
                // Finally we generate the menu event for JavaFX
                listener.menuEvent(me.x, me.y, me.screenX, me.screenY, false);
            }
            return null;
        };
        // Disabling default browser drag & drop as JavaFX has its own
        //container.setAttribute("ondragstart", "return false;");
        //container.setAttribute("ondrop", "return false;");
    }

    private void registerMouseListener(String type) {
        container.addEventListener(type, e -> passHtmlMouseEventOnToFx((MouseEvent) e, type));
    }

    private boolean atLeastOneAnimationFrameOccurredSinceLastMousePressed = true;

    private void passHtmlMouseEventOnToFx(MouseEvent e, String type) {
        javafx.scene.input.MouseEvent fxMouseEvent = FxEvents.toFxMouseEvent(e, type);
        if (fxMouseEvent != null) {
            // We now need to call Scene.impl_processMouseEvent() to pass the event to the JavaFX stack
            Scene scene = getScene();
            // Also fixing a problem: mouse released and mouse pressed are sent very closely on mobiles and might be
            // treated in the same animation frame, which prevents the button pressed state (ex: a background bound to
            // the button pressedProperty) to appear before the action (which might be time-consuming) is fired, so the
            // user doesn't know if the button has been successfully pressed or not during the action execution.
            if (fxMouseEvent.getEventType() == javafx.scene.input.MouseEvent.MOUSE_RELEASED && !atLeastOneAnimationFrameOccurredSinceLastMousePressed)
                UiScheduler.scheduleInAnimationFrame(() -> scene.impl_processMouseEvent(fxMouseEvent), 1);
            else {
                scene.impl_processMouseEvent(fxMouseEvent);
                if (fxMouseEvent.getEventType() == javafx.scene.input.MouseEvent.MOUSE_PRESSED) {
                    atLeastOneAnimationFrameOccurredSinceLastMousePressed = false;
                    UiScheduler.scheduleInAnimationFrame(() -> atLeastOneAnimationFrameOccurredSinceLastMousePressed = true, 1);
                    /* Try to uncomment this code if the focus hasn't been updated after clicking on a Node (not necessary so far)
                    if (scene.mouseHandler.lastEvent != null) {
                        PickResult pickResult = scene.mouseHandler.lastEvent.getPickResult();
                        if (pickResult != null) {
                            Node node = pickResult.getIntersectedNode();
                            if (node != null && node.isFocusTraversable()) {
                                node.requestFocus();
                            }
                        }
                    }*/
                }
            }
            // Stopping propagation if the event has been consumed by JavaFX
            if (fxMouseEvent.isConsumed())
                e.stopPropagation();
            // Note: important to not stop propagation for third-party js components (ex: perfect-scrollbar)
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

    private void installFontsListener() {
        // Listening the fonts requested by the application code
        mapObservableList(Font.getRequestedFonts(), fonts -> addFonts(fonts), fonts -> removeFonts(fonts));
        // Listening other possible fonts loaded from HTML CSS
        document.fonts.getReady().then(p0 -> {
            // Once a font is loaded, the browser will apply it to all text elements set to use that font, and this will
            // probably change their size, but JavaFX won't detect that change, and this will create layout issues (ex:
            // a text supposed to be centered won't appear centered anymore with the new font). To fix those issues, we
            // call onCssOrFontLoaded() which forces a layout of the whole scene graph. However, we postpone that call
            // a few animation frames later, because the measurement of the text elements is still not considering the
            // new font at this point, a little delay seems necessary after fonts.getReady() (browser bug?).
            UiScheduler.scheduleInAnimationFrame(this::onCssOrFontLoaded, 5); // 5 animation frames seem enough in most cases
            UiScheduler.scheduleInAnimationFrame(this::onCssOrFontLoaded, 10);// but sometimes not, so we schedule also 5 frames later
            return null;
        });
    }

    private final Map<String /* url  => */, FontFace> fontFaces = new HashMap<>();

    private void addFonts(List<? extends Font> fonts) {
        fonts.forEach(font -> {
            FontFace fontFace = new FontFace(font.getFamily(), "url("  + font.getUrl() + ")");
            fontFaces.put(font.getUrl(), fontFace);
            document.fonts.add(fontFace);
            fontFace.load().then(p0 -> {
                onCssOrFontLoaded();
                Font.getLoadingFonts().remove(font);
                return null;
            });
        });
    }

    private void removeFonts(List<? extends Font> fonts) {
        fonts.forEach(font -> {
            FontFace fontFace = fontFaces.remove(font.getUrl());
            if (fontFace != null)
                document.fonts.delete(fontFace);
        });
    }

    private void onCssOrFontLoaded() {
        forceWholeSceneGraphLayout(scene);
    }

    private void installIconsListener() {
        FXProperties.onPropertySet(getScene().windowProperty(), window -> {
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

    private static void forceWholeSceneGraphLayout(Scene scene) {
        forceLayoutOnThisNodeAndChildren(scene.getRoot());
    }

    private static void forceLayoutOnThisNodeAndChildren(Node node) {
        if (node != null) {
            node.clearCache();
            if (node instanceof Parent) {
                Parent parent = (Parent) node;
                parent.setLayoutFlag(LayoutFlags.NEEDS_LAYOUT);
                parent.getChildren().forEach(HtmlScenePeer::forceLayoutOnThisNodeAndChildren);
            }
            node.onPeerSizeChanged();
        }
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
        // Checking that we pick it from the right scene (in case there are several windows/scenes within the DOM)
        if (peer != null) {
            Node node = peer.getNode();
            if (node == null || node.getScene() != getScene())
                peer = null;
        }
        return peer;
    }

    @Override
    public void onRootBound() {
        HtmlUtil.setChildren(container, HtmlSvgNodePeer.toContainerElement(scene.getRoot()));
    }

    @Override
    public void updateParentAndChildrenPeers(Parent parent, ListChangeListener.Change<? extends Node> childrenChange) {
        HtmlSvgNodePeer parentPeer = HtmlSvgNodePeer.toNodePeer(parent);
        // HasNoChildrenPeers have no JavaFX fxChildren (mapped to HTML) but they do have HTML fxChildren (generated by the peer)
        if (parentPeer instanceof HasNoChildrenPeers) // Ex: HtmlHtmlTextPeer, HtmlHtmlTextEditorPeer, HtmlVisualGridPeer
            return; // => we don't want to clear the HTML fxChildren generated by the peer
        Element childrenContainer = parentPeer.getChildrenContainer();
        ObservableList<Node> fxChildren = parent.getChildren();
        if (childrenChange == null) { // Indicates we need to map children (not just a partial change)
            List<Element> childElements = toChildElements(fxChildren, null);
            HtmlUtil.setChildren(childrenContainer, childElements);
        } else { // partial change of children. The above code (remap all children) would also work, but the code below
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
                    // Getting the associated elements we will need to add into the DOM
                    List<Element> childElementsToAdd = toChildElements(addedSubList, null);
                    // Getting the index of the node this is now just after these added nodes (changes have already being applied in fxChildren)
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

    public static void installKeyboardListeners(Scene scene) {
        registerKeyboardListener("keydown", scene);
        registerKeyboardListener( "keyup", scene);
    }

    private static void registerKeyboardListener(String type, Scene scene) {
        document.addEventListener(type, e -> {
            Node focusOwner = scene.getFocusOwner();
            // Resetting the focus owner to scene initial when it's not valid, so in the following cases:
            if (focusOwner == null // 1) No focus set yet
                    || focusOwner.getScene() != scene // 2) focus doesn't match the scene (probably removed from the scene)
                    || !focusOwner.impl_isTreeVisible()) { // 3) the focus is not visible in the scene graph (works also as a workaround when scene is not reset to null by WebFX)
                scene.focusInitial();
                focusOwner = scene.getFocusOwner();
            }
            javafx.event.EventTarget fxTarget = focusOwner != null ? focusOwner : scene;
            boolean fxConsumed = passHtmlKeyEventOnToFx((KeyboardEvent) e, type, fxTarget);
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
        EventType<KeyEvent> eventType;
        switch (type) {
            case "keydown": eventType = KeyEvent.KEY_PRESSED; break;
            case "keyup": eventType = KeyEvent.KEY_RELEASED; break;
            default: eventType = KeyEvent.KEY_TYPED;
        }
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
                    fxKeyName = Strings.replaceAll(htmlKey,"Numpad", "Numpad ");
                KeyCode keyCode = KeyCode.getKeyCode(fxKeyName);
                if (keyCode == null)
                    keyCode = KeyCode.getKeyCode(fxKeyName.toUpperCase());
                if (keyCode == null)
                    keyCode = KeyCode.UNDEFINED;
                return keyCode;
            }
        }
    }

    // Utility method to help mapping observable lists

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
