package naga.providers.toolkit.html.fx.html.viewer;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.EmbedGuiNode;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.base.EmbedGuiNodeViewerBase;
import naga.toolkit.fx.spi.viewer.base.EmbedGuiNodeViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlEmbedGuiNodeViewer extends HtmlNodeViewer<EmbedGuiNode, EmbedGuiNodeViewerBase, EmbedGuiNodeViewerMixin> {

    public HtmlEmbedGuiNodeViewer() {
        super(new EmbedGuiNodeViewerBase(), HtmlUtil.createDivElement());
    }

    @Override
    public void bind(EmbedGuiNode node, DrawingRequester drawingRequester) {
        setElementAttribute("width", "100%");
        setElementAttribute("height", "100%");
        HtmlUtil.setChild(getElement(), node.getGuiNode().unwrapToNativeNode());
        getNodeViewerBase().bind(node, drawingRequester);
    }
}
