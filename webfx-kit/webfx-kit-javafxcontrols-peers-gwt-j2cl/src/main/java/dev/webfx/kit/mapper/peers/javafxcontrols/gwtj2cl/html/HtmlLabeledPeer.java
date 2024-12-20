package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import dev.webfx.kit.mapper.peers.javafxcontrols.base.LabeledPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.LabeledPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.HtmlImageViewPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.HtmlNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.shared.HtmlSvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;
import dev.webfx.platform.util.Strings;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

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
                Element graphicElement = HtmlSvgNodePeer.toContainerElement(graphic);
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

    @Override
    public void updateWrapText(boolean wrapText) {
        if (doesSkinRelyOnPeerToProvideVisualContent()) { // Note: for LabeledSkinBase, the wrapping is done though its internal Text node having a wrapping width -> see HtmlTextPeer.updateWrappingWidth()
            // TODO: replace hardcoded rule with CSS (ex: wrap-text class)
            setElementStyleAttribute("white-space", wrapText ? "pre-wrap" : "pre");
        }
    }

    @Override
    public void updateLineSpacing(Number lineSpacing) {
        if (doesSkinRelyOnPeerToProvideVisualContent()) { // Note: for LabeledSkinBase, the wrapping is done though its internal Text node having a line spacing -> see HtmlTextPeer.updateLineSpacing()
            // TODO: implement lineSpacing if necessary
        }
    }
}
