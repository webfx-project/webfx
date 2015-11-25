package naga.spi.plat.jre;

import naga.core.spi.plat.Platform;
import naga.core.spi.plat.javaplat.JavaPlatform;

/**
 * @author Bruno Salmon
 */
public final class JrePlatform extends JavaPlatform {

    /**
     * Registers the Java platform with a default configuration.
     */
    public static void register() {
        Platform.register(new JrePlatform());
    }

}
