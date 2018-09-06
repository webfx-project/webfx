package webfx.platforms.core.services.shutdown;

import webfx.platforms.core.services.shutdown.spi.ShutdownProvider;
import webfx.platforms.core.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class Shutdown {

    public static ShutdownProvider getProvider() {
        return ServiceLoaderHelper.loadService(ShutdownProvider.class);
    }

    public static void addShutdownHook(Runnable hook) {
        getProvider().addShutdownHook(hook);
    }
}
