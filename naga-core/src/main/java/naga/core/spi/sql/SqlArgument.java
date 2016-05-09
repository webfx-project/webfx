package naga.core.spi.sql;

import naga.core.composite.codec.AbstractCompositeCodec;
import naga.core.composite.CompositeObject;
import naga.core.composite.WritableCompositeObject;
import naga.core.composite.codec.CompositeCodecManager;
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
     *                 Composite Codec                  *
     * *************************************************/

    private static String CODEC_ID = "sqlArg";
    private static String SQL_KEY = "sql";
    private static String PARAMETERS_KEY = "params";
    private static String DATA_SOURCE_ID_KEY = "dsId";

    public static void registerCompositeCodec() {
        new AbstractCompositeCodec<SqlArgument>(SqlArgument.class, CODEC_ID) {

            @Override
            public void encodeToComposite(SqlArgument arg, WritableCompositeObject co) {
                co.set(SQL_KEY, arg.getSql());
                if (!Arrays.isEmpty(arg.getParameters()))
                    co.set(PARAMETERS_KEY, CompositeCodecManager.encodePrimitiveArrayToCompositeArray(arg.getParameters()));
                co.set(DATA_SOURCE_ID_KEY, arg.getDataSourceId());
            }

            @Override
            public SqlArgument decodeFromComposite(CompositeObject co) {
                return new SqlArgument(
                        co.getString(SQL_KEY),
                        CompositeCodecManager.decodePrimitiveArrayFromCompositeArray(co.getArray(PARAMETERS_KEY)),
                        co.get(DATA_SOURCE_ID_KEY)
                );
            }
        };
    }

}
