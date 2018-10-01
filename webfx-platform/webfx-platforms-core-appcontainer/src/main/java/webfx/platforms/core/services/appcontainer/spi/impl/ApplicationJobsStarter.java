package webfx.platforms.core.services.appcontainer.spi.impl;

import webfx.platforms.core.services.appcontainer.ApplicationContainer;
import webfx.platforms.core.services.appcontainer.spi.ApplicationJob;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.util.collection.Collections;

import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class ApplicationJobsStarter implements ApplicationModuleInitializer {

    private List<ApplicationJob> providedJobs; // Not initialized here as it's not the good time (calling lower module initializer before)

    @Override
    public String getModuleName() {
        return "webfx-platforms-core-appcontainer (jobs starter)";
    }

    @Override
    public int getInitLevel() {
        return JOBS_START_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        providedJobs = Collections.listOf(ServiceLoader.load(ApplicationJob.class));
        Logger.log(providedJobs.size() + " provided application jobs");
        providedJobs.forEach(job -> {
            Logger.log("Starting " + job.getClass().getSimpleName());
            ApplicationContainer.startApplicationJob(job);
        });
    }

    @Override
    public void exitModule() {
        providedJobs.forEach(job -> {
            Logger.log("Stopping " + job.getClass().getSimpleName());
            ApplicationContainer.stopApplicationJob(job);
        });
    }
}
