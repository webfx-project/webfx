package naga.fx.spi.gwt.util;

import com.google.gwt.core.client.JavaScriptObject;
import elemental2.dom.*;
import naga.commons.util.Strings;
import naga.commons.util.async.Future;

import static elemental2.dom.DomGlobal.document;

/**
 * @author Bruno Salmon
 */
public class HtmlUtil {

    public static <N extends Node> N removeChildren(N node) {
        if (node != null)
            while (node.firstChild != null)
                node.removeChild(node.firstChild);
        return node;
    }

    public static <N extends Node> N appendChild(N parent, Node child) {
        if (parent != null && child != null)
            parent.appendChild(child);
        return parent;
    }

    public static <N extends Node> N appendFirstChild(N parent, Node child) {
        if (parent != null && child != null)
            parent.insertBefore(child, parent.firstChild);
        return parent;
    }

    public static <N extends Node> N setChild(N parent, Node child) {
        return appendChild(removeChildren(parent), child);
    }

    public static <N extends Node> N setChildren(N parent, Iterable<? extends Node> children) {
        Element activeElement = getActiveElement(); // Getting the focused element in case we loose it
        appendChildren(removeChildren(parent), children); // Removing children may cause a focus lost!
        if (activeElement != null) // Restoring the focused element in case we lost it
            activeElement.focus(); // (works in all browsers but not IE for any reason)
        return parent;
    }

    public static <N extends Node> N setChildren(N parent, Node... children) {
        Element activeElement = getActiveElement(); // Getting the focused element in case we loose it
        appendChildren(removeChildren(parent), children); // Removing children may cause a focus lost!
        if (activeElement != null) // Restoring the focused element in case we lost it
            activeElement.focus(); // (works in all browsers but not IE for any reason)
        return parent;
    }

    private static Element getActiveElement() {
        return getActiveElement(document);
    }

    private static native Element getActiveElement(Document document) /*-{
        return document.activeElement;
    }-*/;

    public static <N extends Node> N appendChildren(N parent, Iterable<? extends Node> children) {
        for (Node child : children)
            appendChild(parent, child);
        return parent;
    }

    public static <N extends Node> N appendChildren(N parent, Node... children) {
        for (Node child : children)
            appendChild(parent, child);
        return parent;
    }

    public static <N extends Node> N replaceNode(Node oldNode, N newNode) {
        if (oldNode != null && oldNode.parentNode != null)
            oldNode.parentNode.replaceChild(newNode, oldNode);
        return newNode;
    }

    public static HTMLBodyElement setBodyContent(Node content) {
        return appendChild(removeChildrenUpToScripts(document.body), content);
    }

    public static <N extends Node> N removeChildrenUpToScripts(N node) {
        if (node != null)
            while (node.firstChild != null && !(node.firstChild instanceof HTMLScriptElement))
                node.removeChild(node.firstChild);
        return node;
    }


    public static <E extends Element> E setAttribute(E e, String name, String value) {
        e.setAttribute(name, value);
        return e;
    }

    public static <E extends Element> E appendAttribute(E e, String name, String value, String separator) {
        return setAttribute(e, name, Strings.appendToken(e.getAttribute(name), value, separator));
    }

    public static <E extends HTMLElement> E setPseudoClass(E e, String pseudoClass) {
        return setAttribute(e, "class", pseudoClass);
    }

    public static <E extends HTMLElement> E setPseudoClass(E e, String pseudoClass, boolean present) {
        return present ? addPseudoClass(e, pseudoClass) : removePseudoClass(e, pseudoClass);
    }

    public static <E extends HTMLElement> E addPseudoClass(E e, String pseudoClass) {
        pseudoClass = " " + pseudoClass + " ";
        if (!e.className.contains(pseudoClass))
            e.className = Strings.concat(e.className, pseudoClass);
        return e;
    }

    public static <E extends HTMLElement> E removePseudoClass(E e, String pseudoClass) {
        pseudoClass = " " + pseudoClass + " ";
        return setPseudoClass(e, Strings.replaceAll(e.className, pseudoClass, ""));
    }

    public static <E extends Element> E setStyle(E e, String style) {
        return setAttribute(e, "style", style);
    }

    private final static String[] browserPrefixes = {"-webkit-", "-moz-"};
    private static String[] getAttributeBrowserPrefixes(String name) {
        switch (name) {
            case "clip-path":
                return browserPrefixes;
        }
        return null;
    }

    public static <N extends Node> N setStyleAttribute(N node, String name, Object value) {
        if (node instanceof Element)
            setStyleAttribute((Element) node, name, value);
        return node;
    }

    public static <E extends Element> E setStyleAttribute(E e, String name, Object value) {
        String[] prefixes = getAttributeBrowserPrefixes(name);
        if (prefixes != null)
            for (String prefix: prefixes)
                setPrefixedStyleAttribute(e, prefix + name, value);
        return setPrefixedStyleAttribute(e, name, value);
    }

    private static <E extends Element> E setPrefixedStyleAttribute(E e, String name, Object value) {
        //e.style.setProperty(name, Strings.toString(value));
        String s = Strings.toString(value);
        if (e instanceof HTMLElement)
            setJsAttribute(((JavaScriptObject) (Object) ((HTMLElement) e).style), name, s);
        else if (value != null)
            appendStyle(e, name + ": " + value);
        return e;
    }

    public static <E extends Element> E appendStyle(E e, String style) {
        return appendAttribute(e, "style", style, "; ");
    }

    private static native void setJsAttribute(JavaScriptObject o, String name, String value) /*-{
        o[name] = value;
    }-*/;

    public static native CSSStyleDeclaration getComputedStyle(Element e) /*-{
        return $wnd.getComputedStyle(e);
    }-*/;

    public static <E extends Element> E createElement(String tagName) {
        return (E) document.createElement(tagName);
    }

    public static HTMLButtonElement createButtonElement() {
        return createElement("button");
    }

    public static HTMLLabelElement createLabelElement() {
        return createElement("label");
    }

    public static HTMLTableElement createTableElement() {
        return createElement("table");
    }

    public static HTMLImageElement createImageElement() {
        return createElement("img");
    }

    public static HTMLDivElement createDivElement() {
        return createElement("div");
    }

    public static HTMLSelectElement createSelectElement() {
        return createElement("select");
    }

    public static HTMLOptionElement createOptionElement() {
        return createElement("option");
    }


    public static <E extends HTMLElement> E absolutePosition(E e) {
        e.style.position = "absolute";
        return e;
    }

    public static <E extends Node> E createNodeFromHtml(String innerHTML) {
        HTMLDivElement div = createDivElement();
        div.innerHTML = innerHTML;
        return (E) div.firstChild;
    }

    public static HTMLElement createSpanElement() {
        return createElement("span");
    }

    public static HTMLInputElement createInputElement(String type) {
        return setAttribute(createElement("input"), "type", type);
    }

    public static HTMLInputElement createTextInput() {
        return createInputElement("text");
    }

    public static HTMLTextAreaElement createTextArea() {
        return createElement("textarea");
    }

    public static HTMLInputElement createCheckBox() {
        return createInputElement("checkbox");
    }

    public static HTMLInputElement createRadioButton() {
        return createInputElement("radio");
    }

    public static <E extends Element> E getElementById(Element element, String id) {
        return getElementById(element, id, "*");
    }

    public static <E extends Element> E getElementById(Element element, String id, String tag) {
        NodeList<Element> elements = element.getElementsByTagName(tag);
        for (int i = 0; i < elements.length; i++) {
            Element e = elements.item(i);
            if (id.equals(e.getAttribute("id")))
                return (E) e;
        }
        return null;
    }

    public static Future<Void> loadScript(String src) {
        NodeList<Element> scriptElements = document.head.getElementsByTagName("script");
        for (double i = 0, n = scriptElements.getLength(); i < n; i++) {
            HTMLScriptElement headElement = (HTMLScriptElement) scriptElements.item(i);
            if (src.equals(headElement.src))
                return Future.succeededFuture();
        }
        Future<Void> future = Future.future();
        HTMLScriptElement script = createElement("script");
        script.onload = a -> {
            future.complete();
            return null;
        };
        script.src = src;
        document.head.appendChild(script);
        return future;
    }

    public static void onNodeInsertedIntoDocument(Node node, Runnable runnable) {
        // Deprecated API! TODO: use MutationObserver instead
        node.addEventListener("DOMNodeInsertedIntoDocument", evt -> runnable.run());
    }
}
