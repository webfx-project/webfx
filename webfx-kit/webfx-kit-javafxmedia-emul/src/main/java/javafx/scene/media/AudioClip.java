package javafx.scene.media;

/**
 * @author Bruno Salmon
 */
public class AudioClip {

    public static final int INDEFINITE = -1; // Note: this is a count, not a Duration.

    private final Media media;
    private MediaPlayer mediaPlayer;
    private double volume = 1;
    private int cycleCount = 1;

    public AudioClip(String url) {
        media = new Media(url);
    }

    private MediaPlayer getOrCreateMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer(media);
            applySettings();
        }
        return mediaPlayer;
    }

    private void applySettings() {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
            mediaPlayer.setCycleCount(cycleCount);
        }
    }

    public void play() {
        getOrCreateMediaPlayer().play();
        mediaPlayer = null;
    }

    public void stop() {
        if (mediaPlayer != null)
            mediaPlayer.stop();
    }

    public void setVolume(double volume) {
        this.volume = volume;
        applySettings();
    }

    public double getVolume() {
        return volume;
    }

    public void setCycleCount(int cycleCount) {
        this.cycleCount = cycleCount;
        applySettings();
    }

    public int getCycleCount() {
        return cycleCount;
    }

}
