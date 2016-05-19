package naga.core.spi.platform.java;

import naga.core.queryservice.QueryService;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.ResourceService;
import naga.core.spi.platform.Scheduler;

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
        return JdbcQueryService.JDBC_SQL_SERVICE;
    }

    @Override
    public ResourceService resourceService() {
        return JavaResourceService.SINGLETON;
    }

}
