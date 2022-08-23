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
    private boolean explicitVolume;

    public GwtMediaPlayerPeer(Media media) {
        this.media = media;
        audio = (HTMLAudioElement) DomGlobal.document.createElement("audio");
        // Trick to make play() work on iOS & iPad (from https://stackoverflow.com/questions/31776548/why-cant-javascript-play-audio-files-on-iphone-safari)
        audio.autoplay = true;
        audio.src = "data:audio/mpeg;base64,SUQzBAAAAAABEVRYWFgAAAAtAAADY29tbWVudABCaWdTb3VuZEJhbmsuY29tIC8gTGFTb25vdGhlcXVlLm9yZwBURU5DAAAAHQAAA1N3aXRjaCBQbHVzIMKpIE5DSCBTb2Z0d2FyZQBUSVQyAAAABgAAAzIyMzUAVFNTRQAAAA8AAANMYXZmNTcuODMuMTAwAAAAAAAAAAAAAAD/80DEAAAAA0gAAAAATEFNRTMuMTAwVVVVVVVVVVVVVUxBTUUzLjEwMFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVf/zQsRbAAADSAAAAABVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVf/zQMSkAAADSAAAAABVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV";
    }

    @Override
    public void setCycleCount(int cycleCount) {
        audio.loop = cycleCount == -1;
    }

    @Override
    public void play() {
        audio.src = media.getUrl();
        audio.muted = false;
        if (!explicitVolume)
            audio.volume = 1;
        audio.play();
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
        explicitVolume = true;
    }
}
