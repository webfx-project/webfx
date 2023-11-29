package dev.webfx.kit.registry.javafxweb;

import dev.webfx.kit.mapper.peers.javafxweb.engine.WebEnginePeerBase;
import dev.webfx.kit.mapper.peers.javafxweb.spi.gwt.HtmlWebViewPeer;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.scheduler.Scheduler;
import elemental2.dom.HTMLIFrameElement;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * @author Bruno Salmon
 */
final class GwtWebEnginePeer extends WebEnginePeerBase {

    private final WebEngine webEngine;
    private HTMLIFrameElement iFrame;

    public GwtWebEnginePeer(WebEngine webEngine) {
        this.webEngine = webEngine;
        WebView webView = webEngine.getWebView();
        FXProperties.runNowAndOnPropertiesChange(e -> updateState(), webView.sceneProperty());
    }

    private void updateState() {
        if (iFrame == null) {
            HtmlWebViewPeer peer = (HtmlWebViewPeer) webEngine.getWebView().getNodePeer();
            if (peer != null)
                iFrame = (HTMLIFrameElement) peer.getElement();
        }
        if (iFrame != null && iFrame.contentWindow != null)
            worker.setState(Worker.State.READY);
        else {
            worker.setState(Worker.State.SCHEDULED);
            if (webEngine.getWebView().getScene() != null)
                Scheduler.scheduleDelay(100, this::updateState);
        }
    }

    @Override
    public Object executeScript(String script) {
        Object result = GwtJSObject.eval(iFrame.contentWindow, script);
        return GwtJSObject.wrapJSObject(result);
    }

}
