package webfx.platforms.core.services.appcontainer.spi.impl;

import webfx.platforms.core.services.appcontainer.ApplicationContainer;
import webfx.platforms.core.services.appcontainer.spi.ApplicationJob;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platforms.core.util.collection.Collections;

import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class ApplicationJobsInitializer implements ApplicationModuleInitializer {

    private List<ApplicationJob> providedJobs; // Not initialized here as it's not the good timing

    @Override
    public String getModuleName() {
        return "webfx-platforms-core-appcontainer (starting provided application jobs)";
    }

    @Override
    public int getInitLevel() {
        return JOBS_START_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        providedJobs = Collections.listOf(ServiceLoader.load(ApplicationJob.class));
        providedJobs.forEach(ApplicationContainer::startApplicationJob);
    }

    @Override
    public void exitModule() {
        providedJobs.forEach(ApplicationContainer::stoptApplicationJob);
    }
}
