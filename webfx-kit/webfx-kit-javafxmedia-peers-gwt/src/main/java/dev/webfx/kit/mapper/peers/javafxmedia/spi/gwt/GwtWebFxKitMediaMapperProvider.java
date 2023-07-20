package dev.webfx.kit.mapper.peers.javafxmedia.spi.gwt;

import dev.webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;
import dev.webfx.kit.mapper.peers.javafxmedia.spi.WebFxKitMediaMapperProvider;
import javafx.scene.media.MediaPlayer;

/**
 * @author Bruno Salmon
 */
public final class GwtWebFxKitMediaMapperProvider implements WebFxKitMediaMapperProvider {

    @Override
    public MediaPlayerPeer createMediaPlayerPeer(MediaPlayer mediaPlayer, boolean audioClip) {
        return new GwtMediaPlayerPeer(mediaPlayer, audioClip);
    }

}
