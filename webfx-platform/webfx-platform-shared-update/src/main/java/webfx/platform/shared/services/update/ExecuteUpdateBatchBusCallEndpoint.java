package webfx.platform.shared.services.update;

import webfx.platform.shared.services.buscall.spi.AsyncFunctionBusCallEndpoint;
import webfx.platform.shared.util.async.Batch;

/**
 * @author Bruno Salmon
 */
public final class ExecuteUpdateBatchBusCallEndpoint extends AsyncFunctionBusCallEndpoint<Batch<UpdateArgument>, Batch<UpdateResult>> {

    public ExecuteUpdateBatchBusCallEndpoint() {
        super(UpdateService.UPDATE_BATCH_SERVICE_ADDRESS, UpdateService::executeUpdateBatch);
    }
}
