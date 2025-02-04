package dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html;

import dev.webfx.kit.mapper.peers.javafxgraphics.base.TextPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.TextPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.HasSizeChangedCallback;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.layoutmeasurable.HtmlLayoutCache;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.layoutmeasurable.HtmlLayoutMeasurableNoHGrow;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;
import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.util.Numbers;
import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLElement;
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
        setElementTextContent(text); // this clears the cache (if text is different)
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
        updateWrappingWithAndLineSpacing(Numbers.doubleValue(wrappingWidth), getNode().getLineSpacing());
    }

    @Override
    public void updateLineSpacing(Number lineSpacing) {
        updateWrappingWithAndLineSpacing(getNode().getWrappingWidth(), Numbers.doubleValue(lineSpacing));
    }

    private void updateWrappingWithAndLineSpacing(double wrappingWidth, double lineSpacing) {
        boolean isWrapping = wrappingWidth != 0;
        HTMLElement element = getElement();
        CSSStyleDeclaration style = element.style;
        // First, we set the HTML line height with no extra spacing between lines (lineSpacing = 0).
        // Note: it looks like JavaFX doesn't apply the same line height for single-line and multi-lines texts.
        // For single-line, it looks like HTML "normal", and for multi-line it looks like 130% (empiric value).
        style.lineHeight = isWrapping ? CSSProperties.LineHeightUnionType.of("130%") : null /*or pre-wrap?*/; // Note: 100% < normal < 130%
        // We correct the line height if an additional line spacing is requested
        if (isWrapping && lineSpacing != 0) { // not necessary if not wrapping (i.e. single line text)
            // There is no HTML equivalent of the JavaFX lineSpacing which specifies only the extra space (expressed in
            // pixels) between lines of space (and not the whole line height). So, to map this into HTML, we need to
            // 1) get the value of the HTML line height expressed in pixels, and 2) correct it by adding lineSpacing.
            // 1) Getting the value of line height in pixels (rather than "normal" or "130%"). To do that, we measure
            // the current height of the element for a single line of text.
            String currentText = element.textContent; // Memorising the current text (it may be a multi-line text)
            element.textContent = "W"; // Temporarily changing the text content to a single letter (=> ensures single-line)
            clearCache(); // Clearing the cache to ensure a fresh measurement
            double lineHeightPx = measure(element, false); // We mesure the height = line height in pixels
            element.textContent = currentText; // Re-establishing the current text
            // 2) Correcting the line height by adding the requested line spacing
            style.lineHeight = CSSProperties.LineHeightUnionType.of(toPx(lineHeightPx + lineSpacing));
        }
        // Mapping the wrapping with using the HTML maxWidth style attribute
        style.maxWidth = isWrapping ? CSSProperties.MaxWidthUnionType.of(toPx(wrappingWidth)) : null;
        // Mapping the wrapping mode using the HTML white-space style attribute
        style.whiteSpace = isWrapping ? "pre-wrap" : "pre";
        // Clearing the measurement cache because HTML attributes have changed
        clearCache();
        // An update of Y may be necessary (to consider textOrigin)
        updateYInAnimationFrame(true);
    }

    @Override
    public void updateStrikethrough(Boolean strikethrough) {
        setElementStyleAttribute("text-decoration", Boolean.TRUE.equals(strikethrough) ? "line-through" : null);
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
