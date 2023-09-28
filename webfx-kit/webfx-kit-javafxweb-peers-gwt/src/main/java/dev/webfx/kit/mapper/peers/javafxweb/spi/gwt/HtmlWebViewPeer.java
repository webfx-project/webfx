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
            // iFrame.src = url; // <= commented because this doesn't report any network errors despite iFrame.onerror
            // For a better error reporting, we fetch the url ourselves, and then inject the result into the iFrame
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

    @Override
    public void updateLoadContent(String content) {
        if (content != null)
            iFrame.srcdoc = content;
    }
}
