package naga.providers.toolkit.swing.drawing;

import naga.providers.toolkit.swing.drawing.view.SwingDrawableView;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.impl.canvas.CanvasDrawingImpl;
import naga.toolkit.drawing.spi.view.DrawableView;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
class SwingDrawing extends CanvasDrawingImpl<SwingDrawableView<?, ?, ?>, Graphics2D, SwingGraphicState> {

    SwingDrawing(SwingDrawingNode drawingNode) {
        super(drawingNode, SwingDrawableViewFactory.SINGLETON);
    }

    @Override
    public void requestCanvasRepaint() {
        ((Component) drawingNode.unwrapToNativeNode()).repaint();
    }

    @Override
    protected SwingGraphicState captureGraphicState(Graphics2D g) {
        return new SwingGraphicState(g.getTransform(), g.getComposite());
    }

    @Override
    protected void restoreGraphicState(SwingGraphicState graphicState, Graphics2D g) {
        g.setTransform(graphicState.getTransform());
        g.setComposite(graphicState.getComposite());
    }

    @Override
    public DrawableView getOrCreateAndBindDrawableView(Drawable drawable) {
        return super.getOrCreateAndBindDrawableView(drawable);
    }
}
