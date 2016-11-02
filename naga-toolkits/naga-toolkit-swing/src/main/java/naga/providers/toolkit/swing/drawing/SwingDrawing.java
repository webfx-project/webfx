package naga.providers.toolkit.swing.drawing;

import naga.providers.toolkit.swing.drawing.view.SwingDrawableView;
import naga.toolkit.drawing.spi.impl.CanvasDrawingImpl;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * @author Bruno Salmon
 */
class SwingDrawing extends CanvasDrawingImpl<SwingDrawableView, Graphics2D, AffineTransform> {

    SwingDrawing(SwingDrawingNode drawingNode) {
        super(drawingNode, SwingDrawableViewFactory.SINGLETON);
    }

    @Override
    protected void requestCanvasRepaint() {
        ((Component) drawingNode.unwrapToNativeNode()).repaint();
    }

    @Override
    protected AffineTransform getCanvasTransform(Graphics2D g) {
        return g.getTransform();
    }

    @Override
    protected void setCanvasTransform(AffineTransform transform, Graphics2D g) {
        g.setTransform(transform);
    }

    @Override
    protected void paintDrawableView(SwingDrawableView drawableView, Graphics2D g) {
        drawableView.paint(g);
    }
}
