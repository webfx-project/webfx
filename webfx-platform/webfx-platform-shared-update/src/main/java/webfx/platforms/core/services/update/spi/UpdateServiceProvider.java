package webfx.platforms.core.services.update.spi;

import webfx.platforms.core.services.update.UpdateArgument;
import webfx.platforms.core.services.update.UpdateResult;
import webfx.platforms.core.util.async.Batch;
import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UpdateServiceProvider {

    Future<UpdateResult> executeUpdate(UpdateArgument argument);

    default Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        return batch.executeSerial(UpdateResult[]::new, this::executeUpdate);
    }

}
