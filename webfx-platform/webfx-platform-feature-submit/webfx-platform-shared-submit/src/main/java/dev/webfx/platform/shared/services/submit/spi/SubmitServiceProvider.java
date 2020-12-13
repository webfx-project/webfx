package dev.webfx.platform.shared.services.submit.spi;

import dev.webfx.platform.shared.services.submit.SubmitArgument;
import dev.webfx.platform.shared.services.submit.SubmitResult;
import dev.webfx.platform.shared.util.async.Batch;
import dev.webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface SubmitServiceProvider {

    Future<SubmitResult> executeSubmit(SubmitArgument argument);

    default Future<Batch<SubmitResult>> executeSubmitBatch(Batch<SubmitArgument> batch) {
        return batch.executeSerial(SubmitResult[]::new, this::executeSubmit);
    }

}
