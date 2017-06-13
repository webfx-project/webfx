package naga.fx.spi.gwt.html.peer;

import elemental2.Element;
import elemental2.Event;
import elemental2.HTMLElement;
import emul.javafx.event.ActionEvent;
import emul.javafx.scene.Node;
import emul.javafx.scene.control.ButtonBase;
import naga.commons.util.Strings;
import naga.fx.scene.SceneRequester;
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
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        getElement().onclick = e -> {
            onClick(e);
            return null;
        };
    }

    protected void onClick(Event e) {
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
        // Embedding text into a span element so that we can align it with a possible graphic (image on the left)
        HTMLElement spanElement = HtmlUtil.createSpanElement();
        HtmlUtil.setStyleAttribute(spanElement, "vertical-align", "middle");
        HtmlUtil.setChild(getElement(), spanElement);
        Node graphic = node.getGraphic();
        String text = Strings.toSafeString(node.getText());
        if (text.isEmpty() && graphic == null)
            spanElement.innerHTML = "&nbsp;";
        else
            spanElement.textContent = text;
        if (graphic != null) {
            Element graphicElement = toElement(graphic, node.getScene());
            HtmlUtil.setStyleAttribute(graphicElement, "margin", " 0 5px 0 0");
            HtmlUtil.setStyleAttribute(graphicElement, "vertical-align", "middle");
            HtmlUtil.setStyleAttribute(graphicElement, "position", "relative");
            HtmlUtil.appendFirstChild(getElement(), graphicElement);
            for (double i = 0, n =  graphicElement.childNodes.length; i < n; i++) {
                elemental2.Node childNode = graphicElement.childNodes.get(i);
                HtmlUtil.setStyleAttribute(childNode, "margin", " 0 5px 0 0");
                HtmlUtil.setStyleAttribute(childNode, "vertical-align", "middle");
                HtmlUtil.setStyleAttribute(childNode, "position", "relative");
            }
        }
        clearLayoutCache();
    }
}
