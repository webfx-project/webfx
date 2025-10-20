package dev.webfx.kit.platform.audio.spi.impl.openjfxweb;

import dev.webfx.platform.audio.Audio;
import dev.webfx.platform.audio.spi.AudioServiceProvider;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

/**
 * @author Bruno Salmon
 */
public final class OpenJFXWebAudioServiceProvider implements AudioServiceProvider {

    @Override
    public Audio loadSound(String url) {
        return new OpenJFXWebSound(new AudioClip(url));
    }

    @Override
    public Audio loadMusic(String url) {
        return new OpenJFXWebMusic(new Media(url));
    }

    @Override
    public boolean supportsMusicSpectrumAnalysis() {
        return true;
    }
}
