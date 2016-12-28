package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.geometry.BoundingBox;
import naga.toolkit.fx.geometry.Bounds;
import naga.toolkit.fx.scene.LayoutMeasurable;
import naga.toolkit.fx.scene.paint.Paint;
import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.fx.scene.shape.Shape;
import naga.toolkit.fx.scene.shape.StrokeLineCap;
import naga.toolkit.fx.scene.shape.StrokeLineJoin;
import naga.toolkit.fx.scene.shape.StrokeType;
import naga.toolkit.fx.spi.viewer.base.ShapeViewerBase;
import naga.toolkit.fx.spi.viewer.base.ShapeViewerMixin;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * @author Bruno Salmon
 */
abstract class SwingShapeViewer
        <N extends Shape, NB extends ShapeViewerBase<N, NB, NM>, NM extends ShapeViewerMixin<N, NB, NM>>

        extends SwingNodeViewer<N, NB, NM>
        implements ShapeViewerMixin<N, NB, NM>, LayoutMeasurable {

    private final SwingPaintUpdater swingPaintUpdater = new SwingPaintUpdater();
    private final SwingStrokeUpdater swingStrokeUpdater = new SwingStrokeUpdater();
    private java.awt.Shape swingShape;
    private Bounds layoutBounds;

    SwingShapeViewer(NB base) {
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
        return swingShape != null && swingShape.contains(point.getX(), point.getY());
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
