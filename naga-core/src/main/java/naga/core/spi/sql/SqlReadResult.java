package naga.core.spi.sql;

import naga.core.codec.AbstractCompositeCodec;
import naga.core.composite.CompositeObject;
import naga.core.composite.WritableCompositeObject;
import naga.core.spi.json.Json;
import naga.core.spi.json.JsonArray;
import naga.core.type.PrimType;
import naga.core.util.Numbers;
import naga.core.util.compression.values.repeat.RepeatedValuesCompressor;

/**
 * @author Bruno Salmon
 */
public class SqlReadResult {

    /**
     * The number of rows in this result set.
     */
    private final int rowCount;

    /**
     * The number of columns in this result set.
     */
    private final int columnCount;

    /**
     * The values of the result set stored in an inline array (more efficient for compression algorithm).
     * First column, then 2nd column, etc... So inlineIndex = rowIndex + columnIndex * rowCount.
     * (better than 1st row, 2nd row, etc.. for compression algorithm)
     */
    private Object[] values;

    /**
     * Sql column names of the result set. This information is actually optional and useful only for debugging or when
     * using methods to access values with column names instead of column index (like in the DomainModelLoader)
     */
    private final String[] columnNames;


    public SqlReadResult(int rowCount, int columnCount, Object[] values, String[] columnNames) {
        if (values.length != columnCount * rowCount || columnNames != null && columnNames.length != columnCount)
            throw new IllegalArgumentException("Incoherent sizes in SqlReadResult initialization");
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.columnNames = columnNames;
        this.values = values;
    }

    public SqlReadResult(int columnCount, Object[] values) {
        this(values.length / columnCount, columnCount, values, null);
    }

    public SqlReadResult(Object[] values, String[] columnNames) {
        this(values.length / columnNames.length, columnNames.length, values, columnNames);
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public <T> T getValue(int rowIndex, int columnIndex) {
        return (T) values[rowIndex + columnIndex * rowCount];
    }

    /*******************************************************************************************
     * Secondary convenient methods to access values with column names instead of column index *
     *******************************************************************************************/

    public int getColumnIndex(String columnName) {
        int n = getColumnCount();
        String[] columnNames = getColumnNames();
        for (int i = 0; i < n; i++) {
            if (columnName.equalsIgnoreCase(columnNames[i]))
                return i;
        }
        return -1;
    }

    public <T> T getValue(int rowIndex, String columnName) {
        return getValue(rowIndex, getColumnIndex(columnName));
    }

    public <T> T getValue(int rowIndex, String columnName, T defaultValue) {
        T value = getValue(rowIndex, getColumnIndex(columnName));
        return value != null ? value : defaultValue;
    }

    // To be used with int to avoid GWT ClassCastException as Integer coming from Json may actually be Double
    public int getInt(int rowIndex, String columnName, int defaultValue) {
        Object value = getValue(rowIndex, getColumnIndex(columnName));
        return Numbers.isNumber(value) ? Numbers.intValue(value) : defaultValue;
    }

    // To be used with Codenameone because generic one raise an error when value is a string
    public boolean getBoolean(int rowIndex, String columnName, boolean defaultValue) {
        Object value = getValue(rowIndex, getColumnIndex(columnName));
        return value == null ? defaultValue : value instanceof Boolean ? (Boolean) value : Boolean.valueOf(value.toString());
    }

    /****************************************************
     *                 Composite Codec                  *
     * *************************************************/

    private final static String CODEC_ID = "SqlReadResult";
    private final static String COLUMN_NAMES_KEY = "names";
    private final static String COLUMN_TYPES_KEY = "types";
    private final static String VALUES_KEY = "values";
    private final static String COMPRESSED_VALUES_KEY = "cvalues";

    private final static boolean COMPRESSION = true;

    public static void registerCompositeCodec() {
        new AbstractCompositeCodec<SqlReadResult>(SqlReadResult.class, CODEC_ID) {

            @Override
            public void encodeToComposite(SqlReadResult result, WritableCompositeObject co) {
                try {
                    int columnCount = result.getColumnCount();
                    // Column names serialization
                    JsonArray namesArray = Json.createArray();
                    for (String name : result.getColumnNames())
                        namesArray.push(name);
                    co.set(COLUMN_NAMES_KEY, namesArray);
                    if (columnCount == -1)
                        columnCount = namesArray.size();
                    // types serialization & values compression
                    JsonArray typesArray = Json.createArray();
                    PrimType[] types = new PrimType[columnCount];
                    int rowCount = result.getRowCount();
                    // Guessing types from values
                    for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
                        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                            Object value = result.getValue(rowIndex, columnIndex);
                            if (value != null) {
                                types[columnIndex] = PrimType.fromObject(value);
                                break;
                            }
                        }
                    for (PrimType type : types)
                        typesArray.push(type == null ? null : type.name());
                    co.set(COLUMN_TYPES_KEY, typesArray);
                    // values packing and serialization
                    if (COMPRESSION)
                        co.set(COMPRESSED_VALUES_KEY, Json.fromJavaArray(RepeatedValuesCompressor.SINGLETON.compress(result.values)));
                    else
                        co.set(VALUES_KEY, result.values);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public SqlReadResult decodeFromComposite(CompositeObject co) {
                // Column names deserialization
                JsonArray namesArray = (JsonArray) co.getArray(COLUMN_NAMES_KEY);
                int columnCount = namesArray.size();
                String[] names = new String[columnCount];
                for (int i = 0; i < columnCount; i++)
                    names[i] = namesArray.getString(i);
                // Types deserialization
                JsonArray typesArray = (JsonArray) co.getArray(COLUMN_TYPES_KEY);
                PrimType[] types = new PrimType[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    String typeName = typesArray.getString(i);
                    if (typeName != null)
                        types[i] = PrimType.valueOf(typeName);
                }
                // Values deserialization
                Object[] inlineValues;
                JsonArray valuesArray = (JsonArray) co.getArray(VALUES_KEY);
                if (valuesArray != null)
                    inlineValues = Json.toJavaArray(valuesArray);
                else
                    inlineValues = RepeatedValuesCompressor.SINGLETON.uncompress(Json.toJavaArray((JsonArray) co.getArray(COMPRESSED_VALUES_KEY)));
                // returning the result as a snapshot
                return new SqlReadResult(inlineValues, names);
            }
        };
    }
}
