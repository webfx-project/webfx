package naga.platform.services.query;

import naga.platform.json.spi.JsonObject;
import naga.platform.json.codec.AbstractJsonCodec;
import naga.platform.json.spi.WritableJsonObject;
import naga.platform.json.codec.JsonCodecManager;
import naga.commons.util.Arrays;

/**
 * @author Bruno Salmon
 */
public class QueryArgument {

    private final String queryString;
    private final Object[] parameters;
    private final Object dataSourceId;

    public QueryArgument(String queryString, Object dataSourceId) {
        this(queryString, null, dataSourceId);
    }

    public QueryArgument(String queryString, Object[] parameters, Object dataSourceId) {
        this.queryString = queryString;
        this.parameters = parameters;
        this.dataSourceId = dataSourceId;
    }

    public String getQueryString() {
        return queryString;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    /****************************************************
     *                    Json Codec                    *
     * *************************************************/

    private static String CODEC_ID = "QueryArg";
    private static String QUERY_KEY = "query";
    private static String PARAMETERS_KEY = "params";
    private static String DATA_SOURCE_ID_KEY = "dsId";

    public static void registerJsonCodec() {
        new AbstractJsonCodec<QueryArgument>(QueryArgument.class, CODEC_ID) {

            @Override
            public void encodeToJson(QueryArgument arg, WritableJsonObject json) {
                json.set(QUERY_KEY, arg.getQueryString());
                if (!Arrays.isEmpty(arg.getParameters()))
                    json.set(PARAMETERS_KEY, JsonCodecManager.encodePrimitiveArrayToJsonArray(arg.getParameters()));
                json.set(DATA_SOURCE_ID_KEY, arg.getDataSourceId());
            }

            @Override
            public QueryArgument decodeFromJson(JsonObject json) {
                return new QueryArgument(
                        json.getString(QUERY_KEY),
                        JsonCodecManager.decodePrimitiveArrayFromJsonArray(json.getArray(PARAMETERS_KEY)),
                        json.get(DATA_SOURCE_ID_KEY)
                );
            }
        };
    }

}
