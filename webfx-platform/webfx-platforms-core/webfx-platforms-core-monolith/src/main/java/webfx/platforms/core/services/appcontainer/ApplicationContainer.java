package webfx.platforms.core.services.appcontainer;

import webfx.platforms.core.services.appcontainer.spi.ApplicationContainerProvider;
import webfx.platforms.core.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public final class ApplicationContainer {

    public static ApplicationContainerProvider getProvider() {
        return ServiceLoaderHelper.loadService(ApplicationContainerProvider.class);
    }

    public static void startApplicationService(ApplicationService applicationService) {
        getProvider().startApplicationService(applicationService);
    }

}
