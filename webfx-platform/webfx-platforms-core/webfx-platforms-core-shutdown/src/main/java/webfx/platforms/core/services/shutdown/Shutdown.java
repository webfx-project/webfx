package webfx.platforms.core.services.shutdown;

import webfx.platforms.core.services.shutdown.spi.ShutdownProvider;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public class Shutdown {

    public static ShutdownProvider getProvider() {
        return SingleServiceLoader.loadService(ShutdownProvider.class);
    }

    public static void addShutdownHook(Runnable hook) {
        getProvider().addShutdownHook(hook);
    }
}
