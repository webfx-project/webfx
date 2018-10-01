package webfx.platforms.core.services.querypush;

import webfx.platforms.core.services.querypush.spi.impl.QueryPushServiceProviderBase;

/**
 * @author Bruno Salmon
 */
public final class PulseArgument {

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
