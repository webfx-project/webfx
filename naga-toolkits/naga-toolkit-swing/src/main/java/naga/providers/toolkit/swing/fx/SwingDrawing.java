package naga.providers.toolkit.swing.fx;

import javafx.beans.property.Property;
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

    private boolean canvasRepaint;
    @Override
    public void requestCanvasRepaint() {
        canvasRepaint = true;
    }

    @Override
    public void pulse() {
        updateWidthAndHeight(); // The width update may cause a layout request (good to check before calling pulse)
        super.pulse(); // This call to pulse() will consider changes in the scene graph and do the layout pass
        if (canvasRepaint) {
            canvasRepaint = false;
            getDrawingComponent().repaint();
        }
    }

    private int lastWidth;
    private int lastHeight;

    private void updateWidthAndHeight() {
        JComponent c = getDrawingComponent();
        // Binding the width property to the actual component width
        boolean sizeChanged = false;
        Property<Double> widthProperty = drawingNode.widthProperty();
        if (true || widthProperty.isBound()) {
            int width = widthProperty.getValue().intValue();
            if (width != lastWidth) {
                c.setSize(lastWidth = width, lastHeight);
                sizeChanged = true;
            }
        } else {
            int width = c.getWidth();
            if (width != lastWidth)
                widthProperty.setValue((double) (lastWidth = width));
        }
        // Binding the component height to the height property
        Property<Double> heightProperty = drawingNode.heightProperty();
        if (true || heightProperty.isBound()) {
            int height = heightProperty.getValue().intValue();
            if (height != lastHeight) {
                c.setSize(lastWidth, lastHeight = height);
                sizeChanged = true;
            }
        } else {
            int height = c.getHeight();
            if (height != lastHeight)
                heightProperty.setValue((double) (lastHeight = height));
        }
        if (sizeChanged) {
            Dimension size = c.getSize();
            c.setMinimumSize(size);
            c.setPreferredSize(size);
            c.setMaximumSize(size);
            //getParent().doLayout();
        }
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
