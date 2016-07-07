package naga.commons.services.update.spi;

import naga.commons.services.update.UpdateArgument;
import naga.commons.services.update.UpdateResult;
import naga.commons.util.async.Batch;
import naga.commons.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UpdateService {

    Future<UpdateResult> executeUpdate(UpdateArgument argument);

    default Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        return batch.execute(this::executeUpdate, UpdateResult.class);
    }

}
