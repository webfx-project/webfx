package naga.platform.services.query;

import naga.platform.json.spi.JsonObject;
import naga.platform.json.codec.AbstractJsonCodec;
import naga.platform.json.spi.WritableJsonObject;
import naga.platform.json.codec.JsonCodecManager;
import naga.util.Arrays;

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

    public static final String CODEC_ID = "QueryArgument";
    private static final String QUERY_STRING_KEY = "queryString";
    private static final String PARAMETERS_KEY = "parameters";
    private static final String DATA_SOURCE_ID_KEY = "dataSourceId";

    public static void registerJsonCodec() {
        new AbstractJsonCodec<QueryArgument>(QueryArgument.class, CODEC_ID) {

            @Override
            public void encodeToJson(QueryArgument arg, WritableJsonObject json) {
                json.set(QUERY_STRING_KEY, arg.getQueryString());
                if (!Arrays.isEmpty(arg.getParameters()))
                    json.set(PARAMETERS_KEY, JsonCodecManager.encodePrimitiveArrayToJsonArray(arg.getParameters()));
                json.set(DATA_SOURCE_ID_KEY, arg.getDataSourceId());
            }

            @Override
            public QueryArgument decodeFromJson(JsonObject json) {
                return new QueryArgument(
                        json.getString(QUERY_STRING_KEY),
                        JsonCodecManager.decodePrimitiveArrayFromJsonArray(json.getArray(PARAMETERS_KEY)),
                        json.get(DATA_SOURCE_ID_KEY)
                );
            }
        };
    }

}
