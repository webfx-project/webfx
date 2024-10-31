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

    private Window getSafeContentWindow(HTMLIFrameElement iFrame) {
        try {
            return iFrame == null ? null : iFrame.contentWindow;
        } catch (Exception e) {
            Console.log("⚠️ Browser is blocking access to iFrame.contentWindow | " + this);
            return null;
        }
    }

    private Document getSafeContentDocument(HTMLIFrameElement iFrame) {
        try {
            return iFrame == null ? null : iFrame.contentDocument;
        } catch (Exception e) {
            Console.log("⚠️ Browser is blocking access to iFrame.contentDocument | " + this);
            return null;
        }
    }

    private Window getScriptWindow() {
        HTMLIFrameElement iFrame = getIFrame();
        // contentWindow is set only once the iFrame is inserted to the DOM, before that it is null
        Window iFrameWindow = getSafeContentWindow(iFrame);
        if (iFrameWindow == null && webEngine.getWebView() == null)
            iFrameWindow = DomGlobal.window;
        return iFrameWindow;
    }

    private Document getDocument() {
        HTMLIFrameElement iFrame = getIFrame();
        Document iFrameDocument = getSafeContentDocument(iFrame);
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
        HTMLIFrameElement iFrame = getIFrame();
        Console.log("⚠️ Couldn't execute script because the webEngine window is not ready (" + (iFrame == null ? "iFrame is null)" : getSafeContentWindow(iFrame) == null ? "iFrame.contentWindow is null)" : "???)"));
        return null;
    }

    @Override
    public void updateUserStyleSheetLocation(String userStyleSheetLocation) {
        // TODO
    }
}
