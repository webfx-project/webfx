package dev.webfx.kit.mapper.peers.javafxmedia;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.media.AudioSpectrumListener;
import javafx.util.Duration;

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

    ObjectProperty<Duration> mediaPlayerCurrentTimeProperty();

    BooleanProperty muteProperty();

    void setAudioSpectrumInterval(double value);

    void setAudioSpectrumNumBands(int value);

    void setAudioSpectrumListener(AudioSpectrumListener listener);

    void seek(Duration duration);

}
