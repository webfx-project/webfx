package dev.webfx.kit.mapper.peers.javafxweb.spi.gwt;

import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLIFrameElement;
import javafx.event.EventHandler;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;

/**
 * @author Bruno Salmon
 */
public class HtmlWebViewPeer
        <N extends WebView, NB extends EmulWebViewPeerBase<N, NB, NM>, NM extends EmulWebViewPeerMixin<N, NB, NM>>
        extends HtmlNodePeer<N, NB, NM>
        implements EmulWebViewPeerMixin<N, NB, NM> {

    private final HTMLIFrameElement iFrame;

    public HtmlWebViewPeer() {
        this((NB) new EmulWebViewPeerBase(), HtmlUtil.createElement("iframe"));
    }

    public HtmlWebViewPeer(NB base, HTMLIFrameElement iFrame) {
        super(base, iFrame);
        this.iFrame = iFrame;
        iFrame.onerror = e -> {
            reportError();
            return null;
        };
    }

    private void reportError() {
        EventHandler<WebErrorEvent> onError = getNode().getEngine().getOnError();
        if (onError != null)
            onError.handle(new WebErrorEvent(this, WebErrorEvent.ANY, null, null));
    }

    @Override
    public void updateWidth(Number width) {
        iFrame.style.width = CSSProperties.WidthUnionType.of(toPx(width.doubleValue()));
    }

    @Override
    public void updateHeight(Number height) {
        iFrame.style.height = CSSProperties.HeightUnionType.of(toPx(height.doubleValue()));
    }

    @Override
    public void updateUrl(String url) {
        if (url != null) {
            // WebFX proposes different loading mode for the iFrame:
            Object webfxLoadingMode = getNode().getProperties().get("webfx-loadingMode");
            if ("prefetch".equals(webfxLoadingMode)) { // prefetch mode
                // For a better error reporting, we prefetch the url, and then inject the result into the iFrame, but
                // this alternative loading mode is not perfect either, as it may face security issue like CORS.
                iFrame.contentWindow.fetch(url)
                        .then(response -> {
                            response.text().then(text -> {
                                updateLoadContent(text);
                                return null;
                            }).catch_(error -> {
                                reportError();
                                return null;
                            });
                            return null;
                        }).catch_(error -> {
                            reportError();
                            return null;
                        });
            } else if ("replace".equals(webfxLoadingMode)) {
                // Using iframe location replace() instead of setting iFrame.src has the benefit to not interfere with
                // the parent window history (see explanation in standard loading mode below). However, it doesn't work
                // in all situations (ex: embed YouTube videos are not loading in this mode).
                iFrame.contentWindow.location.replace(url);
            } else { // Standard loading mode
                iFrame.src = url; // Standard way to load an iFrame
                // But it has 2 downsides (which is why webfx proposes alternative loading modes):
                // 1) it doesn't report any network errors (iFrame.onerror not called). Issue addressed by the webfx
                // "prefetch" mode
                // 2) it has a side effect on the parent window navigation when the url changes several times. The first
                // time has no side effect, but the subsequent times add an unwanted new entry in the parent window
                // history, so the user needs to click twice to go back to the previous page, instead of a single click.
                // Issue addressed by the webfx "replace" mode.
            }
        }
    }

    @Override
    public void updateLoadContent(String content) {
        if (content != null)
            iFrame.srcdoc = content;
    }
}
