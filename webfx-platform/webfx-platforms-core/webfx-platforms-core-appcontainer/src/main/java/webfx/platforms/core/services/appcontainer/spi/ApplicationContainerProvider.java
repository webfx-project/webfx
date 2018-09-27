package webfx.platforms.core.services.appcontainer.spi;

import webfx.platforms.core.services.appcontainer.spi.impl.ApplicationModuleInitializerManager;

/**
 * @author Bruno Salmon
 */
public interface ApplicationContainerProvider {

    default void initialize() {
        ApplicationModuleInitializerManager.initialize();
    }

    default void startApplicationJob(ApplicationJob applicationJob) {
        applicationJob.onStartAsync();
    }

    default void stopApplicationJob(ApplicationJob applicationJob) {
        applicationJob.onStopAsync();
    }

}
