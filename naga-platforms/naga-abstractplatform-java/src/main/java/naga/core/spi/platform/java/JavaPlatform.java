package naga.core.spi.platform.java;

import naga.core.queryservice.QueryService;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.ResourceService;
import naga.core.spi.platform.Scheduler;
import naga.core.updateservice.UpdateService;

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
