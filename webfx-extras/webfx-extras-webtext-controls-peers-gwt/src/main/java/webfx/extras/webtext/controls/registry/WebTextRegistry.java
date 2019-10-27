package webfx.extras.webtext.controls.registry;

import webfx.extras.webtext.controls.HtmlText;
import webfx.extras.webtext.controls.HtmlTextEditor;
import webfx.extras.webtext.controls.peers.gwt.html.HtmlHtmlTextEditorPeer;
import webfx.extras.webtext.controls.peers.gwt.html.HtmlHtmlTextPeer;
import webfx.extras.webtext.controls.SvgText;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.HtmlSvgTextPeer;

import static webfx.fxkit.javafxgraphics.mapper.spi.NodePeerFactoryRegistry.registerNodePeerFactory;

public final class WebTextRegistry {

    public static void registerHtmlText() {
        registerNodePeerFactory(HtmlText.class, HtmlHtmlTextPeer::new);
    }

    public static void registerHtmlTextEditor() {
        registerNodePeerFactory(HtmlTextEditor.class, HtmlHtmlTextEditorPeer::new);
    }

    public static void registerSvgText() {
        registerNodePeerFactory(SvgText.class, HtmlSvgTextPeer::new);
    }

}
