package naga.providers.toolkit.swing.drawing;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.swing.events.SwingMouseEvent;
import naga.providers.toolkit.swing.nodes.SwingNode;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.Point2D;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingMixin;
import naga.toolkit.drawing.spi.DrawingNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    @Override
    public Property<Double> widthProperty() {
        return node.widthProperty;
    }

    @Override
    public Property<Double> heightProperty() {
        return node.heightProperty;
    }

    static class DrawingPanel extends JPanel {

        private SwingDrawing drawing;
        private int lastWidth;
        private final Property<Double> widthProperty = new SimpleObjectProperty<>(0d);
        private final Property<Double> heightProperty = new SimpleObjectProperty<>(0d);

        {
            heightProperty.addListener((observable, oldValue, newHeight) -> setPreferredSize(new Dimension(getWidth(), newHeight.intValue())));
            addMouseListener(new MouseAdapter() {
                // Using mouseReleased() instead of mouseClicked() because for any reason the later miss some clicks (when the mouse is moving at the same time)
                @Override
                public void mouseReleased(MouseEvent e) {
                    Drawable drawable = drawing.pickDrawable(new Point2D(e.getX(), e.getY()));
                    if (drawable != null) {
                        if (drawable.getOnMouseClicked() != null)
                            drawable.getOnMouseClicked().handle(new SwingMouseEvent(e));
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawing.paintCanvas((Graphics2D) g);
            int width = getWidth();
            if (width != lastWidth)
                widthProperty.setValue((double) (lastWidth = width));
        }
    }
}
