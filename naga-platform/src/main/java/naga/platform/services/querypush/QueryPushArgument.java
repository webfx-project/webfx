package naga.platform.services.querypush;

import naga.platform.json.codec.AbstractJsonCodec;
import naga.platform.json.codec.JsonCodecManager;
import naga.platform.json.spi.JsonObject;
import naga.platform.json.spi.WritableJsonObject;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;
import naga.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class QueryPushArgument {

    private final Object queryStreamId;
    private final Object pushClientId;
    private final QueryArgument queryArgument;
    private final Consumer<QueryResultSet> queryResultSetConsumer;
    private final Object dataSourceId;
    private final Boolean active;
    private final Boolean close;

    public QueryPushArgument(Object queryStreamId, Object pushClientId, QueryArgument queryArgument, Consumer<QueryResultSet> queryResultSetConsumer, Boolean active, Boolean close) {
        this(queryStreamId, pushClientId, queryArgument, queryResultSetConsumer, queryArgument.getDataSourceId(), active, close);
    }

    public QueryPushArgument(Object queryStreamId, Object pushClientId, QueryArgument queryArgument, Consumer<QueryResultSet> queryResultSetConsumer, Object dataSourceId, Boolean active, Boolean close) {
        this.queryStreamId = queryStreamId;
        this.pushClientId = pushClientId;
        this.queryArgument = queryArgument;
        this.queryResultSetConsumer = queryResultSetConsumer;
        this.dataSourceId = dataSourceId;
        this.active = active;
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

    public Consumer<QueryResultSet> getQueryResultSetConsumer() {
        return queryResultSetConsumer;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public Boolean getActive() {
        return active;
    }

    public Boolean getClose() {
        return close;
    }

    public boolean isOpenStreamArgument() {
        return queryStreamId == null;
    }

    public boolean isUpdateStreamArgument() {
        return queryStreamId != null && queryArgument != null;
    }

    public boolean isActivateStreamArgument() {
        return queryStreamId != null && active != null;
    }

    public boolean isCloseStreamArgument() {
        return queryStreamId != null && close != null;
    }

    public static QueryPushArgument openStreamArgument(Object pushClientId, QueryArgument queryArgument, Consumer<QueryResultSet> queryResultSetConsumer) {
        return new QueryPushArgument(null, pushClientId, queryArgument, queryResultSetConsumer, true, null);
    }

    public static QueryPushArgument updateStreamArgument(Object queryStreamId, QueryArgument queryArgument) {
        return new QueryPushArgument(queryStreamId, null, queryArgument, null, null, null);
    }

    public static QueryPushArgument activateStreamArgument(Object queryStreamId, Object dataSourceId, boolean active) {
        return new QueryPushArgument(queryStreamId, null, null, null, dataSourceId, active, null);
    }

    public static QueryPushArgument closeStreamArgument(Object queryStreamId, Object dataSourceId) {
        return new QueryPushArgument(queryStreamId, null, null, null, dataSourceId, null, true);
    }

    /****************************************************
     *                    Json Codec                    *
     * *************************************************/

    public static final String CODEC_ID = "QueryPushArg";
    private static final String QUERY_STREAM_ID_KEY = "queryStreamId";
    private static final String CLIENT_PUSH_ID_KEY = "pushClientId";
    private static final String QUERY_ARGUMENT_KEY = "queryArg";
    private static final String DATA_SOURCE_ID_KEY = "dsId";
    private static final String ACTIVE_KEY = "active";
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
                encodeKeyIfNotNull(CLOSE_KEY, arg.getClose(), json);
            }

            @Override
            public QueryPushArgument decodeFromJson(JsonObject json) {
                return new QueryPushArgument(
                        json.get(QUERY_STREAM_ID_KEY),
                        json.get(CLIENT_PUSH_ID_KEY),
                        JsonCodecManager.decodeFromJson(json.get(QUERY_ARGUMENT_KEY)),
                        null,
                        json.get(DATA_SOURCE_ID_KEY),
                        json.getBoolean(ACTIVE_KEY),
                        json.getBoolean(CLOSE_KEY)
                );
            }
        };
    }

}
