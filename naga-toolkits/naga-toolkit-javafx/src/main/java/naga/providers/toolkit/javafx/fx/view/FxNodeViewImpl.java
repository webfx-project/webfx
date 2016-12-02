package naga.providers.toolkit.javafx.fx.view;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import naga.providers.toolkit.javafx.events.FxMouseEvent;
import naga.providers.toolkit.javafx.util.FxTransforms;
import naga.toolkit.fx.effect.BlendMode;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.impl.DrawingImpl;
import naga.toolkit.fx.spi.view.NodeView;
import naga.toolkit.fx.effect.GaussianBlur;
import naga.toolkit.fx.effect.Effect;
import naga.toolkit.properties.conversion.ConvertedProperty;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;
import naga.toolkit.util.ObservableLists;
import naga.toolkit.util.Properties;

/**
 * @author Bruno Salmon
 */
abstract class FxNodeViewImpl<N extends Node, FxN extends javafx.scene.Node> implements FxNodeView<N, FxN> {

    FxN fxNode;

    @Override
    public void bind(N node, DrawingRequester drawingRequester) {
        setAndBindNodeProperties(node, createFxNode(node));
    }

    abstract FxN createFxNode(N node);

    void setAndBindNodeProperties(N node, FxN fxNode) {
        this.fxNode = fxNode;
        ObservableLists.bindConverted(fxNode.getTransforms(), node.getTransforms(), FxTransforms::toFxTransform);
        fxNode.visibleProperty().bind(node.visibleProperty());
        fxNode.opacityProperty().bind(node.opacityProperty());
        DrawingImpl drawing = DrawingImpl.getThreadLocalDrawing();
        Properties.runNowAndOnPropertiesChange((clipProperty) -> fxNode.setClip(getFxNode(node.getClip(), drawing)), node.clipProperty());
        Properties.runNowAndOnPropertiesChange((blendMode) -> fxNode.setBlendMode(toFxBlendMode(node.getBlendMode())), node.blendModeProperty());
        Properties.runNowAndOnPropertiesChange((effect) -> fxNode.setEffect(toFxEffect(node.getEffect())), node.effectProperty());
        fxNode.layoutXProperty().bind(node.layoutXProperty());
        fxNode.layoutYProperty().bind(node.layoutYProperty());
        fxNode.onMouseClickedProperty().bind(new ConvertedProperty<>(node.onMouseClickedProperty(), FxNodeViewImpl::toFxMouseEventHandler));
        fxNode.mouseTransparentProperty().bind(node.mouseTransparentProperty());
    }

    @Override
    public boolean updateList(ObservableList changedList) {
        return false;
    }

    @Override
    public void unbind() {
        fxNode = null;
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return false;
    }

    public FxN getFxNode() {
        return fxNode;
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

    private static javafx.scene.Node getFxNode(Node node, DrawingImpl drawing) {
        if (node != null) {
            NodeView nodeView = drawing.getOrCreateAndBindNodeView(node);
            if (nodeView instanceof FxNodeView)
                return ((FxNodeView) nodeView).getFxNode();
        }
        return null;
    }

}
