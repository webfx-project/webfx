package naga.providers.toolkit.swing.drawing.view;

import naga.providers.toolkit.swing.util.SwingBlendModes;
import naga.providers.toolkit.swing.util.SwingTransforms;
import naga.toolkit.drawing.shapes.BlendMode;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.Point2D;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.impl.DrawingImpl;
import naga.toolkit.drawing.spi.impl.canvas.CanvasDrawableView;
import naga.toolkit.drawing.spi.view.DrawableView;
import naga.toolkit.drawing.spi.view.base.DrawableViewBase;
import naga.toolkit.drawing.spi.view.base.DrawableViewImpl;
import naga.toolkit.drawing.spi.view.base.DrawableViewMixin;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;
import naga.toolkit.transform.Transform;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Collection;


/**
 * @author Bruno Salmon
 */
public abstract class SwingDrawableView
        <D extends Drawable, DV extends DrawableViewBase<D, DV, DM>, DM extends DrawableViewMixin<D, DV, DM>>

        extends DrawableViewImpl<D, DV, DM>
        implements CanvasDrawableView<D, Graphics2D> {

    private AffineTransform swingTransform;
    private Composite swingComposite;
    private DrawingImpl drawing;
    private SwingShapeView swingClipView;
    private Shape swingClip;

    SwingDrawableView(DV base) {
        super(base);
    }

    @Override
    public void bind(D drawable, DrawingRequester drawingRequester) {
        drawing = DrawingImpl.getThreadLocalDrawing();
        getDrawableViewBase().bind(drawable, drawingRequester);
    }

    @Override
    public void prepareCanvasContext(Graphics2D g) {
        if (swingTransform != null) {
            AffineTransform tx = new AffineTransform(g.getTransform());
            tx.concatenate(swingTransform);
            g.setTransform(tx);
        }
        if (swingComposite != null) {
/*
            Composite composite = g.getComposite();
            if (composite instanceof AlphaComposite) {
                AlphaComposite alphaComposite = (AlphaComposite) composite;
                g.setComposite(alphaComposite.derive(swingComposite.getAlpha() * alphaComposite.getAlpha()));
            } else
*/
                g.setComposite(swingComposite);
        }
        if (swingClipView != null) {
            if (swingClip == null)
                swingClip = swingClipView.createSwingShape(g);
            g.setClip(swingClip);
        }
    }

    @Override
    public void updateOnMouseClicked(UiEventHandler<? super MouseEvent> onMouseClicked) {
    }

    @Override
    public void updateVisible(Boolean visible) {
    }

    @Override
    public void updateOpacity(Double opacity) {
        updateComposite();
    }

    @Override
    public void updateBlendMode(BlendMode blendMode) {
        updateComposite();
    }

    private void updateComposite() {
        D drawable = getDrawable();
        swingComposite = SwingBlendModes.toComposite(drawable.getBlendMode(), drawable.getOpacity());
    }

    @Override
    public void updateClip(Drawable clip) {
        swingClip = null;
        swingClipView = null;
        if (clip != null) {
            if (drawing == null)
                drawing = DrawingImpl.getThreadLocalDrawing();
            DrawableView drawableView = drawing.getOrCreateAndBindDrawableView(clip);
            if (drawableView instanceof SwingShapeView)
                swingClipView = (SwingShapeView) drawableView;
        }
    }

    @Override
    public void updateLocalToParentTransforms(Collection<Transform> localToParentTransforms) {
        swingTransform = SwingTransforms.toSwingTransform(localToParentTransforms);
    }

    @Override
    public void paint(Graphics2D g) {
    }

    @Override
    public boolean containsPoint(Point2D point) {
        return false;
    }
}
