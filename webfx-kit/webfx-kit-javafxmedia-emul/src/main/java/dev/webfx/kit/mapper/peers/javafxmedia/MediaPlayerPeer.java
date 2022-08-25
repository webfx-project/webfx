package dev.webfx.kit.mapper.peers.javafxmedia;

import java.time.Duration;

/**
 * @author Bruno Salmon
 */
public interface MediaPlayerPeer {

    void setCycleCount(int cycleCount);

    void play();

    void pause();

    void stop();

    void setVolume(double volume);

    Duration getCurrentTime();
}
