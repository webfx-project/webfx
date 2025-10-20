package dev.webfx.kit.mapper.peers.javafxmedia.spi.elemental2;

import dev.webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;
import dev.webfx.kit.mapper.peers.javafxmedia.spi.WebFxKitMediaMapperProvider;
import javafx.scene.media.MediaPlayer;

/**
 * @author Bruno Salmon
 */
public final class Elemental2WebFxKitMediaMapperProvider implements WebFxKitMediaMapperProvider {

    @Override
    public MediaPlayerPeer createMediaPlayerPeer(MediaPlayer mediaPlayer, boolean audioClip) {
        return new Elemental2MediaPlayerPeer(mediaPlayer, audioClip);
    }

}
