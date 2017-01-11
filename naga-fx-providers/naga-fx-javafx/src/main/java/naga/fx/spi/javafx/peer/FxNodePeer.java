package naga.fx.spi.javafx.peer;

import naga.fx.event.ActionEvent;
import naga.fx.event.EventHandler;
import naga.fx.event.EventTarget;
import naga.fx.event.EventType;
import naga.fx.scene.Node;
import naga.fx.scene.Scene;
import naga.fx.scene.SceneRequester;
import naga.fx.scene.effect.BlendMode;
import naga.fx.scene.effect.Effect;
import naga.fx.scene.effect.GaussianBlur;
import naga.fx.scene.input.MouseEvent;
import naga.fx.scene.transform.Transform;
import naga.fx.spi.javafx.util.FxTransforms;
import naga.fx.spi.peer.NodePeer;
import naga.fx.spi.peer.base.NodePeerBase;
import naga.fx.spi.peer.base.NodePeerMixin;
import naga.fx.spi.peer.base.NodePeerImpl;

import java.util.Collection;
import java.util.List;


/**
 * @author Bruno Salmon
 */
public abstract class FxNodePeer
        <FxN extends javafx.scene.Node, N extends Node, NB extends NodePeerBase<N, NB, NM>, NM extends NodePeerMixin<N, NB, NM>>

        extends NodePeerImpl<N, NB, NM>
        implements NodePeerMixin<N, NB, NM> {

    private FxN fxNode;

    FxNodePeer(NB base) {
        super(base);
    }

    public FxN getFxNode() {
        return fxNode;
    }

    protected abstract FxN createFxNode();

    protected void onFxNodeCreated() {
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        fxNode = createFxNode();
        getNodePeerBase().bind(node, sceneRequester);
        fxNode.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> getNode().fireEvent(toMouseEvent(getNode(), getNode(), event)));
        fxNode.addEventFilter(javafx.event.ActionEvent.ACTION, event -> getNode().fireEvent(toActionEvent(getNode(), getNode(), event)));
        onFxNodeCreated();
    }

    @Override
    public void requestFocus() {
        getFxNode().requestFocus();
    }

    @Override
    public void updateMouseTransparent(Boolean mouseTransparent) {
        getFxNode().setMouseTransparent(mouseTransparent);
    }

    @Override
    public void updateVisible(Boolean visible) {
        fxNode.setVisible(visible);
    }

    @Override
    public void updateOpacity(Double opacity) {
        fxNode.setOpacity(opacity);
    }

    @Override
    public void updateDisabled(Boolean disabled) {
        fxNode.setDisable(disabled);
    }

    @Override
    public void updateBlendMode(BlendMode blendMode) {
        fxNode.setBlendMode(toFxBlendMode(blendMode));
    }

    @Override
    public void updateEffect(Effect effect) {
        fxNode.setEffect(toFxEffect(effect));
    }

    @Override
    public void updateClip(Node clip) {
        fxNode.setClip(toFxNode(clip));
    }

    @Override
    public void updateLayoutX(Double layoutX) {
        fxNode.setLayoutX(layoutX);
    }

    @Override
    public void updateLayoutY(Double layoutY) {
        fxNode.setLayoutY(layoutY);
    }

    @Override
    public void updateTransforms(List<Transform> transforms) {
        fxNode.getTransforms().setAll(FxTransforms.toFxTransforms(transforms));
    }

    @Override
    public void updateLocalToParentTransforms(Collection<Transform> localToParentTransforms) {
        // never called
    }

    private static javafx.event.EventHandler toFxMouseEventHandler(EventHandler<? super MouseEvent> mouseEventHandler) {
        return mouseEventHandler == null ? null : event -> mouseEventHandler.handle(toMouseEvent((javafx.scene.input.MouseEvent) event));
    }

    private static MouseEvent toMouseEvent(javafx.scene.input.MouseEvent me) {
        return toMouseEvent(null, null, me);
    }

    private static MouseEvent toMouseEvent(Object source, EventTarget target, javafx.scene.input.MouseEvent e) {
        return new MouseEvent(source, target, toEventType(e.getEventType()), e.getX(), e.getY(), e.getScreenX(), e.getScreenY(), null, e.getClickCount(), e.isShiftDown(), e.isControlDown(), e.isAltDown(), e.isMetaDown(), e.isPrimaryButtonDown(), e.isMiddleButtonDown(), e.isSecondaryButtonDown(), e.isSynthesized(), e.isPopupTrigger(), e.isStillSincePress(), null);
    }

    private static ActionEvent toActionEvent(Object source, EventTarget target, javafx.event.ActionEvent e) {
        return new ActionEvent(source, target);
    }

    private static EventType toEventType(javafx.event.EventType fxType) {
        if (fxType == javafx.scene.input.MouseEvent.MOUSE_CLICKED)
            return MouseEvent.MOUSE_CLICKED;
        if (fxType == javafx.event.ActionEvent.ACTION)
            return ActionEvent.ACTION;
        return null;
    }

    private static javafx.scene.effect.BlendMode toFxBlendMode(BlendMode blendMode) {
        if (blendMode != null)
            switch (blendMode) {
                case SRC_OVER: return javafx.scene.effect.BlendMode.SRC_OVER;
                case SRC_ATOP: return javafx.scene.effect.BlendMode.SRC_ATOP;
                case ADD: return javafx.scene.effect.BlendMode.ADD;
                case MULTIPLY: return javafx.scene.effect.BlendMode.MULTIPLY;
                case SCREEN: return javafx.scene.effect.BlendMode.SCREEN;
                case OVERLAY: return javafx.scene.effect.BlendMode.OVERLAY;
                case DARKEN: return javafx.scene.effect.BlendMode.DARKEN;
                case LIGHTEN: return javafx.scene.effect.BlendMode.LIGHTEN;
                case COLOR_DODGE: return javafx.scene.effect.BlendMode.COLOR_DODGE;
                case COLOR_BURN: return javafx.scene.effect.BlendMode.COLOR_BURN;
                case HARD_LIGHT: return javafx.scene.effect.BlendMode.HARD_LIGHT;
                case SOFT_LIGHT: return javafx.scene.effect.BlendMode.SOFT_LIGHT;
                case DIFFERENCE: return javafx.scene.effect.BlendMode.DIFFERENCE;
                case EXCLUSION: return javafx.scene.effect.BlendMode.EXCLUSION;
                case RED: return javafx.scene.effect.BlendMode.RED;
                case GREEN: return javafx.scene.effect.BlendMode.GREEN;
                case BLUE: return javafx.scene.effect.BlendMode.BLUE;
            }
        return null;
    }

    private static javafx.scene.effect.Effect toFxEffect(Effect effect) {
        if (effect == null)
            return null;
        if (effect instanceof GaussianBlur) {
            GaussianBlur b = (GaussianBlur) effect;
            return new javafx.scene.effect.GaussianBlur(b.getRadius());
        }
        return null;
    }

    static javafx.scene.Node toFxNode(Node node) {
        return toFxNode(node, null);
    }

    static javafx.scene.Node toFxNode(Node node, Scene scene) {
        if (node != null) {
            if (scene != null)
                node.setScene(scene);
            NodePeer nodePeer = node.getOrCreateAndBindNodePeer();
            if (nodePeer instanceof FxNodePeer)
                return ((FxNodePeer) nodePeer).getFxNode();
        }
        return null;
    }

}
