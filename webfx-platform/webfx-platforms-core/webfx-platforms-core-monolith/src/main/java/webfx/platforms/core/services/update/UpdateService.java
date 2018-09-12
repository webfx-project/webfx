package webfx.platforms.core.services.update;

import webfx.platforms.core.services.bus.call.BusCallService;
import webfx.platforms.core.services.update.spi.UpdateServiceProvider;
import webfx.platforms.core.services.update.spi.remote.RemoteUpdateServiceProviderImpl;
import webfx.platforms.core.util.async.Batch;
import webfx.platforms.core.util.async.Future;
import webfx.platforms.core.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class UpdateService {

    public static final String UPDATE_SERVICE_ADDRESS = "service/update";
    public static final String UPDATE_BATCH_SERVICE_ADDRESS = "service/update/batch";

    static {
        ServiceLoaderHelper.registerDefaultServiceFactory(UpdateServiceProvider.class, RemoteUpdateServiceProviderImpl::new);
        // registerJsonCodecsAndBusCalls() body:
        UpdateArgument.registerJsonCodec();
        GeneratedKeyBatchIndex.registerJsonCodec();
        UpdateResult.registerJsonCodec();
        BusCallService.registerJavaAsyncFunctionAsCallableService(UPDATE_SERVICE_ADDRESS, UpdateService::executeUpdate);
        BusCallService.registerJavaAsyncFunctionAsCallableService(UPDATE_BATCH_SERVICE_ADDRESS, UpdateService::executeUpdateBatch);
    }

    public static void registerJsonCodecsAndBusCalls() {
        // Body actually moved into the static constructor
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
