package naga.core.spi.sql;

import naga.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface SqlService {

    Future<SqlReadResult> read(SqlArgument argument);

    Future<SqlWriteResult> write(SqlArgument argument);

}
