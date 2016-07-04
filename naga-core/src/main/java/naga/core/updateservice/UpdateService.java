package naga.core.updateservice;

import naga.core.util.async.Batch;
import naga.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UpdateService {

    Future<UpdateResult> executeUpdate(UpdateArgument argument);

    default Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        return batch.execute(this::executeUpdate, UpdateResult.class);
    }

}
