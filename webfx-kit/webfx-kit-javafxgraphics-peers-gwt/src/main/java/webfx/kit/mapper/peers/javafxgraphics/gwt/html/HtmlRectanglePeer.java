package webfx.kit.mapper.peers.javafxgraphics.gwt.html;

import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLElement;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import javafx.scene.shape.Rectangle;
import webfx.kit.mapper.peers.javafxgraphics.base.RectanglePeerBase;
import webfx.kit.mapper.peers.javafxgraphics.base.RectanglePeerMixin;

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
        // inset(top right bottom left round top-radius right-radius bottom-radius left-radius)
        double top = r.getY();
        double left = r.getX();
        double right = left + r.getWidth();
        double bottom = top + r.getHeight();
/*
            double leftRadius = r.getArcWidth() / 2, rightRadius = leftRadius;
            double topRadius = r.getArcHeight() / 2, bottomRadius = topRadius;
            return "inset(" + toPx(top) + " " + toPx(right) + " " + toPx(bottom) + " " + toPx(left) + " round " + topRadius + "px " + rightRadius + "px " + bottomRadius + "px " + leftRadius + "px)";
*/
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
