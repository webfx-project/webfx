package naga.providers.toolkit.swing.drawing;

import naga.providers.toolkit.swing.nodes.SwingNode;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingMixin;
import naga.toolkit.drawing.spi.DrawingNode;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingDrawingNode extends SwingNode<SwingDrawingNode.DrawingPanel> implements DrawingNode<SwingDrawingNode.DrawingPanel>, DrawingMixin {

    public SwingDrawingNode() {
        this(new DrawingPanel());
    }

    private SwingDrawingNode(DrawingPanel node) {
        super(node);
        node.drawing = new SwingDrawing(this);
    }

    @Override
    public Drawing getDrawing() {
        return node.drawing;
    }

    static class DrawingPanel extends JPanel {

        private SwingDrawing drawing;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawing.paintCanvas((Graphics2D) g);
        }
    }
}
