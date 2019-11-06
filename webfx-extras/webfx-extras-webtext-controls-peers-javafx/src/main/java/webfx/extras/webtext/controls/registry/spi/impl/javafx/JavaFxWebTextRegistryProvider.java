package webfx.extras.webtext.controls.registry.spi.impl.javafx;

import webfx.extras.webtext.controls.HtmlText;
import webfx.extras.webtext.controls.HtmlTextEditor;
import webfx.extras.webtext.controls.peers.javafx.FxHtmlTextEditorPeer;
import webfx.extras.webtext.controls.peers.javafx.FxHtmlTextPeer;
import webfx.extras.webtext.controls.registry.spi.WebTextRegistryProvider;

import static webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry.registerNodePeerFactory;

public final class JavaFxWebTextRegistryProvider implements WebTextRegistryProvider {

    public void registerHtmlText() {
        registerNodePeerFactory(HtmlText.class, FxHtmlTextPeer::new);
    }

    public void registerHtmlTextEditor() {
        registerNodePeerFactory(HtmlTextEditor.class, FxHtmlTextEditorPeer::new);
    }

    public void registerSvgText() {
    }

}
