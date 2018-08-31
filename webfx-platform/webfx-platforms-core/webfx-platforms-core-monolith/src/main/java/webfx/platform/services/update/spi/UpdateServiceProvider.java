package webfx.platform.services.update.spi;

import webfx.platform.services.update.UpdateArgument;
import webfx.platform.services.update.UpdateResult;
import webfx.util.async.Batch;
import webfx.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UpdateServiceProvider {

    Future<UpdateResult> executeUpdate(UpdateArgument argument);

    default Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        return batch.executeSerial(UpdateResult[]::new, this::executeUpdate);
    }

}
