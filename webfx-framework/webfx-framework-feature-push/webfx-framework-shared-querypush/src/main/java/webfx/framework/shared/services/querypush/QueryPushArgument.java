package webfx.framework.shared.services.querypush;

import webfx.platform.shared.services.serial.SerialCodecManager;
import webfx.platform.shared.services.serial.spi.impl.SerialCodecBase;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.json.WritableJsonObject;
import webfx.platform.shared.services.query.QueryArgument;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public final class QueryPushArgument {

    private final Object queryStreamId;
    private final Object parentQueryStreamId;
    private final Object pushClientId;
    private final QueryArgument queryArgument;
    private final Object dataSourceId;
    private final Boolean active;
    private final Boolean resend;
    private final Boolean close;
    private final transient Consumer<QueryPushResult> queryPushResultConsumer;

    public QueryPushArgument(Object queryStreamId, Object parentQueryStreamId, Object pushClientId, QueryArgument queryArgument, Object dataSourceId, Boolean active, Boolean resend, Boolean close, Consumer<QueryPushResult> queryPushResultConsumer) {
        this.queryStreamId = queryStreamId;
        this.parentQueryStreamId = parentQueryStreamId;
        this.pushClientId = pushClientId;
        this.queryArgument = queryArgument;
        this.queryPushResultConsumer = queryPushResultConsumer;
        this.dataSourceId = dataSourceId != null || queryArgument == null ? dataSourceId : queryArgument.getDataSourceId();
        this.active = active;
        this.resend = resend;
        this.close = close;
    }

    public Object getQueryStreamId() {
        return queryStreamId;
    }

    public Object getParentQueryStreamId() {
        return parentQueryStreamId;
    }

    public Object getPushClientId() {
        return pushClientId;
    }

    public QueryArgument getQueryArgument() {
        return queryArgument;
    }

    public Consumer<QueryPushResult> getQueryPushResultConsumer() {
        return queryPushResultConsumer;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public Boolean getActive() {
        return active;
    }

    public Boolean getResend() {
        return resend;
    }

    public Boolean getClose() {
        return close;
    }

    public boolean isOpenStreamArgument() {
        return queryStreamId == null;
    }

    public boolean isUpdateStreamArgument() {
        return queryStreamId != null && close == null;
    }

    public boolean isCloseStreamArgument() {
        return queryStreamId != null && close != null;
    }

    public static QueryPushArgumentBuilder builder() {
        return new QueryPushArgumentBuilder();
    }

    public static QueryPushArgument openStreamArgument(Object parentQueryStreamId, Object pushClientId, QueryArgument queryArgument, Consumer<QueryPushResult> queryResultConsumer) {
        return new QueryPushArgument(null, parentQueryStreamId, pushClientId, queryArgument, null, true, null, null, queryResultConsumer);
    }

    public static QueryPushArgument updateStreamArgument(Object queryStreamId, QueryArgument queryArgument) {
        return updateStreamArgument(queryStreamId, queryArgument, null);
    }

    public static QueryPushArgument updateStreamArgument(Object queryStreamId, QueryArgument queryArgument, Boolean active) {
        return updateStreamArgument(queryStreamId, queryArgument, queryArgument.getDataSourceId(), active);
    }

    public static QueryPushArgument updateStreamArgument(Object queryStreamId, Object dataSourceId, Boolean active) {
        return updateStreamArgument(queryStreamId, null, dataSourceId, active);
    }

    public static QueryPushArgument updateStreamArgument(Object queryStreamId, QueryArgument queryArgument, Object dataSourceId, Boolean active) {
        return new QueryPushArgument(queryStreamId, null, null, queryArgument, dataSourceId, active, null,null, null);
    }

    public static QueryPushArgument closeStreamArgument(Object queryStreamId, Object dataSourceId) {
        return new QueryPushArgument(queryStreamId, null, null, null, dataSourceId, null,null, true, null);
    }

    /****************************************************
     *                   Serial ProvidedSerialCodec                   *
     * *************************************************/

    public static final class ProvidedSerialCodec extends SerialCodecBase<QueryPushArgument> {

        private static final String CODEC_ID = "QueryPushArgument";
        private static final String QUERY_STREAM_ID_KEY = "queryStreamId";
        private static final String PARENT_QUERY_STREAM_ID_KEY = "parentQueryStreamId";
        private static final String CLIENT_PUSH_ID_KEY = "pushClientId";
        private static final String QUERY_ARGUMENT_KEY = "queryArgument";
        private static final String DATA_SOURCE_ID_KEY = "dataSourceId";
        private static final String ACTIVE_KEY = "active";
        private static final String RESEND_KEY = "resend";
        private static final String CLOSE_KEY = "close";

        public ProvidedSerialCodec() {
            super(QueryPushArgument.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(QueryPushArgument arg, WritableJsonObject json) {
            SerialCodecBase.encodeKeyIfNotNull(QUERY_STREAM_ID_KEY, arg.getQueryStreamId(), json);
            SerialCodecBase.encodeKeyIfNotNull(PARENT_QUERY_STREAM_ID_KEY, arg.getParentQueryStreamId(), json);
            SerialCodecBase.encodeKeyIfNotNull(CLIENT_PUSH_ID_KEY, arg.getPushClientId(), json);
            SerialCodecBase.encodeKeyIfNotNull(QUERY_ARGUMENT_KEY, arg.getQueryArgument(), json);
            SerialCodecBase.encodeKey(DATA_SOURCE_ID_KEY, arg.getDataSourceId(), json);
            SerialCodecBase.encodeKeyIfNotNull(ACTIVE_KEY, arg.getActive(), json);
            SerialCodecBase.encodeKeyIfNotNull(RESEND_KEY, arg.getResend(), json);
            SerialCodecBase.encodeKeyIfNotNull(CLOSE_KEY, arg.getClose(), json);
        }

        @Override
        public QueryPushArgument decodeFromJson(JsonObject json) {
            return new QueryPushArgument(
                    json.get(QUERY_STREAM_ID_KEY),
                    json.get(PARENT_QUERY_STREAM_ID_KEY),
                    json.get(CLIENT_PUSH_ID_KEY),
                    SerialCodecManager.decodeFromJson(json.get(QUERY_ARGUMENT_KEY)),
                    json.get(DATA_SOURCE_ID_KEY),
                    json.getBoolean(ACTIVE_KEY),
                    json.getBoolean(RESEND_KEY),
                    json.getBoolean(CLOSE_KEY),
                    null
            );
        }

    }
}
