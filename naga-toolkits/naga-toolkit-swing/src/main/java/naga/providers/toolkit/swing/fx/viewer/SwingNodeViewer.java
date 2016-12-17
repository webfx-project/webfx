package naga.providers.toolkit.swing.fx.viewer;

import naga.commons.util.tuples.Unit;
import naga.providers.toolkit.swing.util.JGradientLabel;
import naga.providers.toolkit.swing.util.SwingBlendModes;
import naga.providers.toolkit.swing.util.SwingTransforms;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.scene.effect.BlendMode;
import naga.toolkit.fx.scene.effect.Effect;
import naga.toolkit.fx.scene.impl.NodeImpl;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.scene.transform.Transform;
import naga.toolkit.fx.spi.Drawing;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.impl.canvas.CanvasDrawingImpl;
import naga.toolkit.fx.spi.impl.canvas.CanvasNodeViewer;
import naga.toolkit.fx.spi.viewer.NodeViewer;
import naga.toolkit.fx.spi.viewer.base.NodeViewerBase;
import naga.toolkit.fx.spi.viewer.base.NodeViewerImpl;
import naga.toolkit.fx.spi.viewer.base.NodeViewerMixin;
import naga.toolkit.properties.markers.HasHeightProperty;
import naga.toolkit.properties.markers.HasWidthProperty;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;
import naga.toolkit.util.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Collection;


/**
 * @author Bruno Salmon
 */
public abstract class SwingNodeViewer
        <N extends Node, NV extends NodeViewerBase<N, NV, NM>, NM extends NodeViewerMixin<N, NV, NM>>

        extends NodeViewerImpl<N, NV, NM>
        implements CanvasNodeViewer<N, Graphics2D> {

    private AffineTransform swingTransform;
    private Composite swingComposite;
    private SwingShapeViewer swingClipView;
    private Shape swingClip;

    SwingNodeViewer(NV base) {
        super(base);
    }

    @Override
    public void bind(N node, DrawingRequester drawingRequester) {
        getNodeViewerBase().bind(node, drawingRequester);
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
            NodeViewer nodeViewer = clip.getOrCreateAndBindNodeViewer();
            if (nodeViewer instanceof SwingShapeViewer)
                swingClipView = (SwingShapeViewer) nodeViewer;
        }
    }

    @Override
    public void updateLocalToParentTransforms(Collection<Transform> localToParentTransforms) {
        swingTransform = SwingTransforms.toSwingTransform(localToParentTransforms);
    }

    static JComponent toSwingComponent(Node node) {
        return node == null ? null : toSwingComponent(node, node.getDrawing(), null);
    }

    static JComponent toSwingComponent(Node node, Drawing drawing, TextAlignment textAlignment) {
        NodeImpl renderedNode = (NodeImpl) node;
        CanvasDrawingImpl canvasDrawing = (CanvasDrawingImpl) drawing;
        renderedNode.setDrawing(canvasDrawing);
        // A difficulty to face with Swing: the requested component might be for cell rendering and needs to be ready to
        // for painting immediately (whereas Naga normally defers the property changes and layout pass to the next
        // animation frame). So we call getOrCreateAndBindNodeViewer() as if in an animation frame (to turn off deferring)
        Unit<NodeViewer> nodeViewerUnit = new Unit<>();
        naga.toolkit.spi.Toolkit.get().scheduler().runLikeAnimationFrame(() -> nodeViewerUnit.set(renderedNode.getOrCreateAndBindNodeViewer()));
        NodeViewer nodeViewer = nodeViewerUnit.get();
        if (nodeViewer instanceof SwingEmbedComponentViewer)
            return ((SwingEmbedComponentViewer) nodeViewer).getSwingComponent();
        if (nodeViewer instanceof SwingShapeViewer) {
            return new JGradientLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Insets insets = getInsets(null);
                    g.translate(insets.left, insets.top);
                    if (renderedNode instanceof Text) {
                        Text textNode = (Text) renderedNode;
                        textNode.setWrappingWidth((double) getWidth() - insets.right - insets.left);
                        textNode.setTextOrigin(VPos.TOP);
                        textNode.setTextAlignment(textAlignment);
                    }
                    canvasDrawing.paintNode(renderedNode, g);
                }
            };
        }
        if (nodeViewer instanceof SwingLayoutViewer) {
            // The reason to ask a Swing component for a Layout viewer is probably to paint it inside a table cell
            return new JComponent() {
                @Override
                protected void paintChildren(Graphics g) {
                    fitNodeSizeToSwingComponentAndLayout(node, this);
                    canvasDrawing.paintNode(node, g);
                }
            };
        }
        return null;
    }

    private static JComponent fitNodeSizeToSwingComponentAndLayout(Node node, JComponent component) {
        if (node instanceof HasWidthProperty)
            Properties.safeSetProperty(((HasWidthProperty) node).widthProperty(), (double) component.getWidth());
        if (node instanceof HasHeightProperty)
            Properties.safeSetProperty(((HasHeightProperty) node).heightProperty(), (double) component.getHeight());
        if (node instanceof Parent)
            naga.toolkit.spi.Toolkit.get().scheduler().runLikeAnimationFrame(((Parent) node)::layout); // to ensure the layout is done immediately
        return component;
    }
}
