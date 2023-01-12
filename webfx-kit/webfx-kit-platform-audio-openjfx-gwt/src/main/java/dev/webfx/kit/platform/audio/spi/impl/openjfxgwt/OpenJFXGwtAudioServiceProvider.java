package dev.webfx.kit.platform.audio.spi.impl.openjfxgwt;

import dev.webfx.platform.audio.Audio;
import dev.webfx.platform.audio.spi.AudioServiceProvider;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

/**
 * @author Bruno Salmon
 */
public final class OpenJFXGwtAudioServiceProvider implements AudioServiceProvider {

    @Override
    public Audio loadSound(String url) {
        return new OpenJFXGwtSound(new AudioClip(url));
    }

    @Override
    public Audio loadMusic(String url) {
        return new OpenJFXGwtMusic(new Media(url));
    }

    @Override
    public boolean supportsMusicSpectrumAnalysis() {
        return true;
    }
}
