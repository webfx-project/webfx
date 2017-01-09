package naga.fx.spi.swing.fx.viewer;

import naga.commons.util.tuples.Unit;
import naga.fx.spi.swing.util.JGradientLabel;
import naga.fx.spi.swing.util.SwingBlendModes;
import naga.fx.spi.swing.util.SwingTransforms;
import naga.fx.event.EventHandler;
import naga.fx.geometry.VPos;
import naga.fx.scene.*;
import naga.fx.scene.effect.BlendMode;
import naga.fx.scene.effect.Effect;
import naga.fx.scene.input.MouseEvent;
import naga.fx.scene.text.Text;
import naga.fx.scene.text.TextAlignment;
import naga.fx.scene.transform.Transform;
import naga.fx.spi.Toolkit;
import naga.fx.spi.viewer.CanvasNodeViewer;
import naga.fx.spi.viewer.NodeViewer;
import naga.fx.spi.viewer.base.NodeViewerBase;
import naga.fx.spi.viewer.base.NodeViewerImpl;
import naga.fx.spi.viewer.base.NodeViewerMixin;
import naga.fx.properties.markers.HasHeightProperty;
import naga.fx.properties.markers.HasWidthProperty;
import naga.fx.properties.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.Collection;


/**
 * @author Bruno Salmon
 */
public abstract class SwingNodeViewer
        <N extends Node, NB extends NodeViewerBase<N, NB, NM>, NM extends NodeViewerMixin<N, NB, NM>>

        extends NodeViewerImpl<N, NB, NM>
        implements CanvasNodeViewer<N, Graphics2D> {

    private AffineTransform swingTransform;
    private Composite swingComposite;
    private SwingShapeViewer swingClipView;
    private Shape swingClip;

    SwingNodeViewer(NB base) {
        super(base);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        getNodeViewerBase().bind(node, sceneRequester);
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
        return node == null ? null : toSwingComponent(node, node.getScene(), null);
    }

    static JComponent toSwingComponent(Node node, Scene scene, TextAlignment textAlignment) {
        node.setScene(scene);
        // A difficulty to face with Swing: the requested component might be for cell rendering and needs to be ready to
        // for painting immediately (whereas Naga normally defers the property changes and layout pass to the next
        // animation frame). So we call getOrCreateAndBindNodeViewer() as if in an animation frame (to turn off deferring)
        Unit<NodeViewer> nodeViewerUnit = new Unit<>();
        naga.fx.spi.Toolkit.get().scheduler().runLikeAnimationFrame(() -> nodeViewerUnit.set(node.getOrCreateAndBindNodeViewer()));
        NodeViewer nodeViewer = nodeViewerUnit.get();
        if (nodeViewer instanceof SwingEmbedComponentViewer)
            return ((SwingEmbedComponentViewer) nodeViewer).getSwingComponent();
        CanvasScenePeer canvasScenePeer = (CanvasScenePeer) scene.impl_getPeer();
        if (nodeViewer instanceof SwingShapeViewer) {
            return new JGradientLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Insets insets = getInsets(null);
                    g.translate(insets.left, insets.top);
                    if (node instanceof Text) {
                        Text textNode = (Text) node;
                        textNode.setWrappingWidth((double) getWidth() - insets.right - insets.left);
                        textNode.setTextOrigin(VPos.TOP);
                        textNode.setTextAlignment(textAlignment);
                    }
                    canvasScenePeer.paintNode(node, g);
                }
            };
        }
        if (nodeViewer instanceof SwingLayoutViewer) {
            // The reason to ask a Swing component for a Layout viewer is probably to paint it inside a table cell
            return new JComponent() {
                @Override
                protected void paintChildren(Graphics g) {
                    fitNodeSizeToSwingComponentAndLayout(node, this);
                    canvasScenePeer.paintNode(node, g);
                }
            };
        }
        return null;
    }

    private static JComponent fitNodeSizeToSwingComponentAndLayout(Node node, JComponent component) {
        if (node instanceof HasWidthProperty)
            Properties.setIfNotBound(((HasWidthProperty) node).widthProperty(), (double) component.getWidth());
        if (node instanceof HasHeightProperty)
            Properties.setIfNotBound(((HasHeightProperty) node).heightProperty(), (double) component.getHeight());
        if (node instanceof Parent)
            Toolkit.get().scheduler().runLikeAnimationFrame(((Parent) node)::layout); // to ensure the layout is done immediately
        return component;
    }

    static ActionListener toActionListener(EventHandler<? super MouseEvent> mouseEventHandler) {
        return e -> mouseEventHandler.handle(toMouseEvent(e));
    }

    public static MouseEvent toMouseEvent(AWTEvent e) {
        return new MouseEvent(null, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null);
    }
}
