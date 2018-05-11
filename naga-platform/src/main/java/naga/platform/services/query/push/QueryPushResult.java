package naga.platform.services.query.push;

import naga.platform.json.codec.AbstractJsonCodec;
import naga.platform.json.codec.JsonCodecManager;
import naga.platform.json.spi.JsonObject;
import naga.platform.json.spi.WritableJsonObject;
import naga.platform.services.query.QueryResult;

/**
 * @author Bruno Salmon
 */
public class QueryPushResult {

    private final Object queryStreamId;
    private final QueryResult queryResult;

    public QueryPushResult(Object queryStreamId, QueryResult queryResult) {
        this.queryStreamId = queryStreamId;
        this.queryResult = queryResult;
    }

    public Object getQueryStreamId() {
        return queryStreamId;
    }

    public QueryResult getQueryResult() {
        return queryResult;
    }

    /****************************************************
     *                    Json Codec                    *
     * *************************************************/

    public static final String CODEC_ID = "QueryPushResult";
    private static final String QUERY_STREAM_ID_KEY = "queryStreamId";
    private static final String QUERY_RESULT_KEY = "queryResult";

    public static void registerJsonCodec() {
        new AbstractJsonCodec<QueryPushResult>(QueryPushResult.class, CODEC_ID) {

            @Override
            public void encodeToJson(QueryPushResult arg, WritableJsonObject json) {
                encodeKeyIfNotNull(QUERY_STREAM_ID_KEY, arg.getQueryStreamId(), json);
                encodeKeyIfNotNull(QUERY_RESULT_KEY, arg.getQueryResult(), json);
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
