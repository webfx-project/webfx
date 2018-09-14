package webfx.platforms.core.services.update.spi.impl;

import webfx.platforms.core.services.buscall.BusCallService;
import webfx.platforms.core.services.update.UpdateArgument;
import webfx.platforms.core.services.update.UpdateResult;
import webfx.platforms.core.services.update.UpdateService;
import webfx.platforms.core.util.async.Batch;
import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class LocalOrRemoteUpdateServiceProviderImpl extends LocalUpdateServiceProviderImpl {

    protected Future<UpdateResult> executeRemoteUpdate(UpdateArgument argument) {
        return BusCallService.call(UpdateService.UPDATE_SERVICE_ADDRESS, argument);
    }

    protected Future<Batch<UpdateResult>> executeRemoteUpdateBatch(Batch<UpdateArgument> batch) {
        return BusCallService.call(UpdateService.UPDATE_BATCH_SERVICE_ADDRESS, batch);
    }

}
