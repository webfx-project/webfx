package javafx.scene.media;

/**
 * @author Bruno Salmon
 */
public class AudioClip {

    public static final int INDEFINITE = -1; // Note: this is a count, not a Duration.

    private final MediaPlayer mediaPlayer;
    private double volume = 1;
    private boolean playing;

    public AudioClip(String url) {
        mediaPlayer = new MediaPlayer(new Media(url), true);
    }

    public void play() {
        mediaPlayer.play();
        playing = true;
        mediaPlayer.setOnEndOfMedia(() -> playing = false);
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void setVolume(double volume) {
        this.volume = volume;
        mediaPlayer.setVolume(volume);
    }

    public double getVolume() {
        return volume;
    }

    public void setCycleCount(int cycleCount) {
        mediaPlayer.setCycleCount(cycleCount);
    }

    public int getCycleCount() {
        return mediaPlayer.getCycleCount();
    }

    public boolean isPlaying() {
        return playing;
    }
}
