package dev.webfx.kit.platform.audio.spi.impl.openjfx;

import dev.webfx.platform.audio.Audio;
import dev.webfx.platform.audio.spi.AudioServiceProvider;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

/**
 * @author Bruno Salmon
 */
public final class OpenJFXAudioServiceProvider implements AudioServiceProvider {

    @Override
    public Audio loadSound(String url) {
        return new OpenJFXSound(new AudioClip(url));
    }

    @Override
    public Audio loadMusic(String url) {
        return new OpenJFXMusic(new Media(url));
    }
}
