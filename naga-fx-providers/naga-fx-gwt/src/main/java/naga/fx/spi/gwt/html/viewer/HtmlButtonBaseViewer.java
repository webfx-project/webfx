package naga.fx.spi.gwt.html.viewer;

import elemental2.Element;
import elemental2.HTMLElement;
import naga.commons.util.Strings;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.scene.Node;
import naga.fx.scene.control.ButtonBase;
import naga.fx.spi.viewer.base.ButtonBaseViewerBase;
import naga.fx.spi.viewer.base.ButtonBaseViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class HtmlButtonBaseViewer
        <N extends ButtonBase, NB extends ButtonBaseViewerBase<N, NB, NM>, NM extends ButtonBaseViewerMixin<N, NB, NM>>

        extends HtmlLabeledViewer<N, NB, NM>
        implements ButtonBaseViewerMixin<N, NB, NM> {

    HtmlButtonBaseViewer(NB base, HTMLElement element) {
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
