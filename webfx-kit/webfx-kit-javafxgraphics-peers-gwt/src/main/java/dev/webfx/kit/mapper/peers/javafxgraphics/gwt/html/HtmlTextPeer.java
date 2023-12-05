package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.base.TextPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.TextPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.HasSizeChangedCallback;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutCache;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutMeasurableNoHGrow;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.util.Numbers;
import elemental2.dom.CSSStyleDeclaration;
import javafx.geometry.VPos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
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
        super(base, HtmlUtil.createElement("fx-text"));
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
        setElementTextContent(text);
        updateYInAnimationFrame(true);
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
        updateYInAnimationFrame(false);
    }

    @Override
    public void updateX(Double x) {
        setElementStyleAttribute("left", toPx(x));
    }

    @Override
    public void updateY(Double y) {
        double top = y;
        if (!getNode().isManaged()) { // It looks like textOrigin is ignored when the text node is managed
            switch (getNode().getTextOrigin()) {
                case CENTER: top -= getLayoutBounds().getHeight() / 2; break;
                case BOTTOM: top -= getLayoutBounds().getHeight(); break;
                case BASELINE: {
                    Font font = getNode().getFont();
                    if (font != null)
                        top -= font.getBaselineOffset();
                }
            }
        }
        setElementStyleAttribute("top", toPx(top));
    }

    private Runnable scheduledRunnable;
    private boolean scheduledSizeChanged;

    private void updateYInAnimationFrame(boolean sizeChanged) {
        if (sizeChanged)
            scheduledSizeChanged = true;
        if (scheduledRunnable == null) {
            scheduledRunnable = () -> {
                updateY(getNode().getY());
                if (scheduledSizeChanged) {
                    if (sizeChangedCallback != null)
                        sizeChangedCallback.run();
                    scheduledSizeChanged = false;
                }
                scheduledRunnable = null;
            };
            UiScheduler.schedulePropertyChangeInAnimationFrame(scheduledRunnable);
        }
    }

    @Override
    public void updateWrappingWidth(Double wrappingWidth) {
        double width = Numbers.doubleValue(wrappingWidth);
        if (width != 0)
            setElementStyleAttribute("width", toPx(width));
        // Setting the wrapping mode in HTML through white-space style attribute
        setElementStyleAttribute("white-space", width != 0 ? "normal" : "nowrap");
        clearCache();
        updateYInAnimationFrame(false);
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
        updateYInAnimationFrame(true);
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

    private final HtmlLayoutCache cache = new HtmlLayoutCache();
    @Override
    public HtmlLayoutCache getCache() {
        return cache;
    }

}
