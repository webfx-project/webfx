package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import com.google.gwt.core.client.JavaScriptObject;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.TextPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.TextPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.HasSizeChangedCallback;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutCache;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutMeasurableNoHGrow;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.util.Numbers;
import dev.webfx.platform.util.Objects;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.HTMLElement;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
        // Commented line-height: 100% below, because of Text being used in LabeledSkinBase (see noWrappingText field)
        // for measuring label text width & height => must return same measures as label (so with normal line-height)
        // TODO: See if it's a deviation from JavaFX (check the expected Text height in JavaFX)
        // setElementStyleAttribute("line-height", "100%"); // 100% means node height = font height with no extra on top & bottom
    }

    private Runnable sizeChangedCallback;

    @Override
    public void setSizeChangedCallback(Runnable sizeChangedCallback) {
        this.sizeChangedCallback = sizeChangedCallback;
    }

    @Override
    public void updateEffect(Effect effect) {
        CSSStyleDeclaration style = getElement().style;
        if (effect instanceof DropShadow)
            style.textShadow = toCssTextShadow((DropShadow) effect);
        else {
            style.textShadow = null;
            super.updateEffect(effect);
        }
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
        String color;
        if (fill == null || fill instanceof Color)
            color = HtmlPaints.toCssColor((Color) fill);
        else {
            color = "transparent";
            super.updateFill(fill); // Apply the fill (ex: gradient) to the background
            setElementStyleAttribute("background-clip", "text");
        }
        getElement().style.color = color;
    }

    @Override
    public void updateTextOrigin(VPos textOrigin) {
        updateYOnNextPulse();
    }


    // Obscure JavaFX behaviour: layout positioning can take precedence over direct positioning (x,y), and in this case
    // (x,y) are just ignored. But in a Pane, it's possible to set layoutX,Y + (x,y), and they will be both considered
    // and added. So, having layoutX,Y positioned doesn't necessary mean that (x,y) are ignored. It's not cleared when
    // direct positioning (x,y) is applicable. The following rules seem to work in practice, so far:
    // 1) if the node is not managed => layout positioning
    // 2) if the node is in a Group => direct positioning
    // 3) if the node is in a Parent, Region or Pane (but not subclasses) => direct positioning
    private boolean isDirectPositioningApplicable() {
        N node = getNode();
        if (!node.isManaged())
            return false; // Otherwise this breaks the WebFX Website text positions (Card titles & circle letters)
        Parent parent = node.getParent();
        if (parent == null || parent instanceof Group)
            return true;
        Class<? extends Parent> parentClass = parent.getClass();
        return parentClass.equals(Parent.class) || parentClass.equals(Region.class) || parentClass.equals(Pane.class);
    }

    @Override
    public void updateX(Double x) {
        setElementStyleAttribute("left", isDirectPositioningApplicable() ? toPx(x) : "0");
    }

    @Override
    public void updateY(Double y) {
        if (isDirectPositioningApplicable()) { // Direct positioning
            double top = y;
            switch (getNode().getTextOrigin()) {
                case CENTER:   top = y - getLayoutBounds().getHeight() / 2; break;
                case BOTTOM:   top = y - getLayoutBounds().getHeight(); break;
                case BASELINE: {
                    Font font = getNode().getFont();
                    if (font != null)
                        top = y - font.getBaselineOffset();
                }
            }
            setElementStyleAttribute("top", toPx(top));
        } else { // Layout positioning (case when it takes precedence over direct positioning)
            setElementStyleAttribute("top", "0");
        }
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
        clearCache();
        updateYOnNextPulse();
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
