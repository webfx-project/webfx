package dev.webfx.kit.mapper.peers.javafxmedia;

import javafx.scene.media.Media;
import dev.webfx.kit.mapper.peers.javafxmedia.spi.WebFxKitMediaMapperProvider;
import dev.webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class WebFxKitMediaMapper {

    private static WebFxKitMediaMapperProvider getProvider() {
        return SingleServiceProvider.getProvider(WebFxKitMediaMapperProvider.class, () -> ServiceLoader.load(WebFxKitMediaMapperProvider.class));
    }

    public static MediaPlayerPeer createMediaPlayerPeer(Media media) {
        return getProvider().createMediaPlayerPeer(media);
    }
}
