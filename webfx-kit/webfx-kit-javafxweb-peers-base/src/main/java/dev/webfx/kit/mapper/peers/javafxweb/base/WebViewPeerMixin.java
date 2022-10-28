package dev.webfx.kit.mapper.peers.javafxweb.base;

import javafx.scene.web.WebView;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerMixin;
/**
 * @author Bruno Salmon
 */
public interface WebViewPeerMixin
        <N extends WebView, NB extends WebViewPeerBase<N, NB, NM>, NM extends WebViewPeerMixin<N, NB, NM>>
        extends NodePeerMixin<N, NB, NM> {

    void updateWidth(Number width);

    void updateHeight(Number height);

}
