package webfx.fxkit.gwt.mapper.html.peer;

import elemental2.dom.Element;
import elemental2.dom.HTMLScriptElement;
import elemental2.dom.Node;
import elemental2.dom.NodeList;
import webfx.platforms.core.util.Strings;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkits.extra.control.HtmlText;
import webfx.fxkits.extra.mapper.spi.peer.impl.HtmlTextPeerBase;
import webfx.fxkits.extra.mapper.spi.peer.impl.HtmlTextPeerMixin;

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