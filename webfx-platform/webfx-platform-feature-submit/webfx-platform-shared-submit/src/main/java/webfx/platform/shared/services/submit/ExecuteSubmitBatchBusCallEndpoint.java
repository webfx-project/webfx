package webfx.platform.shared.services.submit;

import webfx.platform.shared.services.buscall.spi.AsyncFunctionBusCallEndpoint;
import webfx.platform.shared.util.async.Batch;

/**
 * @author Bruno Salmon
 */
public final class ExecuteSubmitBatchBusCallEndpoint extends AsyncFunctionBusCallEndpoint<Batch<SubmitArgument>, Batch<SubmitResult>> {

    public ExecuteSubmitBatchBusCallEndpoint() {
        super(SubmitService.SUBMIT_BATCH_SERVICE_ADDRESS, SubmitService::executeSubmitBatch);
    }
}
