package naga.fx.spi.swing.peer;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import naga.fx.scene.LayoutMeasurable;
import naga.fx.scene.paint.Paint;
import naga.fx.spi.peer.base.ShapePeerBase;
import naga.fx.spi.peer.base.ShapePeerMixin;
import com.sun.javafx.geom.Point2D;
import naga.fx.scene.shape.Shape;
import naga.fx.scene.shape.StrokeLineCap;
import naga.fx.scene.shape.StrokeLineJoin;
import naga.fx.scene.shape.StrokeType;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * @author Bruno Salmon
 */
abstract class SwingShapePeer
        <N extends Shape, NB extends ShapePeerBase<N, NB, NM>, NM extends ShapePeerMixin<N, NB, NM>>

        extends SwingNodePeer<N, NB, NM>
        implements ShapePeerMixin<N, NB, NM>, LayoutMeasurable {

    private final SwingPaintUpdater swingPaintUpdater = new SwingPaintUpdater();
    private final SwingStrokeUpdater swingStrokeUpdater = new SwingStrokeUpdater();
    private java.awt.Shape swingShape;
    private Bounds layoutBounds;

    SwingShapePeer(NB base) {
        super(base);
    }

    @Override
    public void updateFill(Paint fill) {
        updatePaint();
    }

    @Override
    public void updateSmooth(Boolean smooth) {
    }

    @Override
    public void updateStroke(Paint stroke) {
        updateStroke();
    }

    @Override
    public void updateStrokeType(StrokeType strokeType) {
        updateStroke();
    }

    @Override
    public void updateStrokeWidth(Double strokeWidth) {
        updateStroke();
    }

    @Override
    public void updateStrokeLineCap(StrokeLineCap strokeLineCap) {
        updateStroke();
    }

    @Override
    public void updateStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        updateStroke();
    }

    @Override
    public void updateStrokeMiterLimit(Double strokeMiterLimit) {
        updateStroke();
    }

    @Override
    public void updateStrokeDashOffset(Double strokeDashOffset) {
        updateStroke();
    }

    @Override
    public void updateStrokeDashArray(List<Double> dashArray) {
        updateStroke();
    }

    private void updatePaint() {
        swingPaintUpdater.updateFromShape(getNode());
    }

    private void updateStroke() {
        swingStrokeUpdater.updateFromShape(getNode());
    }

    protected void invalidateSwingShape() {
        swingShape = null;
        layoutBounds = null;
    }

    protected java.awt.Shape getOrCreateSwingShape(Graphics2D g) {
        if (swingShape == null) {
            swingShape = createSwingShape(g);
            layoutBounds = null;
        }
        return swingShape;
    }

    @Override
    public void paint(Graphics2D g) {
        paintSwingShape(g);
    }

    protected abstract java.awt.Shape createSwingShape(Graphics2D g);

    public void prepareCanvasContext(Graphics2D g) {
        super.prepareCanvasContext(g);
        N node = getNode();
        boolean smooth = node.isSmooth();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, smooth ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, smooth ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }

    void paintSwingShape(Graphics2D g) {
        Double width = null, height = null;
        if (swingStrokeUpdater.getSwingStroke() != null) {
            Rectangle2D bounds2D = getOrCreateSwingShape(g).getBounds2D();
            width = bounds2D.getWidth();
            height = bounds2D.getHeight();
        }
        paintSwingShape(width, height, g);
    }

    void paintSwingShape(Double width, Double height, Graphics2D g) {
        swingPaintUpdater.updateProportionalGradient(width, height);
        g.setPaint(swingPaintUpdater.getSwingPaint());
        g.fill(getOrCreateSwingShape(g));
        if (swingStrokeUpdater.getSwingStroke() != null) {
            g.setStroke(swingStrokeUpdater.getSwingStroke());
            swingStrokeUpdater.updateProportionalGradient(width, height);
            g.setPaint(swingStrokeUpdater.getSwingPaint());
            g.draw(swingShape);
        }
    }

    @Override
    public boolean containsPoint(Point2D point) {
        return swingShape != null && swingShape.contains(point.x, point.y);
    }

    @Override
    public Bounds getLayoutBounds() {
        if (layoutBounds == null)
            layoutBounds = createLayoutBounds();
        return layoutBounds;
    }

    protected Bounds createLayoutBounds() {
        if (swingShape == null)
            return new BoundingBox(0, 0, 0, 0);
        Rectangle bounds = swingShape.getBounds();
        return new BoundingBox(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    @Override
    public double minWidth(double height) {
        return getLayoutBounds().getWidth();
    }

    @Override
    public double maxWidth(double height) {
        return getLayoutBounds().getWidth();
    }

    @Override
    public double minHeight(double width) {
        return getLayoutBounds().getHeight();
    }

    @Override
    public double maxHeight(double width) {
        return getLayoutBounds().getHeight();
    }

    @Override
    public double prefWidth(double height) {
        return getLayoutBounds().getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getLayoutBounds().getHeight();
    }
}
