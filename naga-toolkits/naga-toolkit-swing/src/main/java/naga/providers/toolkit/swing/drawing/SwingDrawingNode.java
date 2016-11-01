package naga.providers.toolkit.swing.drawing;

import naga.providers.toolkit.swing.drawing.view.SwingDrawableView;
import naga.providers.toolkit.swing.nodes.SwingNode;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingMixin;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.drawing.spi.impl.DrawingImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class SwingDrawingNode extends SwingNode<SwingDrawingNode.DrawingPanel> implements DrawingNode<SwingDrawingNode.DrawingPanel>, DrawingMixin {

    public SwingDrawingNode() {
        this(new DrawingPanel());
    }

    private SwingDrawingNode(DrawingPanel node) {
        super(node);
    }

    @Override
    public Drawing getDrawing() {
        return node.drawing;
    }

    static class DrawingPanel extends JPanel {

        private final DrawingImpl drawing = new DrawingImpl(SwingDrawableViewFactory.SINGLETON) {
            @Override
            protected void onDrawableRepaintRequested(Drawable drawable) {
                repaint();
            }
        };


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            paintDrawables(drawing.getDrawableChildren(), (Graphics2D) g);
        }

        private void paintDrawables(Collection<Drawable> drawables, Graphics2D g) {
            AffineTransform parentTransform = g.getTransform();
            for (Drawable drawable : drawables) {
                g.setTransform(parentTransform); // Resetting the graphics to the initial transform (ex: x,y to 0,0)
                paintDrawable(drawable, g);
            }
        }

        private void paintDrawable(Drawable drawable, Graphics2D g) {
            SwingDrawableView drawableView = (SwingDrawableView) drawing.getOrCreateAndBindDrawableView(drawable);
            drawableView.paintShape(g);
            if (drawable instanceof DrawableParent)
                paintDrawables(((DrawableParent) drawable).getDrawableChildren(), g);
        }
    }
}
