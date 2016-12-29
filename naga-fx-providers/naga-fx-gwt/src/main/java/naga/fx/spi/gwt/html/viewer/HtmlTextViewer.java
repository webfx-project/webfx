package naga.fx.spi.gwt.html.viewer;

import naga.commons.util.Numbers;
import naga.fx.spi.gwt.util.HtmlPaints;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.geometry.VPos;
import naga.fx.scene.paint.Paint;
import naga.fx.scene.text.Font;
import naga.fx.scene.text.Text;
import naga.fx.scene.text.TextAlignment;
import naga.fx.spi.viewer.base.TextViewerBase;
import naga.fx.spi.viewer.base.TextViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlTextViewer
        <N extends Text, NB extends TextViewerBase<N, NB, NM>, NM extends TextViewerMixin<N, NB, NM>>
        extends HtmlShapeViewer<N, NB, NM>
        implements TextViewerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlTextViewer() {
        this((NB) new TextViewerBase());
    }

    public HtmlTextViewer(NB base) {
        super(base, HtmlUtil.createSpanElement());
        setElementStyleAttribute("line-height", "100%");
    }

    @Override
    public void updateText(String text) {
        setElementTextContent(text);
        updateY();
    }

    @Override
    public void updateFill(Paint fill) {
        getElement().style.color = HtmlPaints.toHtmlCssPaint(fill);
    }

    @Override
    public void updateTextOrigin(VPos textOrigin) {
        updateY();
    }

    @Override
    public void updateX(Double x) {
        setElementStyleAttribute("left", toPx(x));
    }

    @Override
    public void updateY(Double y) {
        VPos textOrigin = getNode().getTextOrigin();
        if (textOrigin != null && textOrigin != VPos.TOP) {
            double clientHeight = getElement().clientHeight;
            if (textOrigin == VPos.CENTER)
                y = y - clientHeight / 2;
            else if (textOrigin == VPos.BOTTOM)
                y = y - clientHeight;
        }
        setElementStyleAttribute("top", toPx(y));
    }

    private void updateY() {
        updateY(getNode().getY());
    }

    @Override
    public void updateWrappingWidth(Double wrappingWidth) {
        double width = Numbers.doubleValue(wrappingWidth);
        if (width != 0)
            setElementStyleAttribute("width", toPx(width));
        updateY();
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
        setElementStyleAttribute("text-align", toCssTextAlignment(textAlignment));
    }

    @Override
    public void updateFont(Font font) {
        setFontAttributes(font);
        updateY();
    }
}
