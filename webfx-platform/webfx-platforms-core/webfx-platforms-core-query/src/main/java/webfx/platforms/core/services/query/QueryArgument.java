package webfx.platforms.core.services.query;

import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.json.WritableJsonObject;
import webfx.platforms.core.services.serial.SerialCodecBase;
import webfx.platforms.core.services.serial.SerialCodecManager;
import webfx.platforms.core.util.Arrays;

/**
 * @author Bruno Salmon
 */
public final class QueryArgument {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryArgument that = (QueryArgument) o;

        if (!queryString.equals(that.queryString)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!java.util.Arrays.equals(parameters, that.parameters)) return false;
        return dataSourceId.equals(that.dataSourceId);
    }

    @Override
    public int hashCode() {
        int result = queryString.hashCode();
        result = 31 * result + java.util.Arrays.hashCode(parameters);
        result = 31 * result + dataSourceId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "QueryArgument{" +
                "queryString='" + queryString + '\'' +
                ", parameters=" + java.util.Arrays.toString(parameters) +
                ", dataSourceId=" + dataSourceId +
                '}';
    }

    /****************************************************
     *                   Serial ProvidedSerialCodec                   *
     * *************************************************/

    public static final class ProvidedSerialCodec extends SerialCodecBase<QueryArgument> {

        private static final String CODEC_ID = "QueryArgument";
        private static final String QUERY_STRING_KEY = "queryString";
        private static final String PARAMETERS_KEY = "parameters";
        private static final String DATA_SOURCE_ID_KEY = "dataSourceId";

        public ProvidedSerialCodec() {
            super(QueryArgument.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(QueryArgument arg, WritableJsonObject json) {
            json.set(QUERY_STRING_KEY, arg.getQueryString());
            if (!Arrays.isEmpty(arg.getParameters()))
                json.set(PARAMETERS_KEY, SerialCodecManager.encodePrimitiveArrayToJsonArray(arg.getParameters()));
            json.set(DATA_SOURCE_ID_KEY, arg.getDataSourceId());
        }

        @Override
        public QueryArgument decodeFromJson(JsonObject json) {
            return new QueryArgument(
                    json.getString(QUERY_STRING_KEY),
                    SerialCodecManager.decodePrimitiveArrayFromJsonArray(json.getArray(PARAMETERS_KEY)),
                    json.get(DATA_SOURCE_ID_KEY)
            );
        }
    }
}
