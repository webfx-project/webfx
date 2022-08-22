package dev.webfx.kit.mapper.peers.javafxmedia.spi.gwt;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLAudioElement;
import javafx.scene.media.Media;
import dev.webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;

/**
 * @author Bruno Salmon
 */
final class GwtMediaPlayerPeer implements MediaPlayerPeer {

    private final HTMLAudioElement audio;
    private final Media media;

    public GwtMediaPlayerPeer(Media media) {
        audio = (HTMLAudioElement) DomGlobal.document.createElement("audio");
        this.media = media;
        //audio.src = media.getUrl();
        //audio.load();
    }

    @Override
    public void setCycleCount(int cycleCount) {
        audio.loop = cycleCount == -1;
    }

    @Override
    public void play() {
        audio.src = media.getUrl();
        //audio.play(); // Commented as is not enough on iPad
        // To make it work on iPad, we first call load()
        audio.load();
        // And then play() whenever possible
        audio.oncanplaythrough = e -> audio.play();
    }

    @Override
    public void pause() {
        audio.pause();
    }

    @Override
    public void stop() {
        audio.pause();
        audio.currentTime = 0;
    }

    @Override
    public void setVolume(double volume) {
        audio.volume = volume;
    }
}
