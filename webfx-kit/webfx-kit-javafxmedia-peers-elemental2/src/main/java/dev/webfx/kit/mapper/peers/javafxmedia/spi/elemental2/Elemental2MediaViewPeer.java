package dev.webfx.kit.mapper.peers.javafxmedia.spi.elemental2;

import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.NodePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.HasSizeChangedCallback;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.HtmlNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.layoutmeasurable.HtmlMeasurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLMediaElement;
import elemental2.dom.HTMLVideoElement;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * @author Bruno Salmon
 */
public class Elemental2MediaViewPeer
        <N extends MediaView, NB extends NodePeerBase<N, NB, NM>, NM extends NodePeerMixin<N, NB, NM>>
        extends HtmlNodePeer<N, NB, NM>
        implements HtmlMeasurable, HasSizeChangedCallback {

    private final HTMLVideoElement videoElement;
    private boolean loaded;
    public Elemental2MediaViewPeer() {
        this((NB) new NodePeerBase(), HtmlUtil.createElement("video"));
    }

    public Elemental2MediaViewPeer(NB base, HTMLElement element) {
        super(base, element);
        videoElement = (HTMLVideoElement) getElement();
        videoElement.controls = false; // There is no controls in JavaFX MediaView
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
        if (mediaPlayer != null) {
            Elemental2MediaPlayerPeer peer = (Elemental2MediaPlayerPeer) mediaPlayer.getPeer();
            peer.setMediaElement(videoElement); // This set videoElement listeners, including onloadedmetadata
        }
        // In addition, we would like to notify the size change which happens when metadata is loaded, but adding a
        // listener through videoElement.addEventListener("onloadedmetadata") doesn't work (it's never called).
        // So we redefine videoElement.onloadedmetadata but call the listener initially set by GwtMediaPlayerPeer.
        HTMLMediaElement.OnloadedmetadataFn mediaPlayerOnloadedmetadata = videoElement.onloadedmetadata;
        videoElement.onloadedmetadata = p0 -> {
            loaded = true;
            if (sizeChangedCallback != null)
                sizeChangedCallback.run();
            return mediaPlayerOnloadedmetadata.onInvoke(p0);
        };
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
    public double measureElement(HTMLElement e, boolean measureWidth) { // Important correction of measure() for FireFox where offsetWidth/Height is not immediately equals to videoWidth/Height
        return measureWidth ? getVideoWidth() : getVideoHeight();
    }
}
