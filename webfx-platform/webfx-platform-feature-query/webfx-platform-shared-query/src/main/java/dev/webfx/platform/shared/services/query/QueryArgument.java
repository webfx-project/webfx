package dev.webfx.platform.shared.services.query;

import dev.webfx.platform.shared.datascope.DataScope;
import dev.webfx.platform.shared.services.json.JsonObject;
import dev.webfx.platform.shared.services.json.WritableJsonObject;
import dev.webfx.platform.shared.services.serial.SerialCodecManager;
import dev.webfx.platform.shared.services.serial.spi.impl.SerialCodecBase;
import dev.webfx.platform.shared.util.Arrays;

/**
 * @author Bruno Salmon
 */
public final class QueryArgument {

    private final transient QueryArgument originalArgument;
    private final Object dataSourceId;
    private final DataScope dataScope;
    private final String language;
    private final String statement;
    private final Object[] parameters;

    public QueryArgument(QueryArgument originalArgument, Object dataSourceId, DataScope dataScope, String language, String statement, Object... parameters) {
        this.originalArgument = originalArgument;
        this.dataSourceId = dataSourceId;
        this.dataScope = dataScope;
        this.language = language;
        this.statement = statement;
        this.parameters = parameters;
    }

    public QueryArgument getOriginalArgument() {
        return originalArgument;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public DataScope getDataScope() {
        return dataScope;
    }

    public String getLanguage() {
        return language;
    }

    public String getStatement() {
        return statement;
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
        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        if (statement != null ? !statement.equals(that.statement) : that.statement != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return java.util.Arrays.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        int result = dataSourceId.hashCode();
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (statement != null ? statement.hashCode() : 0);
        result = 31 * result + java.util.Arrays.hashCode(parameters);
        return result;
    }

    @Override
    public String toString() {
        return "QueryArgument{" +
                "dataSourceId=" + dataSourceId +
                ", queryLang='" + language + '\'' +
                ", queryString='" + statement + '\'' +
                ", parameters=" + java.util.Arrays.toString(parameters) +
                '}';
    }

    public static QueryArgumentBuilder builder() {
        return new QueryArgumentBuilder();
    }

    /**************************************
     *           Serial Codec             *
     * ***********************************/

    public static final class ProvidedSerialCodec extends SerialCodecBase<QueryArgument> {

        private static final String CODEC_ID = "QueryArgument";
        private static final String DATA_SOURCE_ID_KEY = "dataSourceId";
        private static final String DATA_SCOPE_KEY = "dataScope";
        private static final String LANGUAGE_KEY = "lang";
        private static final String STATEMENT_KEY = "statement";
        private static final String PARAMETERS_KEY = "parameters";

        public ProvidedSerialCodec() {
            super(QueryArgument.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(QueryArgument arg, WritableJsonObject json) {
            json.set(DATA_SOURCE_ID_KEY, arg.getDataSourceId());
            json.set(DATA_SCOPE_KEY, SerialCodecManager.encodeToJson(arg.getDataScope()));
            json.set(LANGUAGE_KEY, arg.getLanguage());
            json.set(STATEMENT_KEY, arg.getStatement());
            if (!Arrays.isEmpty(arg.getParameters()))
                json.set(PARAMETERS_KEY, SerialCodecManager.encodePrimitiveArrayToJsonArray(arg.getParameters()));
        }

        @Override
        public QueryArgument decodeFromJson(JsonObject json) {
            return new QueryArgument(null,
                    json.get(DATA_SOURCE_ID_KEY),
                    SerialCodecManager.decodeFromJson(json.getObject(DATA_SCOPE_KEY)),
                    json.getString(LANGUAGE_KEY),
                    json.getString(STATEMENT_KEY),
                    SerialCodecManager.decodePrimitiveArrayFromJsonArray(json.getArray(PARAMETERS_KEY))
            );
        }
    }
}
