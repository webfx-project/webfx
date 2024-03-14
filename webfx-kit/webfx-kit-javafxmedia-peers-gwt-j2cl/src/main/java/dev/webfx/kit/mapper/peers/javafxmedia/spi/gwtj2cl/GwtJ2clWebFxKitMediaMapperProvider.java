package dev.webfx.kit.mapper.peers.javafxmedia.spi.gwtj2cl;

import dev.webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;
import dev.webfx.kit.mapper.peers.javafxmedia.spi.WebFxKitMediaMapperProvider;
import javafx.scene.media.MediaPlayer;

/**
 * @author Bruno Salmon
 */
public final class GwtJ2clWebFxKitMediaMapperProvider implements WebFxKitMediaMapperProvider {

    @Override
    public MediaPlayerPeer createMediaPlayerPeer(MediaPlayer mediaPlayer, boolean audioClip) {
        return new GwtJ2clMediaPlayerPeer(mediaPlayer, audioClip);
    }

}
