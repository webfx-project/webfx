package naga.fx.spi.gwt.html.peer;

import elemental2.dom.Element;
import elemental2.dom.HTMLScriptElement;
import elemental2.dom.Node;
import elemental2.dom.NodeList;
import naga.util.Strings;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fxdata.control.HtmlText;
import naga.fxdata.spi.peer.base.HtmlTextPeerBase;
import naga.fxdata.spi.peer.base.HtmlTextPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlHtmlTextPeer
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
        getElement().innerHTML = Strings.toSafeString(text);
        if (text.contains("<script"))
            executeScripts(getElement());
    }

    private void executeScripts(Node node) {
        if (node instanceof Element) {
            Element element = (Element) node;
            if ("SCRIPT".equals(element.tagName)) {
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