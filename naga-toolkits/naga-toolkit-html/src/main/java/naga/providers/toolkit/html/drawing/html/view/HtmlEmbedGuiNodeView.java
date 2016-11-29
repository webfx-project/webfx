package naga.providers.toolkit.html.drawing.html.view;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.drawing.scene.EmbedGuiNode;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.base.EmbedGuiNodeViewBase;
import naga.toolkit.drawing.spi.view.base.EmbedGuiNodeViewMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlEmbedGuiNodeView extends HtmlNodeView<EmbedGuiNode, EmbedGuiNodeViewBase, EmbedGuiNodeViewMixin> {

    public HtmlEmbedGuiNodeView() {
        super(new EmbedGuiNodeViewBase(), HtmlUtil.createDivElement());
    }

    @Override
    public void bind(EmbedGuiNode node, DrawingRequester drawingRequester) {
        setElementAttribute("width", "100%");
        setElementAttribute("height", "100%");
        HtmlUtil.setChild(getElement(), node.getGuiNode().unwrapToNativeNode());
        getNodeViewBase().bind(node, drawingRequester);
    }
}
