package dev.webfx.kit.mapper.peers.javafxmedia;

import javafx.scene.media.AudioSpectrumListener;

import java.time.Duration;

/**
 * @author Bruno Salmon
 */
public interface MediaPlayerPeer {

    void setCycleCount(int cycleCount);

    void play();

    void pause();

    void stop();

    void dispose();

    void setVolume(double volume);

    void setMute(boolean mute);

    Duration getCurrentTime();

    void setAudioSpectrumInterval(double value);

    void setAudioSpectrumNumBands(int value);

    void setAudioSpectrumListener(AudioSpectrumListener listener);

    void setOnEndOfMedia(Runnable onEndOfMedia);

    void setOnPlaying(Runnable onPlaying);

}
