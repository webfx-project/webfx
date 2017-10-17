package naga.platform.services.update.spi;

import naga.platform.services.update.UpdateArgument;
import naga.platform.services.update.UpdateResult;
import naga.util.async.Batch;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UpdateService {

    Future<UpdateResult> executeUpdate(UpdateArgument argument);

    default Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        return batch.executeSerial(UpdateResult[]::new, this::executeUpdate);
    }

}
