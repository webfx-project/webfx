package webfx.platforms.core.services.query.push;

import webfx.platforms.core.services.query.push.spi.impl.QueryPushServiceProviderImplBase;

/**
 * @author Bruno Salmon
 */
public class PulseArgument {

    private final Object dataSourceId;
    private final QueryPushServiceProviderImplBase.QueryInfo queryInfo;

    public PulseArgument(Object dataSourceId) {
        this.dataSourceId = dataSourceId;
        queryInfo = null;
    }

    public PulseArgument(QueryPushServiceProviderImplBase.QueryInfo queryInfo) {
        this.queryInfo = queryInfo;
        dataSourceId = null;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public QueryPushServiceProviderImplBase.QueryInfo getQueryInfo() {
        return queryInfo;
    }
}
