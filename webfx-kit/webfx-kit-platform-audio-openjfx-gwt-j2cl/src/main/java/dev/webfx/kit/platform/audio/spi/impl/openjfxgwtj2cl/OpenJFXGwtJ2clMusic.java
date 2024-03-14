package dev.webfx.kit.platform.audio.spi.impl.openjfxgwtj2cl;

import dev.webfx.platform.audio.Audio;
import dev.webfx.platform.audio.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @author Bruno Salmon
 */
public class OpenJFXGwtJ2clMusic implements Audio {

    private MediaPlayer mediaPlayer;
    private boolean playing;

    public OpenJFXGwtJ2clMusic(Media media) {
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
        playing = true;
        mediaPlayer.setOnEndOfMedia(() -> playing = false);
        mediaPlayer.play();
    }

    @Override
    public void pause() {
        playing = false;
        mediaPlayer.pause();
    }

    @Override
    public void stop() {
        playing = false;
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

    @Override
    public void setCycleCount(int cycleCount) {
        mediaPlayer.setCycleCount(cycleCount);
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public long getCurrentTimeMillis() {
        return (long) mediaPlayer.getCurrentTime().toMillis();
    }

    @Override
    public boolean supportsSpectrumAnalysis() {
        return true;
    }

    @Override
    public void setAudioSpectrumInterval(double value) {
        mediaPlayer.setAudioSpectrumInterval(value);
    }

    @Override
    public void setAudioSpectrumNumBands(int value) {
        mediaPlayer.setAudioSpectrumNumBands(value);
    }

    @Override
    public void setAudioSpectrumListener(AudioSpectrumListener listener) {
        mediaPlayer.setAudioSpectrumListener(listener::spectrumDataUpdate);
    }

}
