package naga.providers.toolkit.swing.fx.view;

import naga.providers.toolkit.swing.util.SwingBlendModes;
import naga.providers.toolkit.swing.util.SwingTransforms;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.effect.BlendMode;
import naga.toolkit.fx.scene.effect.Effect;
import naga.toolkit.fx.scene.transform.Transform;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.impl.canvas.CanvasNodeView;
import naga.toolkit.fx.spi.view.NodeView;
import naga.toolkit.fx.spi.view.base.NodeViewBase;
import naga.toolkit.fx.spi.view.base.NodeViewImpl;
import naga.toolkit.fx.spi.view.base.NodeViewMixin;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Collection;


/**
 * @author Bruno Salmon
 */
public abstract class SwingNodeView
        <N extends Node, NV extends NodeViewBase<N, NV, NM>, NM extends NodeViewMixin<N, NV, NM>>

        extends NodeViewImpl<N, NV, NM>
        implements CanvasNodeView<N, Graphics2D> {

    private AffineTransform swingTransform;
    private Composite swingComposite;
    private SwingShapeView swingClipView;
    private Shape swingClip;

    SwingNodeView(NV base) {
        super(base);
    }

    @Override
    public void bind(N node, DrawingRequester drawingRequester) {
        getNodeViewBase().bind(node, drawingRequester);
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
    public void updateMouseTransparent(Boolean mouseTransparent) {
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

    @Override
    public void updateEffect(Effect effect) {
        // Not yet implemented
    }

    private void updateComposite() {
        N node = getNode();
        swingComposite = SwingBlendModes.toComposite(node.getBlendMode(), node.getOpacity());
    }

    @Override
    public void updateClip(Node clip) {
        swingClip = null;
        swingClipView = null;
        if (clip != null) {
            NodeView nodeView = clip.getOrCreateAndBindNodeView();
            if (nodeView instanceof SwingShapeView)
                swingClipView = (SwingShapeView) nodeView;
        }
    }

    @Override
    public void updateLocalToParentTransforms(Collection<Transform> localToParentTransforms) {
        swingTransform = SwingTransforms.toSwingTransform(localToParentTransforms);
    }

}
