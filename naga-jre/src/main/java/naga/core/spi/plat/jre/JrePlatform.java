package naga.core.spi.plat.jre;

import naga.core.spi.plat.Platform;
import naga.core.spi.plat.javaplat.JavaPlatform;

/**
 * @author Bruno Salmon
 */
public final class JrePlatform extends JavaPlatform {

    /**
     * Providing JrePlatform.register() method if needed but this explicit call is normally not necessary
     * as this platform is listed in META-INFO/services and can therefore be found by the ServiceLoader.
     */
    public static void register() {
        Platform.register(new JrePlatform());
    }

}
