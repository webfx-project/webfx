package webfx.platforms.core.services.shutdown;

import webfx.platforms.core.services.shutdown.spi.ShutdownProvider;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class Shutdown {

    public static ShutdownProvider getProvider() {
        return SingleServiceLoader.loadService(ShutdownProvider.class);
    }

    public static void addShutdownHook(Runnable hook) {
        getProvider().addShutdownHook(hook);
    }

    public static void removeShutdownHook(Runnable hook) {
        getProvider().removeShutdownHook(hook);
    }

    public static void softwareShutdown(boolean exit, int exitStatus) {
        getProvider().softwareShutdown(exit, exitStatus);
    }

    public static boolean isShuttingDown() {
        return getProvider().isShuttingDown();
    }

    public static boolean isSoftwareShutdown() {
        return getProvider().isSoftwareShutdown();
    }
}
