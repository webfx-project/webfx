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
public class SwingDrawing extends CanvasDrawingImpl<SwingNodeViewer<?, ?, ?>, Graphics2D> {

    SwingDrawing(SwingDrawingNode drawingNode) {
        super(drawingNode, SwingNodeViewerFactory.SINGLETON);
    }

    public JComponent getDrawingComponent() {
        return (JComponent) drawingNode.unwrapToNativeNode();
    }

    @Override
    public void requestCanvasRepaint() {
        getDrawingComponent().repaint();
    }

    @Override
    protected Graphics2D createCanvasContext(Graphics2D g) {
        return (Graphics2D) g.create();
    }

    @Override
    protected void disposeCanvasContext(Graphics2D canvasContext) {
        canvasContext.dispose();
    }

    @Override
    protected NodeViewer<Node> createNodeViewer(Node node) {
        NodeViewer<Node> nodeViewer = super.createNodeViewer(node);
        // SwingLayoutMeasurable components must be added to the Swing structure so they can report correct layout measures
        if (nodeViewer instanceof SwingLayoutMeasurable) {
            JComponent swingComponent = ((SwingLayoutMeasurable) nodeViewer).getSwingComponent();
            getDrawingComponent().add(swingComponent);
            // But they won't be displayed by Swing (they will be painted by the canvas) - See SwingDrawingNode.DrawingPanel.paintChildren()
        }
        return nodeViewer;
    }
}
