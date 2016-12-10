package naga.providers.toolkit.javafx.fx.viewer;

import javafx.event.EventHandler;
import naga.providers.toolkit.javafx.events.FxMouseEvent;
import naga.providers.toolkit.javafx.util.FxTransforms;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.effect.BlendMode;
import naga.toolkit.fx.scene.effect.Effect;
import naga.toolkit.fx.scene.effect.GaussianBlur;
import naga.toolkit.fx.scene.impl.NodeImpl;
import naga.toolkit.fx.scene.transform.Transform;
import naga.toolkit.fx.spi.Drawing;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.impl.DrawingImpl;
import naga.toolkit.fx.spi.viewer.NodeViewer;
import naga.toolkit.fx.spi.viewer.base.NodeViewerBase;
import naga.toolkit.fx.spi.viewer.base.NodeViewerImpl;
import naga.toolkit.fx.spi.viewer.base.NodeViewerMixin;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;

import java.util.Collection;
import java.util.List;


/**
 * @author Bruno Salmon
 */
public abstract class FxNodeViewer
        <FxN extends javafx.scene.Node, N extends Node, NV extends NodeViewerBase<N, NV, NM>, NM extends NodeViewerMixin<N, NV, NM>>

        extends NodeViewerImpl<N, NV, NM>
        implements NodeViewerMixin<N, NV, NM> {

    private FxN fxNode;

    FxNodeViewer(NV base) {
        super(base);
    }

    public FxN getFxNode() {
        return fxNode;
    }

    abstract FxN createFxNode();

    @Override
    public void bind(N node, DrawingRequester drawingRequester) {
        fxNode = createFxNode();
        getNodeViewerBase().bind(node, drawingRequester);
    }

    @Override
    public void updateOnMouseClicked(UiEventHandler<? super MouseEvent> onMouseClicked) {
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
        fxNode.setClip(getFxNode(clip));
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

    private static EventHandler<? super javafx.scene.input.MouseEvent> toFxMouseEventHandler(UiEventHandler<? super MouseEvent> mouseEventHandler) {
        return mouseEventHandler == null ? null : (EventHandler<javafx.scene.input.MouseEvent>) event -> mouseEventHandler.handle(new FxMouseEvent(event));
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

    static javafx.scene.Node getFxNode(Node node) {
        return getFxNode(node, null);
    }

    static javafx.scene.Node getFxNode(Node node, Drawing drawing) {
        if (node != null) {
            if (drawing != null)
                ((NodeImpl) node).setDrawing((DrawingImpl) drawing);
            NodeViewer nodeViewer = node.getOrCreateAndBindNodeViewer();
            if (nodeViewer instanceof FxNodeViewer)
                return ((FxNodeViewer) nodeViewer).getFxNode();
        }
        return null;
    }

}
