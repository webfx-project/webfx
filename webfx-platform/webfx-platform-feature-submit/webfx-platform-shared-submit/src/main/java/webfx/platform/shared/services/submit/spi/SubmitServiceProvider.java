package webfx.platform.shared.services.submit.spi;

import webfx.platform.shared.services.submit.SubmitArgument;
import webfx.platform.shared.services.submit.SubmitResult;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface SubmitServiceProvider {

    Future<SubmitResult> executeSubmit(SubmitArgument argument);

    default Future<Batch<SubmitResult>> executeSubmitBatch(Batch<SubmitArgument> batch) {
        return batch.executeSerial(SubmitResult[]::new, this::executeSubmit);
    }

}
