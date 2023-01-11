package dev.webfx.kit.platform.audio.spi.impl.openjfxgwt;

import dev.webfx.platform.audio.Audio;
import javafx.scene.media.AudioClip;

/**
 * @author Bruno Salmon
 */
final class OpenJFXGwtSound implements Audio {

    private AudioClip audioClip;
    private long startTime;

    public OpenJFXGwtSound(AudioClip audioClip) {
        this.audioClip = audioClip;
    }

    @Override
    public void setLooping(boolean looping) {
        audioClip.setCycleCount(looping ? AudioClip.INDEFINITE : 1);
    }

    @Override
    public void setVolume(double volume) {
        audioClip.setVolume(volume);
    }

    @Override
    public void play() {
        audioClip.play();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void pause() {
        audioClip.stop();
    }

    @Override
    public void stop() {
        audioClip.stop();
    }

    @Override
    public void dispose() {
        audioClip = null;
    }

    @Override
    public boolean isDisposed() {
        return audioClip == null;
    }

    @Override
    public long getCurrentTimeMillis() {
        return System.currentTimeMillis() - startTime;
    }
}
