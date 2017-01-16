package naga.fx.spi.gwt.html.peer;

import elemental2.Element;
import elemental2.HTMLElement;
import naga.commons.util.Strings;
import javafx.event.ActionEvent;
import naga.fx.scene.Node;
import naga.fx.scene.SceneRequester;
import naga.fx.scene.control.ButtonBase;
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
            node.fireEvent(new ActionEvent(node, node));
            return null;
        };
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
        spanElement.textContent = Strings.toSafeString(getNode().getText());
        HtmlUtil.setStyleAttribute(spanElement, "vertical-align", "middle");
        HtmlUtil.setChild(getElement(), spanElement);
        Node graphic = node.getGraphic();
        if (graphic != null) {
            Element graphicElement = toElement(graphic, node.getScene());
            HtmlUtil.setStyleAttribute(graphicElement, "margin", " 0 5px 0 0");
            HtmlUtil.setStyleAttribute(graphicElement, "vertical-align", "middle");
            HtmlUtil.appendFirstChild(getElement(), graphicElement);
        }
    }
}
