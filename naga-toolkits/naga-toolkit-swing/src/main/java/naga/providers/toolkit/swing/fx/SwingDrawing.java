package naga.providers.toolkit.swing.fx;

import naga.providers.toolkit.swing.fx.viewer.SwingLayoutMeasurable;
import naga.providers.toolkit.swing.fx.viewer.SwingNodeViewer;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.impl.canvas.CanvasDrawingImpl;
import naga.toolkit.fx.spi.viewer.NodeViewer;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
class SwingDrawing extends CanvasDrawingImpl<SwingNodeViewer<?, ?, ?>, Graphics2D, SwingGraphicState> {

    SwingDrawing(SwingDrawingNode drawingNode) {
        super(drawingNode, SwingNodeViewerFactory.SINGLETON);
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

    @Override
    protected NodeViewer<Node> createNodeViewer(Node node) {
        NodeViewer<Node> nodeViewer = super.createNodeViewer(node);
        // SwingLayoutMeasurable components must be added to the Swing structure so they can report correct layout measures
        if (nodeViewer instanceof SwingLayoutMeasurable) {
            JComponent swingComponent = ((SwingLayoutMeasurable) nodeViewer).getSwingComponent();
            ((JComponent) drawingNode.unwrapToNativeNode()).add(swingComponent);
            // But they won't be displayed by Swing (they will be painted by the canvas) - See SwingDrawingNode.DrawingPanel.paintChildren()
        }
        return nodeViewer;
    }
}
