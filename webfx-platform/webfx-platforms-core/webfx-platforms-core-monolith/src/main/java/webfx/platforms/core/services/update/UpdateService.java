package webfx.platforms.core.services.update;

import webfx.platforms.core.services.update.spi.remote.RemoteUpdateServiceProviderImpl;
import webfx.platforms.core.services.update.spi.UpdateServiceProvider;
import webfx.platforms.core.util.async.Batch;
import webfx.platforms.core.util.async.Future;
import webfx.platforms.core.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class UpdateService {

    static {
        ServiceLoaderHelper.registerDefaultServiceFactory(UpdateServiceProvider.class, RemoteUpdateServiceProviderImpl::new);
    }

    public static UpdateServiceProvider getProvider() {
        return ServiceLoaderHelper.loadService(UpdateServiceProvider.class);
    }

    public static void registerProvider(UpdateServiceProvider provider) {
        ServiceLoaderHelper.cacheServiceInstance(UpdateServiceProvider.class, provider);
    }

    public static Future<UpdateResult> executeUpdate(UpdateArgument argument) {
        return getProvider().executeUpdate(argument);
    }

    public static  Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        return getProvider().executeUpdateBatch(batch);
    }

}
