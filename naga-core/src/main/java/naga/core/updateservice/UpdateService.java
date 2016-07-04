package naga.core.updateservice;

import naga.core.util.async.Batch;
import naga.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UpdateService {

    Future<UpdateResult> update(UpdateArgument argument);

    default Future<Batch<UpdateResult>> updateBatch(Batch<UpdateArgument> batch) {
        return batch.execute(this::update, UpdateResult.class);
    }

}
