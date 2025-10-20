package dev.webfx.kit.mapper.peers.javafxweb.spi.elemental2;

import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxweb.base.WebViewPeerBase;
import javafx.beans.value.ObservableValue;
import javafx.scene.web.WebView;

/**
 * @author Bruno Salmon
 */
public class EmulWebViewPeerBase
        <N extends WebView, NB extends EmulWebViewPeerBase<N, NB, NM>, NM extends EmulWebViewPeerMixin<N, NB, NM>>
        extends WebViewPeerBase<N, NB, NM> {

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.urlProperty()
                , node.loadContentProperty()
                , node.pageFillProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
               || updateProperty(node.urlProperty(), changedProperty, mixin::updateUrl)
               || updateProperty(node.loadContentProperty(), changedProperty, mixin::updateLoadContent)
               || updateProperty(node.pageFillProperty(), changedProperty, mixin::updatePageFill)
                ;
    }

}