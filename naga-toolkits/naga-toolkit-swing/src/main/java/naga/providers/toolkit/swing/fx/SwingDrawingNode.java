package naga.providers.toolkit.swing.fx;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.swing.fx.view.SwingEmbedComponentView;
import naga.providers.toolkit.swing.events.SwingMouseEvent;
import naga.providers.toolkit.swing.nodes.SwingNode;
import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.Drawing;
import naga.toolkit.fx.spi.DrawingMixin;
import naga.toolkit.fx.spi.DrawingNode;
import naga.toolkit.fx.spi.impl.canvas.PickResult;
import naga.toolkit.fx.spi.view.NodeView;

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
        private int lastHeight;
        private final Property<Double> widthProperty = new SimpleObjectProperty<>(0d);
        private final Property<Double> heightProperty = new SimpleObjectProperty<>(0d);
        private long lastEmbedTargetEventTime;
        private static long maxSwingEmbedTargetAnimatedTransitionTime = 500;

        DrawingPanel() {
            super(null); // No layout needed is it is done by NagaFx
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
                            if (nodeView instanceof SwingEmbedComponentView)
                                embedTarget = ((SwingEmbedComponentView) nodeView).getEmbedSwingComponent();
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
            if (drawing.isPulseRunning()) { // Waiting the drawing is ready and pulse is scheduled
                updateWidthAndHeight(); // The width update may cause a layout request (good to check before calling pulse)
                drawing.pulse(); // This call to pulse() will consider changes in the scene graph and do the layout pass
                updateWidthAndHeight(); // In case the drawing height has been updated during the pulse
                super.paintComponent(g); // Erasing the graphic buffer
                drawing.paintCanvas((Graphics2D) g); // Painting all nodes from the scene graph
                if (getComponentCount() > 0 || System.currentTimeMillis() < lastEmbedTargetEventTime + maxSwingEmbedTargetAnimatedTransitionTime)
                    repaint(); // requesting a repaint while last transition animation (triggered by mouse events on embed swing native components) may not be finished
            }
        }

        private void updateWidthAndHeight() {
            // Binding the width property to the actual component width
            int width = getWidth();
            if (width != lastWidth)
                widthProperty.setValue((double) (lastWidth = width));
            // Binding the component height to the height property
            int height = heightProperty.getValue().intValue();
            if (height != lastHeight) {
                setSize(width, lastHeight = height);
                Dimension size = getSize();
                setMinimumSize(size);
                setPreferredSize(size);
                setMaximumSize(size);
                getParent().doLayout();
            }
        }

        @Override
        protected void paintChildren(Graphics g) {
            // DrawingPanel was initially designed to have no children because all the nodes tree is already painted by
            // the canvas in paintComponent() (this includes embed Swing components which can in this way be transformed
            // (ex: rotated), overlapped, etc...) as opposed to the standard Swing paint process.
            // However some SwingLayoutMeasurable components may have been added to the Swing structure (which is
            // necessary so they can report correct layout measures - see createNodeView.createNodeView()).
            // So we override this method to avoid they are painted again in an incorrect way (not transformed, on top
            // of nodes that should overlap them, etc...).
            // Also to avoid any unwelcome direct mouse interaction with such children (which may result in painting
            // them), we move them out of the visible part.
            for (int i = 0, n = getComponentCount(); i < n; i++)
                getComponent(i).setLocation(Integer.MIN_VALUE, Integer.MIN_VALUE);
        }
    }
}
