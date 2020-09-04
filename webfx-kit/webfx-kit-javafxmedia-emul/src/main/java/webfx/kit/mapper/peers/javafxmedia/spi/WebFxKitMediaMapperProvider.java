package webfx.kit.mapper.peers.javafxmedia.spi;

import javafx.scene.media.Media;
import webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;

/**
 * @author Bruno Salmon
 */
public interface WebFxKitMediaMapperProvider {

    default MediaPlayerPeer createMediaPlayerPeer(Media media) {
        return null;
    }

}
