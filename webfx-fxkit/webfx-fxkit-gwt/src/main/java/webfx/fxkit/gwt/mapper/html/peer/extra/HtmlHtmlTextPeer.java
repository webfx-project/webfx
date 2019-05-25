package webfx.fxkit.gwt.mapper.html.peer.extra;

import elemental2.dom.Element;
import elemental2.dom.HTMLScriptElement;
import elemental2.dom.Node;
import elemental2.dom.NodeList;
import webfx.fxkit.gwt.mapper.html.peer.HtmlLayoutMeasurable;
import webfx.fxkit.gwt.mapper.html.peer.javafxgraphics.HtmlRegionPeer;
import webfx.platform.shared.util.Strings;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkit.extra.controls.html.HtmlText;
import webfx.fxkit.extra.controls.mapper.spi.peer.impl.base.HtmlTextPeerBase;
import webfx.fxkit.extra.controls.mapper.spi.peer.impl.base.HtmlTextPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlHtmlTextPeer
        <N extends HtmlText, NB extends HtmlTextPeerBase<N, NB, NM>, NM extends HtmlTextPeerMixin<N, NB, NM>>
        extends HtmlRegionPeer<N, NB, NM>
        implements HtmlTextPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlHtmlTextPeer() {
        this((NB) new HtmlTextPeerBase());
    }

    HtmlHtmlTextPeer(NB base) {
        super(base, HtmlUtil.createSpanElement());
        setElementStyleAttribute("color", "white"); // Temporary hack to make the text white by default in the payment activity TODO: remove this hack
    }

    @Override
    public void updateText(String text) {
        text = Strings.toSafeString(text);
        getElement().innerHTML = text;
        if (text.contains("<script"))
            executeScripts(getElement());
    }

    private void executeScripts(Node node) {
        if (node instanceof Element) {
            Element element = (Element) node;
            if ("SCRIPT".equalsIgnoreCase(element.tagName)) {
                HTMLScriptElement script = HtmlUtil.createElement("script");
                script.text = element.innerHTML;
                for (int i = 0; i < element.attributes.length; i++)
                    script.setAttribute(element.attributes.getAt(i).name, element.attributes.getAt(i).value);
                element.parentNode.replaceChild(script, element);
                return;
            }
        }
        NodeList<Node> children = node.childNodes;
        for (int i = 0; i < children.length; i++)
            executeScripts(children.item(i));
    }

}