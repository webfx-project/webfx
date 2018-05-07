package naga.platform.services.query.push;

import naga.platform.json.codec.AbstractJsonCodec;
import naga.platform.json.codec.JsonCodecManager;
import naga.platform.json.spi.JsonObject;
import naga.platform.json.spi.WritableJsonObject;
import naga.platform.services.query.QueryResultSet;

/**
 * @author Bruno Salmon
 */
public class QueryPushResult {

    private final Object queryStreamId;
    private final QueryResultSet queryResultSet;

    public QueryPushResult(Object queryStreamId, QueryResultSet queryResultSet) {
        this.queryStreamId = queryStreamId;
        this.queryResultSet = queryResultSet;
    }

    public Object getQueryStreamId() {
        return queryStreamId;
    }

    public QueryResultSet getQueryResultSet() {
        return queryResultSet;
    }

    /****************************************************
     *                    Json Codec                    *
     * *************************************************/

    public static final String CODEC_ID = "QueryPushRes";
    private static final String QUERY_STREAM_ID_KEY = "queryStreamId";
    private static final String QUERY_RESULT_KEY = "queryRes";

    public static void registerJsonCodec() {
        new AbstractJsonCodec<QueryPushResult>(QueryPushResult.class, CODEC_ID) {

            @Override
            public void encodeToJson(QueryPushResult arg, WritableJsonObject json) {
                encodeKeyIfNotNull(QUERY_STREAM_ID_KEY, arg.getQueryStreamId(), json);
                encodeKeyIfNotNull(QUERY_RESULT_KEY, arg.getQueryResultSet(), json);
            }

            @Override
            public QueryPushResult decodeFromJson(JsonObject json) {
                return new QueryPushResult(
                        json.get(QUERY_STREAM_ID_KEY),
                        JsonCodecManager.decodeFromJson(json.get(QUERY_RESULT_KEY))
                );
            }
        };
    }

}
