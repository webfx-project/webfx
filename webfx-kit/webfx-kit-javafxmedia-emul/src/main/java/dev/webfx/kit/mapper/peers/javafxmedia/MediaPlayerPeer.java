package dev.webfx.kit.mapper.peers.javafxmedia;

/**
 * @author Bruno Salmon
 */
public interface MediaPlayerPeer {

    void setCycleCount(int cycleCount);

    void play();

    void pause();

    void stop();

    void setVolume(double volume);

}
