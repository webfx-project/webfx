package javafx.scene.media;

import dev.webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;
import dev.webfx.kit.mapper.peers.javafxmedia.WebFxKitMediaMapper;

import java.time.Duration;

/**
 * @author Bruno Salmon
 */
public class MediaPlayer {

    private final MediaPlayerPeer peer;

    public MediaPlayer(Media media) {
        peer = WebFxKitMediaMapper.createMediaPlayerPeer(media);
    }

    public Media getMedia() {
        return peer.getMedia();
    }

    public void setCycleCount(int cycleCount) {
        peer.setCycleCount(cycleCount);
    }

    public void play() {
        peer.play();
    }

    public void pause() {
        peer.pause();
    }

    public void stop() {
        peer.stop();
    }

    public void setVolume(double volume) {
        peer.setVolume(volume);
    }

    public final Duration getCurrentTime() {
        return peer.getCurrentTime();
    }

    /**
     * Sets the value of the audio spectrum notification interval in seconds.
     * @param value a positive value specifying the spectral update interval
     */
    public final void setAudioSpectrumInterval(double value) {
        peer.setAudioSpectrumInterval(value);
    }

    /**
     * Sets the number of bands in the audio spectrum.
     * @param value the number of spectral bands; <code>value</code>must be &ge; 2
     */
    public final void setAudioSpectrumNumBands(int value) {
        peer.setAudioSpectrumNumBands(value);
    }


    public final void setAudioSpectrumListener(AudioSpectrumListener listener) {
        peer.setAudioSpectrumListener(listener);
    }

}
