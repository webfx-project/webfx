package naga.providers.toolkit.swing.drawing;

import naga.providers.toolkit.swing.drawing.view.SwingNodeView;
import naga.toolkit.drawing.spi.impl.canvas.CanvasDrawingImpl;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
class SwingDrawing extends CanvasDrawingImpl<SwingNodeView<?, ?, ?>, Graphics2D, SwingGraphicState> {

    SwingDrawing(SwingDrawingNode drawingNode) {
        super(drawingNode, SwingNodeViewFactory.SINGLETON);
    }

    @Override
    public void requestCanvasRepaint() {
        ((Component) drawingNode.unwrapToNativeNode()).repaint();
    }

    @Override
    protected SwingGraphicState captureGraphicState(Graphics2D g) {
        return new SwingGraphicState(g.getTransform(), g.getComposite(), g.getClip());
    }

    @Override
    protected void restoreGraphicState(SwingGraphicState graphicState, Graphics2D g) {
        g.setTransform(graphicState.getTransform());
        g.setComposite(graphicState.getComposite());
        g.setClip(graphicState.getClip());
    }
}
