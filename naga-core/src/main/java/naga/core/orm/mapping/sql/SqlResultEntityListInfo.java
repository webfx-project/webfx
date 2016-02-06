package naga.core.orm.mapping.sql;

import naga.core.orm.entity.EntityStore;
import naga.core.spi.sql.SqlReadResult;

/**
 * @author Bruno Salmon
 */
public class SqlResultEntityListInfo {

    SqlReadResult entityQueryResult;
    SqlRowToEntityMapping rowMapping;
    EntityStore store;
    Object listId;

}
