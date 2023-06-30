package javafx.scene.media;

import dev.webfx.platform.audio.Audio;
import dev.webfx.platform.audio.AudioService;

/**
 * @author Bruno Salmon
 */
public class MediaPlayer {

    public static final int INDEFINITE = -1; // Note: this is a count, not a Duration.

    private final Media media;
    private final Audio audio;

    public MediaPlayer(Media media) {
        this.media = media;
        audio = AudioService.loadMusic(media.getSource());
    }

    public Media getMedia() {
        return media;
    }

    public void setCycleCount(int cycleCount) {
        audio.setCycleCount(cycleCount);
    }

    public void play() {
        audio.play();
    }

    public void pause() {
        audio.pause();
    }

    public void stop() {
        audio.stop();
    }

    public void dispose() {
        audio.dispose();
    }

    public void setVolume(double volume) {
        audio.setVolume(volume);
    }

}
