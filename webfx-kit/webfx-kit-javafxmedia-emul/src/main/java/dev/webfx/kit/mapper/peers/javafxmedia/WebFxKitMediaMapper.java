package dev.webfx.kit.mapper.peers.javafxmedia;

import dev.webfx.kit.mapper.peers.javafxmedia.spi.WebFxKitMediaMapperProvider;
import dev.webfx.platform.service.SingleServiceProvider;
import javafx.scene.media.MediaPlayer;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class WebFxKitMediaMapper {

    private static WebFxKitMediaMapperProvider getProvider() {
        return SingleServiceProvider.getProvider(WebFxKitMediaMapperProvider.class, () -> ServiceLoader.load(WebFxKitMediaMapperProvider.class));
    }

    public static MediaPlayerPeer createMediaPlayerPeer(MediaPlayer mediaPlayer, boolean audioClip) {
        return getProvider().createMediaPlayerPeer(mediaPlayer, audioClip);
    }
}
