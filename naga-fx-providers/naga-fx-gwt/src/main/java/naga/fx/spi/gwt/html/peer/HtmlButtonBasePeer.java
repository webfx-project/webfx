package naga.fx.spi.gwt.html.peer;

import elemental2.dom.Element;
import elemental2.dom.Event;
import elemental2.dom.HTMLElement;
import emul.javafx.event.ActionEvent;
import emul.javafx.scene.Node;
import emul.javafx.scene.control.ButtonBase;
import naga.commons.util.Strings;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.ButtonBasePeerBase;
import naga.fx.spi.peer.base.ButtonBasePeerMixin;

/**
 * @author Bruno Salmon
 */
abstract class HtmlButtonBasePeer
        <N extends ButtonBase, NB extends ButtonBasePeerBase<N, NB, NM>, NM extends ButtonBasePeerMixin<N, NB, NM>>

        extends HtmlLabeledPeer<N, NB, NM>
        implements ButtonBasePeerMixin<N, NB, NM> {

    HtmlButtonBasePeer(NB base, HTMLElement element) {
        super(base, element);
        element.style.overflow = "hidden"; // hiding button content overflow
    }

    protected void onClickElement(Event e) {
        N node = getNode();
        node.fireEvent(new ActionEvent(node, node));
    }

    @Override
    public void updateText(String text) {
        updateHtmlContent();
    }

    @Override
    public void updateGraphic(Node graphic) {
        updateHtmlContent();
    }

    protected void updateHtmlContent() {
        N node = getNode();
        if (node.getSkin() == null) {
            // Embedding text into a span element so that we can align it with a possible graphic (image on the left)
            HTMLElement spanElement = HtmlUtil.createSpanElement();
            HtmlUtil.setStyleAttribute(spanElement, "vertical-align", "middle");
            HTMLElement buttonElement = getElement();
            Node graphic = node.getGraphic();
            String text = Strings.toSafeString(node.getText());
            if (text.isEmpty() && graphic == null)
                spanElement.innerHTML = "&nbsp;";
            else
                spanElement.textContent = text;
            if (graphic == null)
                HtmlUtil.setChild(buttonElement, spanElement);
            else {
                Element graphicElement = toElement(graphic, node.getScene());
                HtmlUtil.setStyleAttribute(graphicElement, "top", "0px");
                HtmlUtil.setStyleAttribute(graphicElement, "left", "0px");
                HtmlUtil.setChildren(buttonElement, graphicElement, spanElement);
            }
        }
        clearLayoutCache();
    }
}
