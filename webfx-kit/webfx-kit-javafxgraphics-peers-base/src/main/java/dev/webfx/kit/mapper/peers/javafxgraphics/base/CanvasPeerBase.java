package dev.webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;

/**
 * @author Bruno Salmon
 */
public class CanvasPeerBase
        <N extends Canvas, NB extends CanvasPeerBase<N, NB, NM>, NM extends CanvasPeerMixin<N, NB, NM>>

        extends NodePeerBase<N, NB, NM> {

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.widthProperty()
                , node.heightProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.widthProperty(), changedProperty, mixin::updateWidth)
                || updateProperty(node.heightProperty(), changedProperty, mixin::updateHeight)
                ;
    }
}
