package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable;

import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.LayoutMeasurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlNodePeer;
import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.DOMRect;
import elemental2.dom.HTMLElement;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

/**
 * @author Bruno Salmon
 */
public interface HtmlLayoutMeasurable extends LayoutMeasurable {

    HTMLElement getElement();

    default Bounds getLayoutBounds() {
        Bounds layoutBounds;
        // Looking the value in the cache (if the peer has one)
        HtmlLayoutCache cache = getCache();
        if (cache != null) {
            layoutBounds = cache.getCachedLayoutBounds();
            if (layoutBounds != null)
                return layoutBounds;
        }
        // Not in the cache, so we ask the browser to measure the layout bounds
        layoutBounds = measureLayoutBounds();
        // Memorising the value (if cache present), unless the element is not connected (i.e. not in the DOM)
        // as the value is probably not relevant for future cases (ex: CSS not applied)
        if (cache != null && getElement().isConnected)
            cache.setCachedLayoutBounds(layoutBounds);
        return layoutBounds;
    }

    default Bounds measureLayoutBounds() {
        HTMLElement e = getElement();
        return new BoundingBox(0, 0, 0, measure(e, true), measure(e, false), 0);
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
        return sizeAndMeasure(height, true);
    }

    default double measureHeight(double width) {
        return sizeAndMeasure(width, false);
    }

    default double sizeAndMeasure(double value, boolean width) {
        HtmlLayoutCache cache = getCache();
        if (cache != null) {
            double cachedSize = cache.getCachedSize(value, width);
            if (cachedSize >= 0)
                return cachedSize;
        }
        HTMLElement e = getElement();
        CSSStyleDeclaration style = e.style;
        CSSProperties.WidthUnionType styleWidth = style.width;
        CSSProperties.HeightUnionType styleHeight = style.height;
        if (width) {
            style.width = null;
            if (value >= 0)
                style.height = CSSProperties.HeightUnionType.of(HtmlNodePeer.toPx(value));
        } else {
            if (value >= 0)
                style.width = CSSProperties.WidthUnionType.of(HtmlNodePeer.toPx(value));
            style.height = null;
        }
        double result = measure(e, width);
        style.width = styleWidth;
        style.height = styleHeight;
        if (cache != null && e.isConnected) // no cache for non-connected elements (as explained above)
            cache.setCachedSize(value, width, result);
        return result;
    }

    default double measure(HTMLElement e, boolean width) {
        // offsetWidth & offsetHeight return the correct values (including transforms), unfortunately their precision is
        // only integer... This diminution can cause problems (ex: text in Label wrapped to next line while it shouldn't).
        int i = width ? e.offsetWidth : e.offsetHeight;
        // So we try to get a better precision (double) using getBoundingClientRect(), unfortunately it doesn't consider
        // transforms... But we will prefer it in case there is no transforms
        DOMRect bcr = e.getBoundingClientRect();
        double d = width ? bcr.width : bcr.height;
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

    default HtmlLayoutCache getCache() {
        return null;
    }

    @Override
    default void clearCache() {
        HtmlLayoutCache cache = getCache();
        if (cache != null)
            cache.clearCache();
    }
}
