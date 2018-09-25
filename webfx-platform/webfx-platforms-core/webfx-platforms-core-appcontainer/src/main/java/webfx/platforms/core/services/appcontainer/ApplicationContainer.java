package webfx.platforms.core.services.appcontainer;

import webfx.platforms.core.services.appcontainer.spi.ApplicationContainerProvider;
import webfx.platforms.core.services.appcontainer.spi.ApplicationJob;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class ApplicationContainer {

    public static ApplicationContainerProvider getProvider() {
        return SingleServiceLoader.loadService(ApplicationContainerProvider.class);
    }

    public static void startApplicationJob(ApplicationJob applicationJob) {
        getProvider().startApplicationJob(applicationJob);
    }

    public static void stoptApplicationJob(ApplicationJob applicationJob) {
        getProvider().stopApplicationJob(applicationJob);
    }

    private static String[] mainArgs;

    public static String[] getMainArgs() {
        return mainArgs;
    }

    public static void start(ApplicationContainerProvider provider, String[] mainArgs) {
        // Caching this instance to make the ApplicationContainer work
        SingleServiceLoader.cacheServiceInstance(ApplicationContainerProvider.class, provider);
        start(mainArgs);
    }

    public static void start(String[] mainArgs) {
        ApplicationContainer.mainArgs = mainArgs;
        getProvider().initialize();
    }

    public static void main(String[] args) {
        start(mainArgs);
    }
}
