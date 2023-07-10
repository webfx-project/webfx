package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLElement;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import javafx.scene.shape.Rectangle;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RectanglePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.RectanglePeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlRectanglePeer
        <N extends Rectangle, NB extends RectanglePeerBase<N, NB, NM>, NM extends RectanglePeerMixin<N, NB, NM>>

        extends HtmlShapePeer<N, NB, NM>
        implements RectanglePeerMixin<N, NB, NM> {

    public HtmlRectanglePeer() {
        this((NB) new RectanglePeerBase(), HtmlUtil.createElement("fx-rectangle"));
    }

    public HtmlRectanglePeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    protected String computeClipPath() {
        Rectangle r = getNode();
        double width = r.getWidth();
        double height = r.getHeight();
        // Temporary hack: skipping empty rectangles because they are likely coming from wrong computation and cause
        // the node to be completely hidden (observed with Label). TODO: Fix problem cause (wrong computation)
        if (width == 0 && height == 0)
            return null;
        double top = r.getY();
        double left = r.getX();
        double right = left + width;
        double bottom = top + height;

        // Returning inset with a round property for round rectangles
        double cornerRadius = Math.max(r.getArcWidth() / 2, r.getArcHeight() / 2);
        if (cornerRadius > 0)
            // inset(inset-top inset-right inset-bottom inset-left round radius) <- values are relative to borders
            return "inset(" + toPx(top) + " " + toPx(width - right) + " " + toPx(height - bottom) + " " + toPx(left) + " round " + toPx(cornerRadius) + ")";

        // Otherwise returning polygon
        // Note: replaced toPx(top) by top + "px" etc... to preserve precision (required for Mandelbrot thumbnails zoom effect as scale is not 1)
        return "polygon(" + left + "px " + top + "px, " + right + "px " + top + "px, " + right + "px " + bottom + "px, " + left + "px " + bottom + "px)";
    }

    @Override
    public void updateX(Double x) {
        if (isClip())
            applyClipPathToClipNodes();
        else
            getElement().style.left = toPx(x);
    }

    @Override
    public void updateY(Double y) {
        if (isClip())
            applyClipPathToClipNodes();
        else
            getElement().style.top = toPx(y);
    }

    @Override
    public void updateWidth(Double width) {
        if (isClip())
            applyClipPathToClipNodes();
        else
            getElement().style.width = CSSProperties.WidthUnionType.of(toPx(width));
    }

    @Override
    public void updateHeight(Double height) {
        if (isClip())
            applyClipPathToClipNodes();
        else
            getElement().style.height = CSSProperties.HeightUnionType.of(toPx(height));
    }

    @Override
    public void updateArcWidth(Double arcWidth) {
        updateBorderRadius();
    }

    @Override
    public void updateArcHeight(Double arcHeight) {
        updateBorderRadius();
    }

    private void updateBorderRadius() {
        if (isClip())
            applyClipPathToClipNodes();
        else {
            Rectangle r = getNode();
            getElement().style.borderRadius = CSSProperties.BorderRadiusUnionType.of(toPx(r.getArcWidth()/2) + " " + toPx(r.getArcHeight()/2));
        }
    }
}
