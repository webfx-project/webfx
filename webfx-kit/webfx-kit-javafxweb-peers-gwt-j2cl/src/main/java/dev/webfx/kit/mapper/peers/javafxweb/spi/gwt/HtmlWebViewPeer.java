package dev.webfx.kit.mapper.peers.javafxweb.spi.gwt;

import dev.webfx.kit.mapper.peers.javafxgraphics.HasNoChildrenPeers;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.HtmlNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;
import dev.webfx.kit.mapper.peers.javafxweb.engine.WorkerImpl;
import dev.webfx.platform.scheduler.Scheduled;
import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.util.Strings;
import elemental2.dom.*;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;

/**
 * @author Bruno Salmon
 */
public class HtmlWebViewPeer
        <N extends WebView, NB extends EmulWebViewPeerBase<N, NB, NM>, NM extends EmulWebViewPeerMixin<N, NB, NM>>
        extends HtmlNodePeer<N, NB, NM>
        implements EmulWebViewPeerMixin<N, NB, NM>, HasNoChildrenPeers {

    private static final boolean DEBUG = false;

    private final HTMLIFrameElement iFrame;
    private Scheduled iFrameStateChecker;

    public HtmlWebViewPeer() {
        this((NB) new EmulWebViewPeerBase(), HtmlUtil.createElement("fx-webview"));
    }

    public HtmlWebViewPeer(NB base, HTMLElement webViewElement) {
        super(base, webViewElement);
        // There will be only child which will be an iFrame to display and load the web content.
        iFrame = HtmlUtil.createElement("iframe");
        HtmlUtil.setChild(getContainer(), iFrame);
        HtmlUtil.setStyleAttribute(iFrame, "width", "100%");  // 100% of <fx-webview>
        HtmlUtil.setStyleAttribute(iFrame, "height", "100%"); // 100% of <fx-webview>
        // Allowing fullscreen and autoplay for videos
        iFrame.allow = "fullscreen; autoplay"; // new way
        HtmlUtil.setAttribute(iFrame, "allowfullscreen", "true"); // old way (must be executed second otherwise warning)
        // Focus management.
        // 1) Detecting when the iFrame gained focus
        DomGlobal.window.addEventListener("blur", e -> { // when iFrame gained focus, the parent window lost focus
            if (DomGlobal.document.activeElement == iFrame) { // and the active element should be the iFrame.
                // Then, we set the WebView as the new focus owner in JavaFX
                N webView = getNode();
                Scene scene = webView == null ? null : webView.getScene();
                if (scene != null)
                    scene.focusOwnerProperty().setValue(webView);
            }
        });
        // 2) Detecting when the iFrame lost focus
        DomGlobal.window.addEventListener("focus", e -> { // when iFrame lost focus, the parent window gained focus
            // If the WebView is still the focus owner in JavaFX, we clear that focus to report the WebView lost focus
            N webView = getNode();
            Scene scene = webView == null ? null : webView.getScene();
            if (scene != null && scene.getFocusOwner() == webView) {
                scene.focusOwnerProperty().setValue(null);
            }
        });
    }

    public HTMLIFrameElement getIFrame() { // called by GwtWebEnginePeer
        return iFrame;
    }

    private void reportError() {
        EventHandler<WebErrorEvent> onError = getNode().getEngine().getOnError();
        if (onError != null)
            onError.handle(new WebErrorEvent(this, WebErrorEvent.ANY, null, null));
    }

    @Override
    public void updateWidth(Number width) {
        getElement().style.width = CSSProperties.WidthUnionType.of(toPx(width.doubleValue()));
    }

    @Override
    public void updateHeight(Number height) {
        getElement().style.height = CSSProperties.HeightUnionType.of(toPx(height.doubleValue()));
    }

    @Override
    public void updatePageFill(Color pageFill) {
        HtmlUtil.setStyleAttribute(iFrame, "background", HtmlPaints.toCssColor(pageFill));
    }

    @Override
    public void updateLoadContent(String content) {
        if (content != null)
            iFrame.srcdoc = content;
    }

    @Override
    public void updateUrl(String url) {
        if (url == null) {
            if (!Strings.isEmpty(iFrame.src))
                iFrame.src = "";
        } else {
            // We move the web engine worker state to SCHEDULED, but to ensure the change of state can be eventually
            // detected by the application code (because it may be already in that state), we move it first to READY
            WorkerImpl<Void> worker = getWebEngineLoadWorker();
            worker.setState(Worker.State.READY);
            worker.setState(Worker.State.SCHEDULED);
            // WebFX proposes different loading mode for the iFrame:
            Object webfxLoadingMode = getNode().getProperties().get("webfx-loadingMode");
            if ("prefetch".equals(webfxLoadingMode)) { // prefetch mode
                // For a better error reporting, we prefetch the url, and then inject the result into the iFrame, but
                // this alternative loading mode is not perfect either, as it may face security issue like CORS.
                iFrame.contentWindow.fetch(url)
                        .then(response -> {
                            response.text().then(text -> {
                                worker.setState(Worker.State.RUNNING);
                                updateLoadContent(text);
                                worker.setState(Worker.State.SUCCEEDED);
                                return null;
                            }).catch_(error -> {
                                worker.setState(Worker.State.FAILED);
                                reportError();
                                return null;
                            });
                            return null;
                        }).catch_(error -> {
                            worker.setState(Worker.State.FAILED);
                            reportError();
                            return null;
                        });
            } else { // Standard or replace mode
                if (!"replace".equals(webfxLoadingMode)) { // Standard mode
                    iFrame.src = url; // Standard way to load an iFrame
                    // But it has 2 downsides (which is why webfx proposes alternative loading modes):
                    // 1) it doesn't report any network errors (iFrame.onerror not called). Issue addressed by the webfx
                    // "prefetch" mode
                    // 2) it has a side effect on the parent window navigation when the url changes several times. The first
                    // time has no side effect, but the subsequent times add an unwanted new entry in the parent window
                    // history, so the user needs to click twice to go back to the previous page, instead of a single click.
                    // Issue addressed by the webfx "replace" mode.
                } else { // replace mode
                    // Using iframe location replace() instead of setting iFrame.src has the benefit to not interfere with
                    // the parent window history (see explanation in standard loading mode below). However, it doesn't work
                    // in all situations (ex: embed YouTube videos are not loading in this mode).
                    iFrame.contentWindow.location.replace(url);
                }
                // We also need to continue updating the web engine load worker state to report how the loading is going
                // in case the application code is listening these states.
                startIFrameStateChecker();
                iFrame.onload = e -> { // Note: if the iFrame is removed and then reinserted into the DOM, the browser
                    // will unload and then reload the iFrame. So onLoad may be called several times.
                    logDebug("iFrame onload is called");
                    startIFrameStateChecker(); // We ensure the state checker is running (will restart it if it's a reload)
                    updateWebEngineLoadWorkerState(); // Will include a reload detection
                };
                // Error management. Note: this listener is not called when network errors occur. If the application
                // code wants to detect them, it can try the "prefetch" loading mode can be used instead.
                iFrame.onerror = e -> {
                    logDebug("iFrame onerror is called");
                    worker.setState(Worker.State.FAILED);
                    stopIFrameStateChecker();
                    reportError();
                    return null;
                };
                iFrame.onabort = e -> {
                    logDebug("iFrame onabort is called");
                    worker.setState(Worker.State.CANCELLED);
                    stopIFrameStateChecker();
                    return null;
                };
            }
        }
    }

    private WorkerImpl<Void> getWebEngineLoadWorker() {
        return (WorkerImpl<Void>) getNode().getEngine().getLoadWorker();
    }

    private void startIFrameStateChecker() {
        if (iFrameStateChecker != null && iFrameStateChecker.isRunning())
            return;
        iFrameStateChecker = UiScheduler.schedulePeriodic(100, scheduled -> {
            updateWebEngineLoadWorkerState();
            if (getWebEngineLoadWorker().getState() == Worker.State.SUCCEEDED) {
                scheduled.cancel();
            }
        });
    }

    private void stopIFrameStateChecker() {
        if (iFrameStateChecker != null)
            iFrameStateChecker.cancel();
        iFrameStateChecker = null;
    }

    private String getIFrameDocumentReadyState() {
        // Note: iFrame.contentDocument can be inaccessible (returns null) with cross-origin
        Document contentDocument = iFrame.contentDocument;
        return contentDocument == null ? null : contentDocument.readyState.toLowerCase();
    }

    private void updateWebEngineLoadWorkerState() {
        WorkerImpl<Void> worker = (WorkerImpl<Void>) getNode().getEngine().getLoadWorker();
        String readyState = getIFrameDocumentReadyState();
        logDebug("iFrame readyState = " + readyState);
        if (readyState != null) {
            switch (readyState) {
                case "loading":
                    // Reload detection:
                    // At this stage, the state should be SCHEDULED, if not, this indicates that it's a reload
                    if (worker.getState() != Worker.State.SCHEDULED) {
                        // We re-execute the initial state sequence
                        worker.setState(Worker.State.READY); // this transition can be used by the application code to detect a reload
                        worker.setState(Worker.State.SCHEDULED);
                    }
                    break;
                case "interactive":
                    worker.setState(Worker.State.RUNNING);
                    break;
                case "complete":
                    worker.setState(Worker.State.SUCCEEDED);
                    break;
                default:
                    DomGlobal.console.log("Unknown iFrame readyState: " + readyState);
            }
        }
    }

    private static void logDebug(String message) {
        if (DEBUG)
            DomGlobal.console.log(message);
    }

}
