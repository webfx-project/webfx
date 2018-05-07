package naga.platform.services.query.push;

import naga.platform.services.query.push.spi.impl.QueryPushServiceProviderBase;

/**
 * @author Bruno Salmon
 */
public class PulseArgument {

    private final Object dataSourceId;
    private final QueryPushServiceProviderBase.QueryInfo queryInfo;

    public PulseArgument(Object dataSourceId) {
        this.dataSourceId = dataSourceId;
        queryInfo = null;
    }

    public PulseArgument(QueryPushServiceProviderBase.QueryInfo queryInfo) {
        this.queryInfo = queryInfo;
        dataSourceId = null;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public QueryPushServiceProviderBase.QueryInfo getQueryInfo() {
        return queryInfo;
    }
}
