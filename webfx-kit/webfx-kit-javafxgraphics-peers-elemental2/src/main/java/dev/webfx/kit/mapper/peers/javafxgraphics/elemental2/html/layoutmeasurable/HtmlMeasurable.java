package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.layoutmeasurable;

import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.measurable.Measurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.measurable.MeasurableCache;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.HtmlNodePeer;
import dev.webfx.platform.console.Console;
import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.DOMRect;
import elemental2.dom.HTMLElement;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

/**
 * @author Bruno Salmon
 */
public interface HtmlMeasurable extends Measurable {

    // TODO: reset all caches when fonts are loaded from CSS
    boolean ENABLE_CACHE = true;
    boolean DETECT_WRONG_CACHE = false;

    HTMLElement getElement();

    default Bounds getLayoutBounds() {
        Bounds layoutBounds;
        // Looking for the value in the cache (if the peer has one)
        MeasurableCache cache = ENABLE_CACHE ? getCache() : null;
        if (cache != null) {
            layoutBounds = cache.getCachedLayoutBounds();
            if (layoutBounds != null)
                return layoutBounds;
        }
        // Not in the cache, so we ask the browser to measure the layout bounds
        layoutBounds = measureLayoutBounds();
        // Memorizing the value (if the cache is present), unless the element is not connected (i.e., not in the DOM)
        // as the value is probably not relevant for future cases (ex: CSS not applied)
        if (cache != null && getElement().isConnected)
            cache.setCachedLayoutBounds(layoutBounds);
        return layoutBounds;
    }

    default Bounds measureLayoutBounds() {
        HTMLElement e = getElement();
        return new BoundingBox(0, 0, 0, measureElement(e, true), measureElement(e, false), 0);
    }

    default double minWidth(double height) {
        return 0;
    }

    default double maxWidth(double height) {
        return Double.MAX_VALUE;
    }

    default double minHeight(double width) {
        return 0;
    }

    default double maxHeight(double width) {
        return Double.MAX_VALUE;
    }

    default double prefWidth(double height) {
        return measureWidth(height);
    }

    default double prefHeight(double width) {
        return measureHeight(width);
    }

    default double measureWidth(double height) {
        return getCacheOrMeasureElement(getElement(), true, height);
    }

    default double measureHeight(double width) {
        return getCacheOrMeasureElement(getElement(), false, width);
    }

    default double getCacheOrMeasureElement(HTMLElement e, boolean measureWidth, double otherSizeValue) {
        MeasurableCache cache = ENABLE_CACHE ? getCache() : null;
        double cachedSize = cache == null ? -1 : cache.getCachedSize(otherSizeValue, measureWidth);
        if (!DETECT_WRONG_CACHE && cachedSize >= 0)
            return cachedSize;
        double measure = prepareAndMeasureElement(e, measureWidth, otherSizeValue);
        if (cache != null && e.isConnected) { // no cache for non-connected elements (as explained above)
            if (DETECT_WRONG_CACHE && cachedSize >= 0 && cachedSize != measure) {
                Console.log("⚠️ Warning: cached " + (measureWidth ? "width" : "height") + " differs from measured: " + cachedSize + " != " + measure + " for " + this + ", style = " + e.style.cssText);
            }
            cache.setCachedSize(otherSizeValue, measureWidth, measure);
        }
        return measure;
    }

    default double prepareAndMeasureElement(HTMLElement e, boolean measureWidth, double otherSizeValue) {
        CSSStyleDeclaration style = e.style;
        CSSProperties.WidthUnionType originalStyleWidth = style.width;
        CSSProperties.HeightUnionType originalStyleHeight = style.height;
        String otherSizeValuePx = otherSizeValue >= 0 ? HtmlNodePeer.toPx(otherSizeValue) : null;
        if (measureWidth) {
            style.width = null;
            if (otherSizeValue >= 0)
                style.height = CSSProperties.HeightUnionType.of(otherSizeValuePx);
        } else {
            if (otherSizeValue >= 0)
                style.width = CSSProperties.WidthUnionType.of(otherSizeValuePx);
            style.height = null;
        }
        double measure = measureElement(e, measureWidth);
        // Restoring the original style after measurement
        style.width = originalStyleWidth;
        style.height = originalStyleHeight;
        return measure;
    }

    default double measureElement(HTMLElement e, boolean measureWidth) {
        // offsetWidth & offsetHeight return the correct values (including transforms); unfortunately, their precision is
        // only integer... This diminution can cause problems (ex: text in Label wrapped to the next line while it shouldn't).
        int i = measureWidth ? e.offsetWidth : e.offsetHeight;
        // So we try to get better precision (double) using getBoundingClientRect(), unfortunately it doesn't consider
        // transforms... But we will prefer it in case there are no transforms
        DOMRect bcr = e.getBoundingClientRect();
        double d = measureWidth ? bcr.width : bcr.height;
        if (i == (int) d) // If the double precision matches the integer precision, it's likely there is no transform,
            return d; // so we return this more precise value
        // Otherwise, it's likely that there is a transform, so we return the integer precision value
        return i;
        // TODO: Investigate this solution that may work return double precision even with transform:
        // var matrix = new DOMMatrix(getComputedStyle(e).transform);
        // const p1 = matrix.transformPoint(new DOMPoint(bcr.x, bcr.y));
        // const p2 = matrix.transformPoint(new DOMPoint(bcr.x + brc.width, bcr.y + bcr.height));
        // return width ? p2.x - p1.x : p2.y - p1.y;
    }

    default MeasurableCache getCache() {
        return null;
    }

    @Override
    default void clearCache() {
        MeasurableCache cache = getCache();
        if (cache != null)
            cache.clearCache();
    }
}
