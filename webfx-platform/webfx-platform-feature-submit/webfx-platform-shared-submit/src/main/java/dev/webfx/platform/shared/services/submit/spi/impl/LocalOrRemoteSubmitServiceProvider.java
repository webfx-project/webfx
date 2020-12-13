package dev.webfx.platform.shared.services.submit.spi.impl;

import dev.webfx.platform.shared.services.buscall.BusCallService;
import dev.webfx.platform.shared.services.submit.SubmitArgument;
import dev.webfx.platform.shared.services.submit.SubmitResult;
import dev.webfx.platform.shared.services.submit.SubmitService;
import dev.webfx.platform.shared.util.async.Batch;
import dev.webfx.platform.shared.util.async.Future;

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
