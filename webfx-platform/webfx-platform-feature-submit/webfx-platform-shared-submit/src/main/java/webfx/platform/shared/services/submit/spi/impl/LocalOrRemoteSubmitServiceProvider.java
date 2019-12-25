package webfx.platform.shared.services.submit.spi.impl;

import webfx.platform.shared.services.buscall.BusCallService;
import webfx.platform.shared.services.submit.SubmitArgument;
import webfx.platform.shared.services.submit.SubmitResult;
import webfx.platform.shared.services.submit.SubmitService;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class LocalOrRemoteSubmitServiceProvider extends LocalSubmitServiceProvider {

    protected Future<SubmitResult> executeRemoteSubmit(SubmitArgument argument) {
        return BusCallService.call(SubmitService.SUBMIT_SERVICE_ADDRESS, argument);
    }

    protected Future<Batch<SubmitResult>> executeRemoteSubmitBatch(Batch<SubmitArgument> batch) {
        return BusCallService.call(SubmitService.SUBMIT_BATCH_SERVICE_ADDRESS, batch);
    }

}
