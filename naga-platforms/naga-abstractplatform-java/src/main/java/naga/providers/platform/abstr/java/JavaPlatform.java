package naga.providers.platform.abstr.java;

import naga.platform.services.query.spi.QueryService;
import naga.platform.services.resource.spi.ResourceService;
import naga.platform.services.update.spi.UpdateService;
import naga.platform.spi.Platform;
import naga.providers.platform.abstr.java.services.query.JdbcQueryService;
import naga.providers.platform.abstr.java.services.resource.JavaResourceService;
import naga.providers.platform.abstr.java.services.update.JdbcUpdateService;

/**
 * @author Bruno Salmon
 */
public abstract class JavaPlatform extends Platform {

    protected JavaPlatform() {}

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
