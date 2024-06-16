package dev.webfx.kit.registry.javafxweb;

import dev.webfx.kit.mapper.peers.javafxweb.engine.WebEnginePeerBase;
import dev.webfx.kit.mapper.peers.javafxweb.spi.gwt.HtmlWebViewPeer;
import dev.webfx.platform.console.Console;
import elemental2.dom.Document;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLIFrameElement;
import elemental2.dom.Window;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * @author Bruno Salmon
 */
final class GwtWebEnginePeer extends WebEnginePeerBase {

    // Note - WebFX convention: if webEngine.getWebView() is null, it's a webEngine that applies to the global window
    private final WebEngine webEngine;

    public GwtWebEnginePeer(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    private HTMLIFrameElement getIFrame() {
        WebView webView = webEngine.getWebView();
        HtmlWebViewPeer peer = webView == null ? null : (HtmlWebViewPeer) webView.getNodePeer();
        if (peer != null)
            return peer.getIFrame();
        return null;
    }

    private Window getScriptWindow() {
        HTMLIFrameElement iFrame = getIFrame();
        // contentWindow is set only once the iFrame is inserted to the DOM, before that it is null
        Window iFrameWindow = iFrame == null ? null : iFrame.contentWindow;
        if (iFrameWindow == null && webEngine.getWebView() == null)
            iFrameWindow = DomGlobal.window;
        return iFrameWindow;
    }

    private Document getDocument() {
        HTMLIFrameElement iFrame = getIFrame();
        Document iFrameDocument = iFrame == null ? null : iFrame.contentDocument;
        return iFrameDocument != null ? iFrameDocument : DomGlobal.document;
    }

    @Override
    public Object executeScript(String script) {
        Window scriptWindow = getScriptWindow();
        if (scriptWindow != null) {
            if ("window".equals(script))
                return GwtJSObject.wrapJSObject(scriptWindow);
            return GwtJSObject.eval(scriptWindow, script);
        }
        Console.log("⚠️ Couldn't execute script because the webEngine window is not ready (" + (getIFrame() == null ? "iFrame is null)" : getIFrame().contentWindow == null ? "iFrame.contentWindow is null)" : "???)"));
        return null;
    }

    @Override
    public void updateUserStyleSheetLocation(String userStyleSheetLocation) {
        // TODO
    }
}
