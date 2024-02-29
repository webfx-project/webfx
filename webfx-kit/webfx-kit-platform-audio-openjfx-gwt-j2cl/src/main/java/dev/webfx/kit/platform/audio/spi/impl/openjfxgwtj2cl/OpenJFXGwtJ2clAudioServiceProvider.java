package dev.webfx.kit.platform.audio.spi.impl.openjfxgwtj2cl;

import dev.webfx.platform.audio.Audio;
import dev.webfx.platform.audio.spi.AudioServiceProvider;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

/**
 * @author Bruno Salmon
 */
public final class OpenJFXGwtJ2clAudioServiceProvider implements AudioServiceProvider {

    @Override
    public Audio loadSound(String url) {
        return new OpenJFXGwtJ2clSound(new AudioClip(url));
    }

    @Override
    public Audio loadMusic(String url) {
        return new OpenJFXGwtJ2clMusic(new Media(url));
    }

    @Override
    public boolean supportsMusicSpectrumAnalysis() {
        return true;
    }
}
