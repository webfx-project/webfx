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
}
