package webfx.platform.shared.services.query;

import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.json.WritableJsonObject;
import webfx.platform.shared.services.serial.SerialCodecManager;
import webfx.platform.shared.services.serial.spi.impl.SerialCodecBase;
import webfx.platform.shared.util.Arrays;

/**
 * @author Bruno Salmon
 */
public final class QueryArgument {

    private final transient QueryArgument originalArgument;

    private final Object dataSourceId;
    private final Object queryScope;
    private final String queryLang;
    private final String queryString;
    private final Object[] parameters;

    public QueryArgument(Object dataSourceId, String queryString, Object... parameters) {
        this(dataSourceId, null, queryString, parameters);
    }

    public QueryArgument(Object dataSourceId, String queryLang, String queryString, Object... parameters) {
        this(null, dataSourceId, null, queryLang, queryString, parameters);
    }

    public QueryArgument(QueryArgument originalArgument, String queryString) {
        this(originalArgument, originalArgument.getDataSourceId(), originalArgument.getQueryScope(), null, queryString, originalArgument.getParameters());
    }

    public QueryArgument(QueryArgument originalArgument, Object dataSourceId, Object queryScope, String queryLang, String queryString, Object... parameters) {
        this.originalArgument = originalArgument;
        this.dataSourceId = dataSourceId;
        this.queryScope = queryScope;
        this.queryLang = queryLang;
        this.queryString = queryString;
        this.parameters = parameters;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public QueryArgument getOriginalArgument() {
        return originalArgument;
    }

    public Object getQueryScope() {
        return queryScope;
    }

    public String getQueryLang() {
        return queryLang;
    }

    public String getQueryString() {
        return queryString;
    }

    public Object[] getParameters() {
        return parameters;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryArgument that = (QueryArgument) o;

        if (!dataSourceId.equals(that.dataSourceId)) return false;
        if (queryLang != null ? !queryLang.equals(that.queryLang) : that.queryLang != null) return false;
        if (queryString != null ? !queryString.equals(that.queryString) : that.queryString != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return java.util.Arrays.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        int result = dataSourceId.hashCode();
        result = 31 * result + (queryLang != null ? queryLang.hashCode() : 0);
        result = 31 * result + (queryString != null ? queryString.hashCode() : 0);
        result = 31 * result + java.util.Arrays.hashCode(parameters);
        return result;
    }

    @Override
    public String toString() {
        return "QueryArgument{" +
                "dataSourceId=" + dataSourceId +
                ", queryLang='" + queryLang + '\'' +
                ", queryString='" + queryString + '\'' +
                ", parameters=" + java.util.Arrays.toString(parameters) +
                '}';
    }

    /****************************************************
     *           Serial ProvidedSerialCodec             *
     * *************************************************/

    public static final class ProvidedSerialCodec extends SerialCodecBase<QueryArgument> {

        private static final String CODEC_ID = "QueryArgument";
        private static final String DATA_SOURCE_ID_KEY = "dataSourceId";
        private static final String QUERY_LANG_KEY = "queryLang";
        private static final String QUERY_STRING_KEY = "queryString";
        private static final String PARAMETERS_KEY = "parameters";

        public ProvidedSerialCodec() {
            super(QueryArgument.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(QueryArgument arg, WritableJsonObject json) {
            json.set(DATA_SOURCE_ID_KEY, arg.getDataSourceId());
            json.set(QUERY_LANG_KEY, arg.getQueryLang());
            json.set(QUERY_STRING_KEY, arg.getQueryString());
            if (!Arrays.isEmpty(arg.getParameters()))
                json.set(PARAMETERS_KEY, SerialCodecManager.encodePrimitiveArrayToJsonArray(arg.getParameters()));
        }

        @Override
        public QueryArgument decodeFromJson(JsonObject json) {
            return new QueryArgument(
                    json.get(DATA_SOURCE_ID_KEY),
                    json.getString(QUERY_LANG_KEY),
                    json.getString(QUERY_STRING_KEY),
                    SerialCodecManager.decodePrimitiveArrayFromJsonArray(json.getArray(PARAMETERS_KEY))
            );
        }
    }
}
