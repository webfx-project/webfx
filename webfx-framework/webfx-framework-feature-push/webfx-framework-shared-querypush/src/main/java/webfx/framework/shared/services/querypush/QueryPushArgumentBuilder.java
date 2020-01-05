package webfx.framework.shared.services.querypush;

import webfx.platform.shared.services.query.QueryArgument;

import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public final class QueryPushArgumentBuilder {

    private Object queryStreamId;
    private Object parentQueryStreamId;
    private Object pushClientId;
    private QueryArgument queryArgument;
    private Object dataSourceId;
    private Boolean active;
    private Boolean resend;
    private Boolean close;
    private Consumer<QueryPushResult> queryPushResultConsumer;

    public QueryPushArgumentBuilder setQueryStreamId(Object queryStreamId) {
        this.queryStreamId = queryStreamId;
        return this;
    }

    public QueryPushArgumentBuilder setParentQueryStreamId(Object parentQueryStreamId) {
        this.parentQueryStreamId = parentQueryStreamId;
        return this;
    }

    public QueryPushArgumentBuilder setPushClientId(Object pushClientId) {
        this.pushClientId = pushClientId;
        return this;
    }

    public QueryPushArgumentBuilder setQueryArgument(QueryArgument queryArgument) {
        this.queryArgument = queryArgument;
        return this;
    }

    public QueryPushArgumentBuilder setDataSourceId(Object dataSourceId) {
        this.dataSourceId = dataSourceId;
        return this;
    }

    public QueryPushArgumentBuilder setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public QueryPushArgumentBuilder setResend(Boolean resend) {
        this.resend = resend;
        return this;
    }

    public QueryPushArgumentBuilder setClose(Boolean close) {
        this.close = close;
        return this;
    }

    public QueryPushArgumentBuilder setQueryPushResultConsumer(Consumer<QueryPushResult> queryPushResultConsumer) {
        this.queryPushResultConsumer = queryPushResultConsumer;
        return this;
    }

    public QueryPushArgumentBuilder copy(QueryPushArgument argument) {
        return setQueryStreamId(argument.getQueryStreamId())
                .setParentQueryStreamId(argument.getParentQueryStreamId())
                .setPushClientId(argument.getPushClientId())
                .setQueryArgument(argument.getQueryArgument())
                .setDataSourceId(argument.getDataSourceId())
                .setActive(argument.getActive())
                .setResend(argument.getResend())
                .setClose(argument.getClose())
                .setQueryPushResultConsumer(argument.getQueryPushResultConsumer())
                ;
    }


    public QueryPushArgument build() {
        return new QueryPushArgument(
                queryStreamId,
                parentQueryStreamId,
                pushClientId,
                queryArgument,
                dataSourceId,
                active,
                resend,
                close,
                queryPushResultConsumer
        );
    }
}
