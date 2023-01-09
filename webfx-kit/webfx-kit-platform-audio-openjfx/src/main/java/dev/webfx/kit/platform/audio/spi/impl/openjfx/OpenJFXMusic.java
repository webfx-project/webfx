package dev.webfx.kit.platform.audio.spi.impl.openjfx;

import dev.webfx.platform.audio.Audio;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @author Bruno Salmon
 */
public class OpenJFXMusic implements Audio {

    private MediaPlayer mediaPlayer;

    public OpenJFXMusic(Media media) {
        mediaPlayer = new MediaPlayer(media);
    }

    @Override
    public void setLooping(boolean looping) {
        mediaPlayer.setCycleCount(looping ? MediaPlayer.INDEFINITE : 1);
    }

    @Override
    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    @Override
    public void play() {
        mediaPlayer.play();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
    }

    @Override
    public void dispose() {
        mediaPlayer.dispose();
        mediaPlayer = null;
    }

    @Override
    public boolean isDisposed() {
        return mediaPlayer == null;
    }
}
