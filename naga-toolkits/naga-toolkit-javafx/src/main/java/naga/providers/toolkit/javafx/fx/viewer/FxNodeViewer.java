package naga.providers.toolkit.javafx.fx.viewer;

import naga.providers.toolkit.javafx.util.FxTransforms;
import naga.toolkit.fx.event.EventHandler;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Scene;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.effect.BlendMode;
import naga.toolkit.fx.scene.effect.Effect;
import naga.toolkit.fx.scene.effect.GaussianBlur;
import naga.toolkit.fx.scene.impl.NodeImpl;
import naga.toolkit.fx.scene.impl.SceneImpl;
import naga.toolkit.fx.scene.input.MouseEvent;
import naga.toolkit.fx.scene.transform.Transform;
import naga.toolkit.fx.spi.viewer.NodeViewer;
import naga.toolkit.fx.spi.viewer.base.NodeViewerBase;
import naga.toolkit.fx.spi.viewer.base.NodeViewerImpl;
import naga.toolkit.fx.spi.viewer.base.NodeViewerMixin;

import java.util.Collection;
import java.util.List;


/**
 * @author Bruno Salmon
 */
public abstract class FxNodeViewer
        <FxN extends javafx.scene.Node, N extends Node, NB extends NodeViewerBase<N, NB, NM>, NM extends NodeViewerMixin<N, NB, NM>>

        extends NodeViewerImpl<N, NB, NM>
        implements NodeViewerMixin<N, NB, NM> {

    private FxN fxNode;

    FxNodeViewer(NB base) {
        super(base);
    }

    public FxN getFxNode() {
        return fxNode;
    }

    protected abstract FxN createFxNode();

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        fxNode = createFxNode();
        getNodeViewerBase().bind(node, sceneRequester);
    }

    @Override
    public void updateOnMouseClicked(EventHandler<? super MouseEvent> onMouseClicked) {
        getFxNode().setOnMouseClicked(toFxMouseEventHandler(onMouseClicked));
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

    private static MouseEvent toMouseEvent(javafx.scene.input.MouseEvent mouseEvent) {
        return new MouseEvent();
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
                ((NodeImpl) node).setScene((SceneImpl) scene);
            NodeViewer nodeViewer = node.getOrCreateAndBindNodeViewer();
            if (nodeViewer instanceof FxNodeViewer)
                return ((FxNodeViewer) nodeViewer).getFxNode();
        }
        return null;
    }

}
