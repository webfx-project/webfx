package dev.webfx.kit.mapper.peers.javafxmedia;

import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;

import java.time.Duration;

/**
 * @author Bruno Salmon
 */
public interface MediaPlayerPeer {

    Media getMedia();

    void setCycleCount(int cycleCount);

    void play();

    void pause();

    void stop();

    void setVolume(double volume);

    Duration getCurrentTime();

    void setAudioSpectrumInterval(double value);

    void setAudioSpectrumNumBands(int value);

    void setAudioSpectrumListener(AudioSpectrumListener listener);

}
