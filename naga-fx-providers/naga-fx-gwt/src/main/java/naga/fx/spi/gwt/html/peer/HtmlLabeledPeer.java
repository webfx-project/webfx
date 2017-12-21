package naga.fx.spi.gwt.html.peer;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import emul.javafx.scene.Node;
import emul.javafx.scene.control.Labeled;
import emul.javafx.scene.paint.Paint;
import emul.javafx.scene.text.Font;
import emul.javafx.scene.text.TextAlignment;
import naga.fx.spi.gwt.util.HtmlPaints;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.LabeledPeerBase;
import naga.fx.spi.peer.base.LabeledPeerMixin;
import naga.util.Strings;

/**
 * @author Bruno Salmon
 */
abstract class HtmlLabeledPeer
        <N extends Labeled, NB extends LabeledPeerBase<N, NB, NM>, NM extends LabeledPeerMixin<N, NB, NM>>

        extends HtmlControlPeer<N, NB, NM>
        implements LabeledPeerMixin<N, NB, NM> {

    HtmlLabeledPeer(NB base, HTMLElement element) {
        super(base, element);
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
        if (doesSkinRelyOnPeerToProvideVisualContent()) {
            // Embedding text into a span element so that we can align it with a possible graphic (image on the left)
            HTMLElement spanElement = HtmlUtil.createSpanElement();
            HtmlUtil.setStyleAttribute(spanElement, "position", "relative");
            HtmlUtil.setStyleAttribute(spanElement, "vertical-align", "middle");
            HTMLElement element = getElement();
            N node = getNode();
            Node graphic = node.getGraphic();
            String text = Strings.toSafeString(node.getText());
            if (text.isEmpty() && graphic == null)
                spanElement.innerHTML = "&nbsp;";
            else
                spanElement.textContent = text;
            if (graphic == null)
                HtmlUtil.setChild(element, spanElement);
            else {
                Element graphicElement = toContainerElement(graphic, node.getScene());
                HtmlUtil.setStyleAttribute(graphicElement, "position", "relative");
                HtmlUtil.setChildren(element, graphicElement, spanElement);
            }
        }
        clearLayoutCache();
    }

    @Override
    public void updateFont(Font font) {
        setFontAttributes(font);
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
        setElementStyleAttribute("text-align", toCssTextAlignment(textAlignment));
    }

    @Override
    public void updateTextFill(Paint textFill) {
        getElement().style.color = HtmlPaints.toHtmlCssPaint(textFill);
    }
}
