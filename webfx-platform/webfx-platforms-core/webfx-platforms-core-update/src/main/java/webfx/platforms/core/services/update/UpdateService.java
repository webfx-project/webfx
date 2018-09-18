package webfx.platforms.core.services.update;

import webfx.platforms.core.services.buscall.BusCallService;
import webfx.platforms.core.services.update.spi.UpdateServiceProvider;
import webfx.platforms.core.services.update.spi.impl.LocalOrRemoteUpdateServiceProviderImpl;
import webfx.platforms.core.util.async.Batch;
import webfx.platforms.core.util.async.Future;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public class UpdateService {

    public static final String UPDATE_SERVICE_ADDRESS = "service/update";
    public static final String UPDATE_BATCH_SERVICE_ADDRESS = "service/update/batch";

    static {
        SingleServiceLoader.registerDefaultServiceFactory(UpdateServiceProvider.class, LocalOrRemoteUpdateServiceProviderImpl::new);
        // registerJsonCodecsAndBusCalls() body:
        BusCallService.registerJavaAsyncFunctionAsCallableService(UPDATE_SERVICE_ADDRESS, UpdateService::executeUpdate);
        BusCallService.registerJavaAsyncFunctionAsCallableService(UPDATE_BATCH_SERVICE_ADDRESS, UpdateService::executeUpdateBatch);
    }

    public static void registerJsonCodecsAndBusCalls() {
        // Body actually moved into the static constructor
    }

    public static UpdateServiceProvider getProvider() {
        return SingleServiceLoader.loadService(UpdateServiceProvider.class);
    }

    public static void registerProvider(UpdateServiceProvider provider) {
        SingleServiceLoader.cacheServiceInstance(UpdateServiceProvider.class, provider);
    }

    public static Future<UpdateResult> executeUpdate(UpdateArgument argument) {
        return getProvider().executeUpdate(argument);
    }

    public static  Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        return getProvider().executeUpdateBatch(batch);
    }

}
