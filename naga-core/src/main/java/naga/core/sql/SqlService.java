package naga.core.sql;

import naga.core.util.async.Batch;
import naga.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface SqlService {

    Future<SqlReadResult> read(SqlArgument argument);

    Future<SqlWriteResult> write(SqlArgument argument);

    // Batch support

    default Future<Batch<SqlReadResult>> readBatch(Batch<SqlArgument> batch) {
        return batch.execute(this::read, SqlReadResult.class);
    }

    default Future<Batch<SqlWriteResult>> writeBatch(Batch<SqlArgument> batch) {
        return batch.execute(this::write, SqlWriteResult.class);
    }

}
