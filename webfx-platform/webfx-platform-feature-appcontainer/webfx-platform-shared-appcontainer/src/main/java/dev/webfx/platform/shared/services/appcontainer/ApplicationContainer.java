package dev.webfx.platform.shared.services.appcontainer;

import dev.webfx.platform.shared.services.appcontainer.spi.ApplicationContainerProvider;
import dev.webfx.platform.shared.services.appcontainer.spi.ApplicationJob;
import dev.webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class ApplicationContainer {

    public static ApplicationContainerProvider getProvider() {
        return SingleServiceProvider.getProvider(ApplicationContainerProvider.class, () -> ServiceLoader.load(ApplicationContainerProvider.class));
    }

    public static void startApplicationJob(ApplicationJob applicationJob) {
        getProvider().startApplicationJob(applicationJob);
    }

    public static void stopApplicationJob(ApplicationJob applicationJob) {
        getProvider().stopApplicationJob(applicationJob);
    }

    private static String[] mainArgs;

    public static String[] getMainArgs() {
        return mainArgs;
    }

    public static void start(ApplicationContainerProvider provider, String[] mainArgs) {
        // Caching this instance to make the ApplicationContainer work
        SingleServiceProvider.registerServiceProvider(ApplicationContainerProvider.class, provider);
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
