package naga.providers.toolkit.html.drawing.view;

import naga.providers.toolkit.html.util.HtmlUtil;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.drawing.shapes.EmbedDrawable;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.base.EmbedDrawableViewBase;
import naga.toolkit.drawing.spi.view.base.EmbedDrawableViewMixin;

/**
 * @author Bruno Salmon
 */
public class SvgEmbedDrawingView extends SvgDrawableView<EmbedDrawable, EmbedDrawableViewBase, EmbedDrawableViewMixin> {

    public SvgEmbedDrawingView() {
        super(new EmbedDrawableViewBase(), SvgUtil.createSvgElement("foreignObject"));
    }

    @Override
    public void bind(EmbedDrawable drawable, DrawingRequester drawingRequester) {
        setSvgAttribute("width", "100%");
        setSvgAttribute("height", "100%");
        HtmlUtil.setChild(getElement(), drawable.getGuiNode().unwrapToNativeNode());
    }
}
