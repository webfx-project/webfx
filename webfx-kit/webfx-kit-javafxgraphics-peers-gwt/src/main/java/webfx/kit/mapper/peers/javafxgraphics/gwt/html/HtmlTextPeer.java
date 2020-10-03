package webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import com.google.gwt.core.client.JavaScriptObject;
import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.HTMLElement;
import javafx.geometry.VPos;
import webfx.kit.mapper.peers.javafxgraphics.emul_coupling.HasSizeChangedCallback;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutCache;
import webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutMeasurableNoHGrow;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import webfx.kit.mapper.peers.javafxgraphics.base.TextPeerBase;
import webfx.kit.mapper.peers.javafxgraphics.base.TextPeerMixin;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.util.Numbers;
import webfx.platform.shared.util.Objects;

/**
 * @author Bruno Salmon
 */
public final class HtmlTextPeer
        <N extends Text, NB extends TextPeerBase<N, NB, NM>, NM extends TextPeerMixin<N, NB, NM>>
        extends HtmlShapePeer<N, NB, NM>
        implements TextPeerMixin<N, NB, NM>, HtmlLayoutMeasurableNoHGrow, HasSizeChangedCallback {

    public HtmlTextPeer() {
        this((NB) new TextPeerBase());
    }

    public HtmlTextPeer(NB base) {
        super(base, HtmlUtil.createSpanElement());
        setElementStyleAttribute("line-height", "100%");
    }

    private Runnable sizeChangedCallback;

    @Override
    public void setSizeChangedCallback(Runnable sizeChangedCallback) {
        this.sizeChangedCallback = sizeChangedCallback;
    }

    @Override
    public void updateEffect(Effect effect) {
        if (effect instanceof DropShadow)
            getElement().style.textShadow = toCssTextShadow((DropShadow) effect);
        else
            super.updateEffect(effect);
    }

    private String toCssTextShadow(DropShadow shadow) {
        return toPx(shadow.getOffsetX()) + " " + toPx(shadow.getOffsetY()) + " " + toPx(shadow.getRadius()) + " " + HtmlPaints.toHtmlCssPaint(shadow.getColor());
    }

    @Override
    public void updateText(String text) {
        currentTextMetrics = null;
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
        if (textOrigin == VPos.CENTER || textOrigin == VPos.BOTTOM) {
            double clientHeight = getLayoutBounds().getHeight(); // time consuming call
            if (textOrigin == VPos.CENTER)
                y = y - clientHeight / 2;
            else // if (textOrigin == VPos.BOTTOM)
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
                if (sizeChangedCallback != null)
                    sizeChangedCallback.run();
            };
            UiScheduler.schedulePropertyChangeInAnimationFrame(updateYRunnable);
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
        clearCache();
    }

    @Override
    public void updateFont(Font font) {
        setFontAttributes(font);
        updateYOnNextPulse();
        clearCache();
    }

    @Override
    protected void updateStroke() {
        N shape = getNode();
        String color = HtmlPaints.toHtmlCssPaint(shape.getStroke());
        Double strokeWidth = shape.getStrokeWidth();
        boolean hasStroke = color != null && strokeWidth > 0;
        setElementStyleAttribute("-webkit-text-stroke-color", hasStroke ? color : null);
        setElementStyleAttribute("-webkit-text-stroke-width", hasStroke ? toPx(strokeWidth) : null);
        clearCache();
    }

    private static final HTMLCanvasElement canvas = HtmlUtil.createElement("canvas");
    private String lastFont;
    private JavaScriptObject currentTextMetrics;

    @Override
    public double measureWidth(double height) {
        if (getCurrentTextMetrics() != null)
            return getTextMetricsWidth(currentTextMetrics);
        return sizeAndMeasure(height, true);
    }

/*
    @Override
    public double measureHeight(double width) {
        return 17;
    }
*/

    private final HtmlLayoutCache cache = new HtmlLayoutCache();
    @Override
    public HtmlLayoutCache getCache() {
        return cache;
    }

    private JavaScriptObject getCurrentTextMetrics() {
        HTMLElement element = getElement();
        String font = element.style.font;
        if (font == null || font.isEmpty())
            return null;
        if (currentTextMetrics == null || !Objects.areEquals(font, lastFont))
            currentTextMetrics = getTextMetrics(canvas, getNode().getText(), lastFont = font);
        return currentTextMetrics;
    }


    private native JavaScriptObject getTextMetrics(HTMLCanvasElement canvas, String text, String font)/*-{
        var context = canvas.getContext("2d");
        context.font = font;
        return context.measureText(text);
    }-*/;

    private static native double getTextMetricsWidth(JavaScriptObject metrics)/*-{
        return metrics.width;
    }-*/;
}
