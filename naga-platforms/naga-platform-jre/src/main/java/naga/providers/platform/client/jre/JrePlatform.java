package naga.providers.platform.client.jre;

import naga.platform.spi.Platform;
import naga.providers.platform.abstr.java.client.JavaClientPlatform;

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
