package dev.webfx.kit.mapper.peers.javafxweb.base;

import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerBase;
import javafx.beans.value.ObservableValue;
import javafx.scene.web.WebView;

/**
 * @author Bruno Salmon
 */
public class WebViewPeerBase
        <N extends WebView, NB extends WebViewPeerBase<N, NB, NM>, NM extends WebViewPeerMixin<N, NB, NM>>
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

