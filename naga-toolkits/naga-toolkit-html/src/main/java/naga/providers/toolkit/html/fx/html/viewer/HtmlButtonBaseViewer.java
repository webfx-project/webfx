package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.Element;
import elemental2.HTMLElement;
import naga.commons.util.Strings;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.ButtonBase;
import naga.toolkit.fx.spi.viewer.base.ButtonBaseViewerBase;
import naga.toolkit.fx.spi.viewer.base.ButtonBaseViewerMixin;

/**
 * @author Bruno Salmon
 */
abstract class HtmlButtonBaseViewer
        <N extends ButtonBase, NV extends ButtonBaseViewerBase<N, NV, NM>, NM extends ButtonBaseViewerMixin<N, NV, NM>>
        extends HtmlRegionViewer<N, NV, NM>
        implements ButtonBaseViewerMixin<N, NV, NM> {

    HtmlButtonBaseViewer(NV base, HTMLElement element) {
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
            Element graphicElement = toElement(graphic, node.getDrawing());
            HtmlUtil.setStyleAttribute(graphicElement, "margin", " 0 5px 0 0");
            HtmlUtil.setStyleAttribute(graphicElement, "vertical-align", "middle");
            HtmlUtil.appendFirstChild(getElement(), graphicElement);
        }
    }
}
