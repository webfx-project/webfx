package dev.webfx.kit.mapper.peers.javafxmedia.spi;

import dev.webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;
import javafx.scene.media.MediaPlayer;

/**
 * @author Bruno Salmon
 */
public interface WebFxKitMediaMapperProvider {

    default MediaPlayerPeer createMediaPlayerPeer(MediaPlayer mediaPlayer, boolean audioClip) {
        return null;
    }

}
