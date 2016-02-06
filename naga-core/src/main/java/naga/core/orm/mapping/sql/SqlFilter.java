package naga.core.orm.mapping.sql;

import naga.core.spi.sql.SqlArgument;

/**
 * @author Bruno Salmon
 */
public class SqlFilter {

    private final SqlArgument sqlArgument;
    private final SqlRowToEntityMapping sqlMapping;

    public SqlFilter(SqlArgument sqlArgument, SqlRowToEntityMapping sqlMapping) {
        this.sqlArgument = sqlArgument;
        this.sqlMapping = sqlMapping;
    }

    public SqlArgument getSqlArgument() {
        return sqlArgument;
    }

    public SqlRowToEntityMapping getSqlMapping() {
        return sqlMapping;
    }
}
