package naga.providers.platform.abstr.java;

import naga.platform.services.update.spi.UpdateService;
import naga.platform.spi.Platform;
import naga.providers.platform.abstr.java.services.update.JdbcUpdateService;

/**
 * @author Bruno Salmon
 */
public abstract class JavaPlatform extends Platform {

    protected JavaPlatform() {}

    @Override
    public UpdateService updateService() {
        return JdbcUpdateService.SINGLETON;
    }

}
