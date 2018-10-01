package webfx.platform.shared.services.update;

import webfx.platform.shared.services.update.spi.UpdateServiceProvider;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class UpdateService {

    public static final String UPDATE_SERVICE_ADDRESS = "service/update";
    public static final String UPDATE_BATCH_SERVICE_ADDRESS = "service/update/batch";

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
