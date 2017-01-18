package naga.fx.spi.swing.peer;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.CanvasScenePeer;
import emul.javafx.scene.Node;
import emul.javafx.scene.PickResult;
import emul.javafx.scene.Scene;
import naga.fx.spi.peer.NodePeer;
import naga.fx.spi.swing.SwingNodePeerFactory;
import naga.fx.spi.swing.util.StyleUtil;
import emul.com.sun.javafx.geom.Point2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author Bruno Salmon
 */
public class SwingScenePeer extends CanvasScenePeer<SwingNodePeer<?, ?, ?>, Graphics2D> {

    private final CanvasPanel canvasPanel = new CanvasPanel();
    private boolean canvasRepaint;

    public SwingScenePeer(Scene scene) {
        super(scene, SwingNodePeerFactory.SINGLETON);
    }

    @Override
    public void requestCanvasRepaint() {
        canvasRepaint = true;
    }

    @Override
    public void onBeforePulse() {
        updateWidthAndHeight(); // The width update may cause a layout request (good to check before calling pulse)
    }

    @Override
    public void onAfterPulse() {
        if (canvasRepaint) {
            canvasRepaint = false;
            canvasPanel.repaint();
        }
    }

    void changedWindowSize(float width, float height) {
        if (listener != null)
            listener.changedSize(width, height);
    }

    private int lastWidth;
    private int lastHeight;

    private void updateWidthAndHeight() {
        // Binding the width property to the actual component width
        boolean sizeChanged = false;
        Property<Double> widthProperty = scene.widthProperty();
        int width = widthProperty.getValue().intValue();
        if (width != lastWidth) {
            canvasPanel.setSize(lastWidth = width, lastHeight);
            sizeChanged = true;
        }
        // Binding the component height to the height property
        Property<Double> heightProperty = scene.heightProperty();
        int height = heightProperty.getValue().intValue();
        if (height != lastHeight) {
            canvasPanel.setSize(lastWidth, lastHeight = height);
            sizeChanged = true;
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
    public void onNodePeerCreated(NodePeer<Node> nodePeer) {
        // SwingLayoutMeasurable components must be added to the Swing structure so they can report correct layout measures
        if (nodePeer instanceof SwingLayoutMeasurable) {
            JComponent swingComponent = ((SwingLayoutMeasurable) nodePeer).getSwingComponent();
            canvasPanel.add(swingComponent);
            // But they won't be displayed by Swing (they will be painted by the canvas) - See CanvasPanel.paintChildren()
        }
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
                Point2D canvasPoint = new Point2D(e.getX(), e.getY());
                int eventId = e.getID();
                if (eventId != MouseEvent.MOUSE_EXITED) {
                    pickResult = pickNode(canvasPoint);
                    if (pickResult != null) {
                        Node node = pickResult.getNode();
                        if (eventId == MouseEvent.MOUSE_CLICKED && node.getOnMouseClicked() != null)
                            node.getOnMouseClicked().handle(SwingNodePeer.toMouseEvent(e));
                        NodePeer nodePeer = pickResult.getNodePeer();
                        if (nodePeer instanceof SwingEmbedComponentPeer)
                            embedTarget = ((SwingEmbedComponentPeer) nodePeer).getSwingComponent();
                    }
                }
                int x, y;
                if (embedTarget != null) {
                    Point2D nodeLocalPoint = pickResult.getNodeLocalPoint();
                    x = (int) nodeLocalPoint.x;
                    y = (int) nodeLocalPoint.y;
                    while (true) {
                        Component childTarget = embedTarget.getComponentAt(x, y);
                        if (childTarget == null || childTarget == embedTarget)
                            break;
                        x -= childTarget.getX();
                        y -= childTarget.getY();
                        embedTarget = childTarget;
                    }
                } else {
                    x = (int) canvasPoint.x;
                    y = (int) canvasPoint.y;
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
                    System.out.println("Unknown AWT event in SwingScenePeer");
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
