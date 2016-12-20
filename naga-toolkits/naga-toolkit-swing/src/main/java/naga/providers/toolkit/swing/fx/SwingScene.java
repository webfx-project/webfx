package naga.providers.toolkit.swing.fx;

import javafx.beans.property.Property;
import naga.providers.toolkit.swing.fx.viewer.SwingEmbedComponentViewer;
import naga.providers.toolkit.swing.fx.viewer.SwingLayoutMeasurable;
import naga.providers.toolkit.swing.fx.viewer.SwingNodeViewer;
import naga.providers.toolkit.swing.util.StyleUtil;
import naga.toolkit.fx.geom.Point2D;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.CanvasScene;
import naga.toolkit.fx.scene.PickResult;
import naga.toolkit.fx.spi.viewer.NodeViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author Bruno Salmon
 */
public class SwingScene extends CanvasScene<SwingNodeViewer<?, ?, ?>, Graphics2D> {

    private final CanvasPanel canvasPanel = new CanvasPanel();
    private boolean canvasRepaint;

    public SwingScene() {
        super(SwingNodeViewerFactory.SINGLETON);
    }

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
            canvasPanel.repaint();
        }
    }

    private int lastWidth;
    private int lastHeight;

    private void updateWidthAndHeight() {
        // Binding the width property to the actual component width
        boolean sizeChanged = false;
        Property<Double> widthProperty = widthProperty();
        if (true || widthProperty.isBound()) {
            int width = widthProperty.getValue().intValue();
            if (width != lastWidth) {
                canvasPanel.setSize(lastWidth = width, lastHeight);
                sizeChanged = true;
            }
        } else {
            int width = canvasPanel.getWidth();
            if (width != lastWidth)
                widthProperty.setValue((double) (lastWidth = width));
        }
        // Binding the component height to the height property
        Property<Double> heightProperty = heightProperty();
        if (true || heightProperty.isBound()) {
            int height = heightProperty.getValue().intValue();
            if (height != lastHeight) {
                canvasPanel.setSize(lastWidth, lastHeight = height);
                sizeChanged = true;
            }
        } else {
            int height = canvasPanel.getHeight();
            if (height != lastHeight)
                heightProperty.setValue((double) (lastHeight = height));
        }
        if (sizeChanged) {
            Dimension size = canvasPanel.getSize();
            canvasPanel.setMinimumSize(size);
            canvasPanel.setPreferredSize(size);
            canvasPanel.setMaximumSize(size);
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
            canvasPanel.add(swingComponent);
            // But they won't be displayed by Swing (they will be painted by the canvas) - See CanvasPanel.paintChildren()
        }
        return nodeViewer;
    }

    private static long maxSwingEmbedTargetAnimatedTransitionTime = 500;

    class CanvasPanel extends JPanel {

        private long lastEmbedTargetEventTime;

        CanvasPanel() {
            super(null); // No layout needed is it is done by NagaFx
            setFont(StyleUtil.getFont(false, false));
            CanvasPanel.EventListener eventListener = new CanvasPanel.EventListener();
            addMouseListener(eventListener);
            addMouseMotionListener(eventListener);
            addMouseWheelListener(eventListener);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Erasing the graphic buffer
            paintCanvas((Graphics2D) g); // Painting all nodes from the scene graph
            if (getComponentCount() > 0 || System.currentTimeMillis() < lastEmbedTargetEventTime + maxSwingEmbedTargetAnimatedTransitionTime)
                requestCanvasRepaint(); // requesting a repaint while last transition animation (triggered by mouse events on embed swing native components) may not be finished
        }

        @Override
        protected void paintChildren(Graphics g) {
            // CanvasPanel was initially designed to have no children because all the nodes tree is already painted by
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

        private class EventListener extends MouseAdapter {
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

            private Component lastEmbedTarget;

            private void handleMouseEvent(MouseEvent e) {
                PickResult pickResult = null;
                Component embedTarget = null;
                Point2D canvasPoint = new Point2D((double) e.getX(), (double) e.getY());
                int eventId = e.getID();
                if (eventId != MouseEvent.MOUSE_EXITED) {
                    pickResult = pickNode(canvasPoint);
                    if (pickResult != null) {
                        Node node = pickResult.getNode();
                        if (eventId == MouseEvent.MOUSE_CLICKED && node.getOnMouseClicked() != null)
                            node.getOnMouseClicked().handle(SwingNodeViewer.toMouseEvent(e));
                        NodeViewer nodeViewer = pickResult.getNodeViewer();
                        if (nodeViewer instanceof SwingEmbedComponentViewer)
                            embedTarget = ((SwingEmbedComponentViewer) nodeViewer).getSwingComponent();
                    }
                }
                int x, y;
                if (embedTarget != null) {
                    Point2D nodeLocalPoint = pickResult.getNodeLocalPoint();
                    x = (int) nodeLocalPoint.getX();
                    y = (int) nodeLocalPoint.getY();
                    while (true) {
                        Component childTarget = embedTarget.getComponentAt(x, y);
                        if (childTarget == null || childTarget == embedTarget)
                            break;
                        x -= childTarget.getX();
                        y -= childTarget.getY();
                        embedTarget = childTarget;
                    }
                } else {
                    x = (int) canvasPoint.getX();
                    y = (int) canvasPoint.getY();
                }
                if (embedTarget != lastEmbedTarget) {
                    if (lastEmbedTarget != null)
                        redispatchEvent(e, MouseEvent.MOUSE_EXITED, x, y, lastEmbedTarget);
                    if (embedTarget != null)
                        redispatchEvent(e, MouseEvent.MOUSE_ENTERED, x, y, embedTarget);
                    lastEmbedTarget = embedTarget;
                }
                if (embedTarget != null) {
                    redispatchEvent(e, eventId, x, y, embedTarget);
                    setCursor(embedTarget.getCursor());
                }
            }

            private void redispatchEvent(AWTEvent event, int eventId, int x, int y, Component target) {
                if (event instanceof MouseWheelEvent) {
                    MouseWheelEvent e = (MouseWheelEvent) event;
                    event = new MouseWheelEvent(target,
                            eventId,
                            lastEmbedTargetEventTime = e.getWhen(),
                            e.getModifiers(),
                            x, y,
                            e.getClickCount(),
                            e.isPopupTrigger(),
                            e.getScrollType(),
                            e.getScrollAmount(),
                            e.getWheelRotation());
                } else if (event instanceof MouseEvent) {
                    MouseEvent e = (MouseEvent) event;
                    event = new MouseEvent(target,
                            eventId,
                            lastEmbedTargetEventTime = e.getWhen(),
                            e.getModifiers(),
                            x, y,
                            e.getClickCount(),
                            e.isPopupTrigger(),
                            e.getButton());
                } else {
                    System.out.println("Unknown AWT event in SwingScene");
                }
                KeyboardFocusManager.getCurrentKeyboardFocusManager().redispatchEvent(target, event);
                requestCanvasRepaint();
            }
        }
    }

    public JComponent getSceneComponent() {
        return canvasPanel;
    }

}
