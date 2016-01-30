package naga.core.spi.sql;

import naga.core.jsoncodec.AbstractJsonCodec;
import naga.core.jsoncodec.JsonCodecManager;
import naga.core.spi.json.JsonObject;
import naga.core.util.Arrays;

/**
 * @author Bruno Salmon
 */
public class SqlArgument {

    private final String sql;
    private final Object[] parameters;
    private final Object dataSourceId;

    public SqlArgument(String sql, Object dataSourceId) {
        this(sql, null, dataSourceId);
    }

    public SqlArgument(String sql, Object[] parameters, Object dataSourceId) {
        this.sql = sql;
        this.parameters = parameters;
        this.dataSourceId = dataSourceId;
    }

    public String getSql() {
        return sql;
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

    private static String CODEC_ID = "sqlArg";
    private static String SQL_KEY = "sql";
    private static String PARAMETERS_KEY = "params";
    private static String DATA_SOURCE_ID_KEY = "dsId";

    public static void registerJsonCodec() {
        new AbstractJsonCodec<SqlArgument>(SqlArgument.class, CODEC_ID) {

            @Override
            public void encodeToJson(SqlArgument arg, JsonObject json) {
                json.set(SQL_KEY, arg.getSql());
                if (!Arrays.isEmpty(arg.getParameters()))
                    json.set(PARAMETERS_KEY, JsonCodecManager.encodePrimitiveArrayToJsonArray(arg.getParameters()));
                json.set(DATA_SOURCE_ID_KEY, arg.getDataSourceId());
            }

            @Override
            public SqlArgument decodeFromJson(JsonObject json) {
                return new SqlArgument(
                        json.getString(SQL_KEY),
                        JsonCodecManager.decodePrimitiveArrayFromJsonArray(json.get(PARAMETERS_KEY)),
                        json.get(DATA_SOURCE_ID_KEY)
                );
            }
        };
    }

}
