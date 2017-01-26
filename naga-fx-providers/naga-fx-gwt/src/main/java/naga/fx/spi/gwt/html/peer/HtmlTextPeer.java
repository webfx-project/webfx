package naga.fx.spi.gwt.html.peer;

import emul.javafx.geometry.VPos;
import emul.javafx.scene.paint.Paint;
import emul.javafx.scene.text.Font;
import emul.javafx.scene.text.Text;
import emul.javafx.scene.text.TextAlignment;
import naga.commons.util.Numbers;
import naga.fx.spi.Toolkit;
import naga.fx.spi.gwt.util.HtmlPaints;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.TextPeerBase;
import naga.fx.spi.peer.base.TextPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlTextPeer
        <N extends Text, NB extends TextPeerBase<N, NB, NM>, NM extends TextPeerMixin<N, NB, NM>>
        extends HtmlShapePeer<N, NB, NM>
        implements TextPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlTextPeer() {
        this((NB) new TextPeerBase());
    }

    public HtmlTextPeer(NB base) {
        super(base, HtmlUtil.createSpanElement());
        setElementStyleAttribute("line-height", "100%");
    }

    @Override
    public void updateText(String text) {
        setElementTextContent(text);
        updateYOnNextPulse();
    }

    @Override
    public void updateFill(Paint fill) {
        getElement().style.color = HtmlPaints.toHtmlCssPaint(fill);
    }

    @Override
    public void updateTextOrigin(VPos textOrigin) {
        updateYOnNextPulse();
    }

    @Override
    public void updateX(Double x) {
        setElementStyleAttribute("left", toPx(x));
    }

    @Override
    public void updateY(Double y) {
        VPos textOrigin = getNode().getTextOrigin();
        if (textOrigin != null && textOrigin != VPos.TOP) {
            double clientHeight = getLayoutBounds().getHeight();
            if (textOrigin == VPos.CENTER)
                y = y - clientHeight / 2;
            else if (textOrigin == VPos.BOTTOM)
                y = y - clientHeight;
        }
        setElementStyleAttribute("top", toPx(y));
    }

    private Runnable updateYRunnable;
    private void updateYOnNextPulse() {
        if (updateYRunnable == null) {
            updateYRunnable = () -> {
                updateY(getNode().getY());
                updateYRunnable = null;
            };
            Toolkit.get().scheduler().scheduleAnimationFrame(updateYRunnable);
        }
    }

    @Override
    public void updateWrappingWidth(Double wrappingWidth) {
        double width = Numbers.doubleValue(wrappingWidth);
        if (width != 0)
            setElementStyleAttribute("width", toPx(width));
        clearCache();
        updateYOnNextPulse();
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
        setElementStyleAttribute("text-align", toCssTextAlignment(textAlignment));
    }

    @Override
    public void updateFont(Font font) {
        setFontAttributes(font);
        updateYOnNextPulse();
    }

    private final HtmlLayoutCache cache = new HtmlLayoutCache();
    @Override
    public HtmlLayoutCache getCache() {
        return cache;
    }
}
