package naga.providers.toolkit.html.fx.svg.view;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.scene.EmbedGuiNode;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.base.EmbedGuiNodeViewerBase;
import naga.toolkit.fx.spi.viewer.base.EmbedGuiNodeViewerMixin;

/**
 * @author Bruno Salmon
 */
public class SvgEmbedGuiNodeViewer extends SvgNodeViewer<EmbedGuiNode, EmbedGuiNodeViewerBase, EmbedGuiNodeViewerMixin> {

    public SvgEmbedGuiNodeViewer() {
        super(new EmbedGuiNodeViewerBase(), SvgUtil.createSvgElement("foreignObject"));
    }

    @Override
    public void bind(EmbedGuiNode node, DrawingRequester drawingRequester) {
        setElementAttribute("width", "100%");
        setElementAttribute("height", "100%");
        HtmlUtil.setChild(getElement(), node.getGuiNode().unwrapToNativeNode());
        getNodeViewerBase().bind(node, drawingRequester);
    }
}
