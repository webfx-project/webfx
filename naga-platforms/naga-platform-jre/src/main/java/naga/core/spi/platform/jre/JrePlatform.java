package naga.core.spi.platform.jre;

import naga.core.spi.platform.Platform;
import naga.core.spi.platform.client.java.JavaClientPlatform;

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
