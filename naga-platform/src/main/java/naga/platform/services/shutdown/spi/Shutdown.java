package naga.platform.services.shutdown.spi;

import naga.util.serviceloader.ServiceLoaderHelper;

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
