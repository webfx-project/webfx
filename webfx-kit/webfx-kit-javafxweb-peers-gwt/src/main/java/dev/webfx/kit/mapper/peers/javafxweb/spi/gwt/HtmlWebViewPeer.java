package dev.webfx.kit.mapper.peers.javafxweb.spi.gwt;

import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.platform.util.Booleans;
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
            // In general, we use iFrame.src, but it doesn't report any network errors despite iFrame.onerror, which can
            // be annoying in some cases. So we propose a prefetch alternative mode.
            boolean usePrefetch = Booleans.isTrue(getNode().getProperties().get("webfx-prefetch"));
            if (!usePrefetch) {
                // Commented the following code (iFrame.src = url) because it has a side effect on the parent window
                // navigation when the url changes several times. The first time has no side effect, but the subsequent
                // times add an unwanted new entry in the parent window history, so the user needs to click twice to go
                // back to the previous page, instead of a single click.
                //iFrame.src = url;

                // Using the iframe location replace() method seems to work better without that side effect explained above.
                iFrame.contentWindow.location.replace(url);
            } else {
                // For a better error reporting, we prefetch the url, and then inject the result into the iFrame, but
                // this second method is not perfect either, as it may face security issue like CORS.
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
            }
        }
    }

    @Override
    public void updateLoadContent(String content) {
        if (content != null)
            iFrame.srcdoc = content;
    }
}
