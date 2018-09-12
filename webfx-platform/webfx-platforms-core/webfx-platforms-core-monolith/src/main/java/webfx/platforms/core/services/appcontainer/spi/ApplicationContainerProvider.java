package webfx.platforms.core.services.appcontainer.spi;

import webfx.platforms.core.services.appcontainer.ApplicationService;
import webfx.platforms.core.services.shutdown.Shutdown;

/**
 * @author Bruno Salmon
 */
public interface ApplicationContainerProvider {

    default void startApplicationService(ApplicationService applicationService) {
        applicationService.onStart();
        Shutdown.addShutdownHook(applicationService::onStop);
    }

}
