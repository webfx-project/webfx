package naga.platform.services.update.spi;

import naga.platform.services.update.UpdateArgument;
import naga.platform.services.update.UpdateResult;
import naga.util.async.Batch;
import naga.util.async.Future;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class UpdateService {

    private static UpdateServiceProvider PROVIDER;

    public static UpdateServiceProvider getProvider() {
        if (PROVIDER == null)
            registerProvider(ServiceLoaderHelper.loadService(UpdateServiceProvider.class));
        return PROVIDER;
    }

    public static void registerProvider(UpdateServiceProvider provider) {
        PROVIDER = provider;
    }

    public static Future<UpdateResult> executeUpdate(UpdateArgument argument) {
        return getProvider().executeUpdate(argument);
    }

    public static  Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        return getProvider().executeUpdateBatch(batch);
    }

}
