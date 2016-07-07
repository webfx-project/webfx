package naga.core.spi.platform.java;

import naga.core.services.query.QueryService;
import naga.core.spi.platform.Platform;
import naga.core.services.resource.ResourceService;
import naga.core.spi.platform.Scheduler;
import naga.core.services.update.UpdateService;

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
