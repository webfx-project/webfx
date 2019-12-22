package webfx.platform.shared.services.update.spi;

import webfx.platform.shared.services.update.UpdateArgument;
import webfx.platform.shared.services.update.UpdateResult;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UpdateServiceProvider {

    Future<UpdateResult> executeUpdate(UpdateArgument argument);

    default Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        return batch.executeSerial(UpdateResult[]::new, this::executeUpdate);
    }

}
