package javafx.scene.media;

/**
 * @author Bruno Salmon
 */
public class AudioClip {

    private final Media media;
    private MediaPlayer mediaPlayer;
    private double volume = -1;

    public AudioClip(String url) {
        media = new Media(url);
        mediaPlayer = new MediaPlayer(media);
    }

    public void play() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer(media);
        if (volume >= 0)
            mediaPlayer.setVolume(volume);
        mediaPlayer.play();
        mediaPlayer = null;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

}
