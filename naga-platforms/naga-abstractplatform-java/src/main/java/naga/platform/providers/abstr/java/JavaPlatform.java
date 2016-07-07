package naga.platform.providers.abstr.java;

import naga.commons.services.query.spi.QueryService;
import naga.platform.spi.Platform;
import naga.commons.services.resource.spi.ResourceService;
import naga.commons.scheduler.spi.Scheduler;
import naga.commons.services.update.spi.UpdateService;

/**
 * @author Bruno Salmon
 */
public abstract class JavaPlatform extends Platform {

    protected JavaPlatform() {
        this(new JavaScheduler());
    }

    protected JavaPlatform(Scheduler scheduler) {
        super(scheduler);
    }

    @Override
    public QueryService queryService() {
        return JdbcQueryService.SINGLETON;
    }

    @Override
    public UpdateService updateService() {
        return JdbcUpdateService.SINGLETON;
    }

    @Override
    public ResourceService resourceService() {
        return JavaResourceService.SINGLETON;
    }

}
