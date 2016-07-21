package naga.platform.services.update.spi;

import naga.platform.services.update.UpdateArgument;
import naga.platform.services.update.UpdateResult;
import naga.commons.util.async.Batch;
import naga.commons.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UpdateService {

    Future<UpdateResult> executeUpdate(UpdateArgument argument);

    default Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        return batch.executeSerial(UpdateResult.class, this::executeUpdate);
    }

}
