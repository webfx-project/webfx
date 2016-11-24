package naga.providers.toolkit.swing.drawing;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.swing.drawing.view.SwingEmbedGuiNodeView;
import naga.providers.toolkit.swing.events.SwingMouseEvent;
import naga.providers.toolkit.swing.nodes.SwingNode;
import naga.toolkit.drawing.scene.Node;
import naga.toolkit.drawing.geom.Point2D;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingMixin;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.drawing.spi.impl.canvas.PickResult;
import naga.toolkit.drawing.spi.view.NodeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author Bruno Salmon
 */
public class SwingDrawingNode extends SwingNode<SwingDrawingNode.DrawingPanel> implements DrawingNode, DrawingMixin {

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
        private long lastEmbedTargetEventTime;
        private static long maxSwingEmbedTargetAnimatedTransitionTime = 500;

        {
            heightProperty.addListener((observable, oldValue, newHeight) -> setPreferredSize(new Dimension(getWidth(), newHeight.intValue())));
            MouseAdapter canvasMouseAdapter = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    handleMouseEvent(e);
                }

                public void mousePressed(MouseEvent e) {
                    handleMouseEvent(e);
                }

                public void mouseReleased(MouseEvent e) {
                    handleMouseEvent(e);
                }

                //public void mouseEntered(MouseEvent e) { handleMouseEvent(e);}
                public void mouseExited(MouseEvent e) {
                    handleMouseEvent(e);
                }

                public void mouseWheelMoved(MouseWheelEvent e) {
                    handleMouseEvent(e);
                }

                public void mouseDragged(MouseEvent e) {
                    handleMouseEvent(e);
                }

                public void mouseMoved(MouseEvent e) {
                    handleMouseEvent(e);
                }

                private JComponent lastEmbedTarget;

                private void handleMouseEvent(MouseEvent e) {
                    PickResult pickResult = null;
                    JComponent embedTarget = null;
                    Point2D canvasPoint = Point2D.create(e.getX(), e.getY());
                    if (e.getID() != MouseEvent.MOUSE_EXITED) {
                        pickResult = drawing.pickNode(canvasPoint);
                        if (pickResult != null) {
                            Node node = pickResult.getNode();
                            if (e.getID() == MouseEvent.MOUSE_CLICKED && node.getOnMouseClicked() != null)
                                node.getOnMouseClicked().handle(new SwingMouseEvent(e));
                            NodeView nodeView = pickResult.getNodeView();
                            if (nodeView instanceof SwingEmbedGuiNodeView)
                                embedTarget = ((SwingEmbedGuiNodeView) nodeView).getEmbedSwingComponent();
                        }
                    }
                    if (embedTarget != lastEmbedTarget) {
                        if (lastEmbedTarget != null)
                            redispatchEvent(e, MouseEvent.MOUSE_EXITED, pickResult != null ? pickResult.getNodeLocalPoint() : canvasPoint, lastEmbedTarget);
                        if (embedTarget != null)
                            redispatchEvent(e, MouseEvent.MOUSE_ENTERED, pickResult.getNodeLocalPoint(), embedTarget);
                        lastEmbedTarget = embedTarget;
                    }
                    if (embedTarget != null)
                        redispatchEvent(e, e.getID(), pickResult.getNodeLocalPoint(), embedTarget);
                }

                private void redispatchEvent(MouseEvent e, int id, Point2D point, JComponent target) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().redispatchEvent(target,
                            new MouseEvent(target,
                                    id,
                                    e.getWhen(),
                                    e.getModifiers(),
                                    (int) point.getX(), (int) point.getY(),
                                    e.getClickCount(),
                                    e.isPopupTrigger(),
                                    e.getButton()));
                    lastEmbedTargetEventTime = e.getWhen();
                    drawing.requestCanvasRepaint();
                }
            };
            addMouseListener(canvasMouseAdapter);
            addMouseMotionListener(canvasMouseAdapter);
            addMouseWheelListener(canvasMouseAdapter);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawing.paintCanvas((Graphics2D) g);
            int width = getWidth();
            if (width != lastWidth)
                widthProperty.setValue((double) (lastWidth = width));
            if (System.currentTimeMillis() < lastEmbedTargetEventTime + maxSwingEmbedTargetAnimatedTransitionTime)
                repaint(); // requesting a repaint while last transition animation (triggered by mouse events on embed swing native components) may not be finished
        }
    }
}
