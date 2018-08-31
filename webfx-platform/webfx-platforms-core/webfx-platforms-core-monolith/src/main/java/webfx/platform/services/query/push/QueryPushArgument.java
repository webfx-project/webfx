package webfx.platform.services.query.push;

import webfx.platform.services.json.codec.AbstractJsonCodec;
import webfx.platform.services.json.codec.JsonCodecManager;
import webfx.platform.services.json.JsonObject;
import webfx.platform.services.json.WritableJsonObject;
import webfx.platform.services.query.QueryArgument;
import webfx.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class QueryPushArgument {

    private final Object queryStreamId;
    private final Object pushClientId;
    private final QueryArgument queryArgument;
    private final Consumer<QueryPushResult> queryPushResultConsumer;
    private final Object dataSourceId;
    private final Boolean active;
    private final Boolean resend;
    private final Boolean close;

    public QueryPushArgument(Object queryStreamId, Object pushClientId, QueryArgument queryArgument, Boolean active, Boolean resend, Boolean close, Consumer<QueryPushResult> queryPushResultConsumer) {
        this(queryStreamId, pushClientId, queryArgument, queryArgument.getDataSourceId(), active, resend, close, queryPushResultConsumer);
    }

    public QueryPushArgument(Object queryStreamId, Object pushClientId, QueryArgument queryArgument, Object dataSourceId, Boolean active, Boolean resend, Boolean close, Consumer<QueryPushResult> queryPushResultConsumer) {
        this.queryStreamId = queryStreamId;
        this.pushClientId = pushClientId;
        this.queryArgument = queryArgument;
        this.queryPushResultConsumer = queryPushResultConsumer;
        this.dataSourceId = dataSourceId;
        this.active = active;
        this.resend = resend;
        this.close = close;
    }

    public Object getQueryStreamId() {
        return queryStreamId;
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

    public static QueryPushArgument openStreamArgument(Object pushClientId, QueryArgument queryArgument, Consumer<QueryPushResult> queryResultConsumer) {
        return new QueryPushArgument(null, pushClientId, queryArgument, true, null, null, queryResultConsumer);
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
        return new QueryPushArgument(queryStreamId, null, queryArgument, dataSourceId, active, null,null, null);
    }

    public static QueryPushArgument closeStreamArgument(Object queryStreamId, Object dataSourceId) {
        return new QueryPushArgument(queryStreamId, null, null, dataSourceId, null,null, true, null);
    }

    /****************************************************
     *                    Json Codec                    *
     * *************************************************/

    public static final String CODEC_ID = "QueryPushArgument";
    private static final String QUERY_STREAM_ID_KEY = "queryStreamId";
    private static final String CLIENT_PUSH_ID_KEY = "pushClientId";
    private static final String QUERY_ARGUMENT_KEY = "queryArgument";
    private static final String DATA_SOURCE_ID_KEY = "dataSourceId";
    private static final String ACTIVE_KEY = "active";
    private static final String RESEND_KEY = "resend";
    private static final String CLOSE_KEY = "close";

    public static void registerJsonCodec() {
        new AbstractJsonCodec<QueryPushArgument>(QueryPushArgument.class, CODEC_ID) {

            @Override
            public void encodeToJson(QueryPushArgument arg, WritableJsonObject json) {
                encodeKeyIfNotNull(QUERY_STREAM_ID_KEY, arg.getQueryStreamId(), json);
                encodeKeyIfNotNull(CLIENT_PUSH_ID_KEY, arg.getPushClientId(), json);
                encodeKeyIfNotNull(QUERY_ARGUMENT_KEY, arg.getQueryArgument(), json);
                encodeKey(DATA_SOURCE_ID_KEY, arg.getDataSourceId(), json);
                encodeKeyIfNotNull(ACTIVE_KEY, arg.getActive(), json);
                encodeKeyIfNotNull(RESEND_KEY, arg.getResend(), json);
                encodeKeyIfNotNull(CLOSE_KEY, arg.getClose(), json);
            }

            @Override
            public QueryPushArgument decodeFromJson(JsonObject json) {
                return new QueryPushArgument(
                        json.get(QUERY_STREAM_ID_KEY),
                        json.get(CLIENT_PUSH_ID_KEY),
                        JsonCodecManager.decodeFromJson(json.get(QUERY_ARGUMENT_KEY)),
                        json.get(DATA_SOURCE_ID_KEY),
                        json.getBoolean(ACTIVE_KEY),
                        json.getBoolean(RESEND_KEY),
                        json.getBoolean(CLOSE_KEY),
                        null
                );
            }
        };
    }

}
