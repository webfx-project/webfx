package naga.fx.spi.swing.peer;

import naga.commons.util.tuples.Unit;
import naga.fx.spi.peer.CanvasNodePeer;
import naga.fx.spi.peer.NodePeer;
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
import naga.fx.spi.peer.base.NodePeerBase;
import naga.fx.spi.peer.base.NodePeerImpl;
import naga.fx.spi.peer.base.NodePeerMixin;
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
public abstract class SwingNodePeer
        <N extends Node, NB extends NodePeerBase<N, NB, NM>, NM extends NodePeerMixin<N, NB, NM>>

        extends NodePeerImpl<N, NB, NM>
        implements CanvasNodePeer<N, Graphics2D> {

    private AffineTransform swingTransform;
    private Composite swingComposite;
    private SwingShapePeer swingClipView;
    private Shape swingClip;

    SwingNodePeer(NB base) {
        super(base);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        getNodePeerBase().bind(node, sceneRequester);
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
    public void updateDisabled(Boolean disabled) {
        if (this instanceof SwingEmbedComponentPeer)
            ((SwingEmbedComponentPeer) this).getSwingComponent().setEnabled(!disabled);
    }

    @Override
    public void requestFocus() {
        if (this instanceof SwingEmbedComponentPeer)
            ((SwingEmbedComponentPeer) this).getSwingComponent().requestFocus();
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
            NodePeer nodePeer = clip.getOrCreateAndBindNodePeer();
            if (nodePeer instanceof SwingShapePeer)
                swingClipView = (SwingShapePeer) nodePeer;
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
        // animation frame). So we call getOrCreateAndBindNodePeer() as if in an animation frame (to turn off deferring)
        Unit<NodePeer> nodePeerUnit = new Unit<>();
        naga.fx.spi.Toolkit.get().scheduler().runLikeAnimationFrame(() -> nodePeerUnit.set(node.getOrCreateAndBindNodePeer()));
        NodePeer nodePeer = nodePeerUnit.get();
        if (nodePeer instanceof SwingEmbedComponentPeer)
            return ((SwingEmbedComponentPeer) nodePeer).getSwingComponent();
        CanvasScenePeer canvasScenePeer = (CanvasScenePeer) scene.impl_getPeer();
        if (nodePeer instanceof SwingShapePeer) {
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
        if (nodePeer instanceof SwingLayoutPeer) {
            // The reason to ask a Swing component for a Layout peer is probably to paint it inside a table cell
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
