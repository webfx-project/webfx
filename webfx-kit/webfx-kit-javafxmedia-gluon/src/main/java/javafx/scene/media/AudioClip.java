package javafx.scene.media;

import dev.webfx.platform.audio.Audio;
import dev.webfx.platform.audio.AudioService;

public class AudioClip {

    public static final int INDEFINITE = -1; // Note: this is a count, not a Duration.
    private final Audio audio;

    public AudioClip(String url) {
        this.audio = AudioService.loadSound(url);
    }

    public void setVolume(double volume) {
        audio.setVolume(volume);
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

    public void setCycleCount(int cycleCount) {
        audio.setCycleCount(cycleCount);
    }

    public boolean isPlaying() {
        return audio.isPlaying();
    }

}
