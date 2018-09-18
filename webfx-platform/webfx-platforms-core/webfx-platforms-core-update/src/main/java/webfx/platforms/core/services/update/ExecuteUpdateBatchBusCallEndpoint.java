package webfx.platforms.core.services.update;

import webfx.platforms.core.services.buscall.spi.AsyncFunctionBusCallEndpoint;
import webfx.platforms.core.util.async.Batch;

/**
 * @author Bruno Salmon
 */
public class ExecuteUpdateBatchBusCallEndpoint extends AsyncFunctionBusCallEndpoint<Batch<UpdateArgument>, Batch<UpdateResult>> {

    public ExecuteUpdateBatchBusCallEndpoint() {
        super(UpdateService.UPDATE_BATCH_SERVICE_ADDRESS, UpdateService::executeUpdateBatch);
    }
}
