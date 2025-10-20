package dev.webfx.kit.mapper.peers.javafxweb.spi.elemental2;

import dev.webfx.kit.mapper.peers.javafxgraphics.HasNoChildrenPeers;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.HtmlNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
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

import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public class HtmlWebViewPeer
        <N extends WebView, NB extends EmulWebViewPeerBase<N, NB, NM>, NM extends EmulWebViewPeerMixin<N, NB, NM>>
        extends HtmlNodePeer<N, NB, NM>
        implements EmulWebViewPeerMixin<N, NB, NM>, HasNoChildrenPeers {

    private static final boolean DEBUG = false;

    private static final String LOADING_READY_STATE = "loading";
    private static final String INTERACTIVE_READY_STATE = "interactive";
    private static final String COMPLETE_READY_STATE = "complete";

    private static final String WEBFX_LOADING_MODE_PROPERTIES_KEY = "webfx-loadingMode";
    private static final String WEBFX_LOADING_MODE_PREFETCH = "prefetch";
    private static final String WEBFX_LOADING_MODE_REPLACE = "replace";

    private final HTMLIFrameElement iFrame;
    private Scheduled iFrameStateChecker;
    private boolean onLoadAlreadyCalled;
    private String lastRequestedUrl; // used to reload the iFrame with correct content in some cases

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
        //HtmlUtil.setAttribute(iFrame, "allowfullscreen", "true"); // old way (must be executed second otherwise warning)
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
        // Note: webViewElement.connectedCallback & webViewElement.disconnectedCallback don't work
        // TODO: investigate if we can make them work to detect subtil iFrame unload like immediate DOM remove + insert
        //  see https://developer.mozilla.org/en-US/docs/Web/API/Web_components/Using_custom_elements
    }

    public HTMLIFrameElement getIFrame() { // called by GwtWebEnginePeer
        return iFrame;
    }

    private Window getSafeContentWindow() {
        try {
            return iFrame.contentWindow; // May raise a SecurityError (or be null) if not from the same origin
        } catch (Exception e) {
            logDebug("⚠️ Browser is blocking access to iFrame.contentWindow");
            return null;
        }
    }

    private Document getSafeContentDocument() {
        try {
            return iFrame.contentDocument; // May raises a SecurityError (or be null) if not from the same origin
        } catch (Exception e) {
            logDebug("⚠️ Browser is blocking access to iFrame.contentDocument");
            return null;
        }
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
            //setWebEngineLoadWorkerState(Worker.State.READY);
        } else {
            resetWebEngineLoadWorkerState();
            // WebFX proposes different loading mode for the iFrame:
            Object webfxLoadingMode = getNode().getProperties().get(WEBFX_LOADING_MODE_PROPERTIES_KEY);
            Window contentWindow = getSafeContentWindow();
            if (WEBFX_LOADING_MODE_PREFETCH.equals(webfxLoadingMode) && contentWindow != null) { // prefetch mode
                // For a better error reporting, we prefetch the url, and then inject the result into the iFrame, but
                // this alternative loading mode is not perfect either, as it may face security issue like CORS.
                contentWindow.fetch(url)
                        .then(response -> {
                            response.text().then(text -> {
                                setWebEngineLoadWorkerState(Worker.State.RUNNING);
                                updateLoadContent(text);
                                setWebEngineLoadWorkerState(Worker.State.SUCCEEDED);
                                return null;
                            }).catch_(error -> {
                                setWebEngineLoadWorkerState(Worker.State.FAILED);
                                reportError();
                                return null;
                            });
                            return null;
                        }).catch_(error -> {
                            setWebEngineLoadWorkerState(Worker.State.FAILED);
                            reportError();
                            return null;
                        });
            } else { // Standard or replace mode
                if (!WEBFX_LOADING_MODE_REPLACE.equals(webfxLoadingMode) || contentWindow == null) { // Standard mode
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
                    contentWindow.location.replace(url);
                }
                // TODO: extend the following state management to the prefetch mode as well
                // We also need to continue updating the web engine load worker state to report how the loading is going
                // in case the application code is listening these states.
                startIFrameStateChecker();
                onLoadAlreadyCalled = false;
                // Note: iFrame.onbeforeunload is quite useless (never called)
                iFrame.onload = e -> { // Note: if the iFrame is removed and then reinserted into the DOM, the browser
                    // will unload and then reload the iFrame. So onLoad may be called several times.
                    if (!onLoadAlreadyCalled) { // initial onLoad (not reload)
                        logDebug("Detected iFrame onload");
                        onLoadAlreadyCalled = true;
                        String readyState = readReadyStateAndUpdateWebEngineLoadWorkerState();
                        if (readyState == null) { // readyState may not be accessible due to CORS issues, but since we
                            // received the onLoad event, it's very likely that the readyState is complete
                            updateWebEngineLoadWorkerStateFromReadyState(COMPLETE_READY_STATE);
                        }
                    } else { // reload
                        logDebug("Detected iFrame reload (from onload)");
                        onReloadDetected();
                    }
                };
                // Error management. Note: this listener is not called when network errors occur. If the application
                // code wants to detect them, it can try the "prefetch" loading mode can be used instead.
                iFrame.onerror = e -> {
                    logDebug("iFrame onerror is called");
                    setWebEngineLoadWorkerState(Worker.State.FAILED);
                    stopIFrameStateChecker();
                    reportError();
                    return null;
                };
                iFrame.onabort = e -> {
                    logDebug("iFrame onabort is called");
                    setWebEngineLoadWorkerState(Worker.State.CANCELLED);
                    stopIFrameStateChecker();
                    return null;
                };
            }
        }
        lastRequestedUrl = url;
    }

    private WorkerImpl<Void> getWebEngineLoadWorker() {
        return (WorkerImpl<Void>) getNode().getEngine().getLoadWorker();
    }

    private Worker.State getWebEngineLoadWorkerState() {
        return getWebEngineLoadWorker().getState();
    }

    private void setWebEngineLoadWorkerState(Worker.State state) {
        getWebEngineLoadWorker().setState(state);
    }

    private void resetWebEngineLoadWorkerState() {
        // We move the web engine worker state to SCHEDULED, but to ensure the change of state can be eventually
        // detected by the application code (because it may be already in that state), we move it first to READY
        setWebEngineLoadWorkerState(Worker.State.READY); // this transition can be used by the application code to detect a reload
        setWebEngineLoadWorkerState(Worker.State.SCHEDULED);
    }

    private void onReloadDetected() { // Very probably caused by (unwanted) DOM remove + insert
        //  The iFrame may be "reloaded" with empty content (especially with "prefetch" & "replace" webfx load methods)
        if (!Objects.equals(iFrame.src, lastRequestedUrl)) { // => needs to reload the iFrame with the requested url
            updateUrl(lastRequestedUrl);
        } else { // otherwise, ok (the iFrame reloaded with correct url) but the application code might need to be
            // notified of this reload (ex: WebFX Extras WebViewPane):
            resetWebEngineLoadWorkerState();
            startIFrameStateChecker(); // We ensure the state checker is running (will restart it if it's a reload)
        }
    }

    private void startIFrameStateChecker() {
        if (iFrameStateChecker != null && iFrameStateChecker.isRunning())
            return;
        iFrameStateChecker = UiScheduler.schedulePeriodic(100, scheduled -> {
            readReadyStateAndUpdateWebEngineLoadWorkerState();
            if (getWebEngineLoadWorkerState() == Worker.State.SUCCEEDED) {
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
        Document contentDocument = getSafeContentDocument();
        return contentDocument == null ? null : contentDocument.readyState.toLowerCase();
    }

    private String readReadyStateAndUpdateWebEngineLoadWorkerState() {
        String readyState = getIFrameDocumentReadyState();
        logDebug("iFrame readyState = " + readyState);
        if (readyState != null) {
            updateWebEngineLoadWorkerStateFromReadyState(readyState);
        } else { // Can happen due to cross-origin restrictions, or at the end of iFrame state lifecycle
            if (onLoadAlreadyCalled) { // We stop the checker at this point (otherwise it can run indefinitely on null state)
                logDebug("Stopping iFrame state checker because readyState is null");
                stopIFrameStateChecker();
            }
        }
        return readyState;
    }

    private void updateWebEngineLoadWorkerStateFromReadyState(String readyState) {
        switch (readyState) {
            case LOADING_READY_STATE:
                // Reload detection:
                // At this stage, the state should be SCHEDULED, if not, this indicates that it's a reload
                if (getWebEngineLoadWorkerState() != Worker.State.SCHEDULED) {
                    logDebug("Detected iFrame reload (from readyState)");
                    onReloadDetected();
                }
                break;
            case INTERACTIVE_READY_STATE:
                setWebEngineLoadWorkerState(Worker.State.RUNNING);
                break;
            case COMPLETE_READY_STATE:
                // Note: an immediate call to the JS loaded code made at this point by the app code might fail (this
                // has been observed with the web payment form and Authorize.net)
                UiScheduler.scheduleDeferred(() -> // We defer the success notification to prevent this issue
                    setWebEngineLoadWorkerState(Worker.State.SUCCEEDED));
                break;
            default:
                log("Unknown iFrame readyState: " + readyState);
        }
    }

    private void logDebug(String message) {
        if (DEBUG)
            log(message);
    }

    private void log(String message) {
        DomGlobal.console.log(message + " | " + this);
    }

}
