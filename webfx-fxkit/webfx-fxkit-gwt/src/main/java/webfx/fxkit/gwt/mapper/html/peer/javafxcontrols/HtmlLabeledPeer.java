package webfx.fxkit.gwt.mapper.html.peer.javafxcontrols;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import webfx.fxkit.gwt.mapper.html.peer.javafxgraphics.HtmlImageViewPeer;
import webfx.fxkit.gwt.mapper.html.peer.javafxgraphics.HtmlNodePeer;
import webfx.fxkit.gwt.mapper.shared.HtmlSvgNodePeer;
import webfx.fxkit.gwt.mapper.util.HtmlPaints;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.LabeledPeerBase;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.LabeledPeerMixin;
import webfx.platform.shared.util.Strings;

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
                Element graphicElement = HtmlSvgNodePeer.toContainerElement(graphic, node.getScene());
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
        setElementStyleAttribute("text-align", HtmlNodePeer.toCssTextAlignment(textAlignment));
    }

    @Override
    public void updateTextFill(Paint textFill) {
        String cssPaint = HtmlPaints.toHtmlCssPaint(textFill);
        getElement().style.color = cssPaint;
        // Also if the labeled is associated with a monochrome svg image, showing the svg image in the same color
        Node graphic = getNode().getGraphic();
        if (graphic != null) {
            HtmlSvgNodePeer nodePeer = (HtmlSvgNodePeer) graphic.getNodePeer();
            if (nodePeer != null)
                HtmlImageViewPeer.applyTextFillToSvg(nodePeer.getContainer(), cssPaint);
        }
    }
}
