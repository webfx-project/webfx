package webfx.platform.services.update;

import webfx.platform.services.update.spi.remote.RemoteUpdateServiceProviderImpl;
import webfx.platform.services.update.spi.UpdateServiceProvider;
import webfx.util.async.Batch;
import webfx.util.async.Future;
import webfx.util.serviceloader.ServiceLoaderHelper;

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
