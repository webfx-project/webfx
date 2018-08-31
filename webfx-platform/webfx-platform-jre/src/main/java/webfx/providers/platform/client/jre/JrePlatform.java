package webfx.providers.platform.client.jre;

import webfx.platform.spi.Platform;
import webfx.providers.platform.abstr.java.client.JavaClientPlatform;

/**
 * @author Bruno Salmon
 */
public final class JrePlatform extends JavaClientPlatform {

    /**
     * Providing JrePlatform.register() method if needed but this explicit call is normally not necessary
     * as this platform is listed in META-INFO/services and can therefore be found by the ServiceLoader.
     */
    public static void register() {
        Platform.register(new JrePlatform());
    }

}
