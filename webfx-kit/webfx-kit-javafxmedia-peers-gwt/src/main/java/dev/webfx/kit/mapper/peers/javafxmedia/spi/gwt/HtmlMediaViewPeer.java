package dev.webfx.kit.mapper.peers.javafxmedia.spi.gwt;

import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.HasSizeChangedCallback;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutMeasurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLVideoElement;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * @author Bruno Salmon
 */
public class HtmlMediaViewPeer
        <E extends Element, N extends MediaView, NB extends NodePeerBase<N, NB, NM>, NM extends NodePeerMixin<N, NB, NM>>
        extends HtmlNodePeer<N, NB, NM>
        implements HtmlLayoutMeasurable, HasSizeChangedCallback {

    private final HTMLVideoElement videoElement;
    private boolean loaded;
    public HtmlMediaViewPeer() {
        this((NB) new NodePeerBase(), HtmlUtil.createElement("video"));
    }

    public HtmlMediaViewPeer(NB base, HTMLElement element) {
        super(base, element);
        videoElement = (HTMLVideoElement) getElement();
        videoElement.controls = false; // There is no controls in JavaFX MediaView
        videoElement.onloadedmetadata = p0 -> {
            loaded = true;
            if (sizeChangedCallback != null)
                sizeChangedCallback.run();
            return null;
        };
    }

    private Runnable sizeChangedCallback;
    @Override
    public void setSizeChangedCallback(Runnable sizeChangedCallback) {
        this.sizeChangedCallback = sizeChangedCallback;
     }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        N mediaView = getNode();
        MediaPlayer mediaPlayer = mediaView.getMediaPlayer();
        GwtMediaPlayerPeer peer = (GwtMediaPlayerPeer) mediaPlayer.getPeer();
        peer.setMediaElement(videoElement);
    }

    // Emulating the JavaFX API behaviour where min/pref/max width/height always returns the video min/height

    @Override
    public double minWidth(double height) {
        return getVideoWidth();
    }

    @Override
    public double prefWidth(double height) {
        return getVideoWidth();
    }

    @Override
    public double maxWidth(double height) {
        return getVideoWidth();
    }

    private double getVideoWidth() {
        return loaded ? videoElement.videoWidth : videoElement.offsetWidth;
    }

    @Override
    public double minHeight(double width) {
        return getVideoHeight();
    }

    @Override
    public double prefHeight(double width) {
        return getVideoHeight();
    }

    @Override
    public double maxHeight(double width) {
        return getVideoHeight();
    }

    private double getVideoHeight() {
        return loaded ? videoElement.videoHeight : videoElement.offsetHeight;
    }

    @Override
    public double measure(HTMLElement e, boolean width) { // Important correction of measure() for FireFox where offsetWidth/Height is not immediately equals to videoWidth/Height
        return width ? getVideoWidth() : getVideoHeight();
    }
}
