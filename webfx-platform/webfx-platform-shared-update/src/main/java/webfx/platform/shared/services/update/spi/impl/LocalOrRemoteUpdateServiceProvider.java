package webfx.platform.shared.services.update.spi.impl;

import webfx.platform.shared.services.buscall.BusCallService;
import webfx.platform.shared.services.update.UpdateArgument;
import webfx.platform.shared.services.update.UpdateService;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.services.update.UpdateResult;

/**
 * @author Bruno Salmon
 */
public class LocalOrRemoteUpdateServiceProvider extends LocalUpdateServiceProvider {

    protected Future<UpdateResult> executeRemoteUpdate(UpdateArgument argument) {
        return BusCallService.call(UpdateService.UPDATE_SERVICE_ADDRESS, argument);
    }

    protected Future<Batch<UpdateResult>> executeRemoteUpdateBatch(Batch<UpdateArgument> batch) {
        return BusCallService.call(UpdateService.UPDATE_BATCH_SERVICE_ADDRESS, batch);
    }

}
