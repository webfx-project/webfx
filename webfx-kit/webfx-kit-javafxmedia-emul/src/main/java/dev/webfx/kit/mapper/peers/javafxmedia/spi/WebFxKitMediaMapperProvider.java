package dev.webfx.kit.mapper.peers.javafxmedia.spi;

import dev.webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;

/**
 * @author Bruno Salmon
 */
public interface WebFxKitMediaMapperProvider {

    default MediaPlayerPeer createMediaPlayerPeer(String mediaUrl, boolean audioClip) {
        return null;
    }

}
