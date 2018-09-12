package webfx.platforms.core.services.appcontainer;

import webfx.platforms.core.services.appcontainer.spi.ApplicationContainerProvider;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class ApplicationContainer {

    public static ApplicationContainerProvider getProvider() {
        return SingleServiceLoader.loadService(ApplicationContainerProvider.class);
    }

    public static void startApplicationService(ApplicationService applicationService) {
        getProvider().startApplicationService(applicationService);
    }

}
