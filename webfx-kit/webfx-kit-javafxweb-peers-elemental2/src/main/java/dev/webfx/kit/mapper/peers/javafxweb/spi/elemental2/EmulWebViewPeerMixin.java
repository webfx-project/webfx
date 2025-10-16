package dev.webfx.kit.mapper.peers.javafxweb.spi.elemental2;

import dev.webfx.kit.mapper.peers.javafxweb.base.WebViewPeerMixin;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;

public interface EmulWebViewPeerMixin
        <N extends WebView, NB extends EmulWebViewPeerBase<N, NB, NM>, NM extends EmulWebViewPeerMixin<N, NB, NM>>
        extends WebViewPeerMixin<N, NB, NM> {

    void updateUrl(String url);

    void updateLoadContent(String content);

    void updatePageFill(Color pageFill);
}
