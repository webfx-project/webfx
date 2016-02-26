package naga.core.spi.sql;

import naga.core.jsoncodec.AbstractJsonCodec;
import naga.core.spi.json.Json;
import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonObject;
import naga.core.type.PrimType;
import naga.core.util.compression.values.repeat.RepeatingValuesCompressor;

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
    private Object[] inlineValues;

    /**
     * Sql column names of the result set. This information is actually optional and useful only for debugging or when
     * using methods to access values with column names instead of column index (like in the DomainModelLoader)
     */
    private final String[] columnNames;


    public SqlReadResult(int rowCount, int columnCount, Object[] inlineValues, String[] columnNames) {
        if (inlineValues.length != columnCount * rowCount || columnNames != null && columnNames.length != columnCount)
            throw new IllegalArgumentException("Incoherent sizes in SqlReadResult initialization");
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.columnNames = columnNames;
        this.inlineValues = inlineValues;
    }

    public SqlReadResult(int columnCount, Object[] inlineValues) {
        this(inlineValues.length / columnCount, columnCount, inlineValues, null);
    }

    public SqlReadResult(Object[] inlineValues, String[] columnNames) {
        this(inlineValues.length / columnNames.length, columnNames.length, inlineValues, columnNames);
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
        return (T) inlineValues[rowIndex + columnIndex * rowCount];
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
        return value instanceof Number ? ((Number) value).intValue() : defaultValue;
    }

    /****************************************************
     *                    Json Codec                    *
     * *************************************************/

    private final static String CODEC_ID = "SqlReadResult";
    private final static String COLUMN_NAMES_KEY = "names";
    private final static String COLUMN_TYPES_KEY = "types";
    private final static String VALUES_KEY = "values";

    public static void registerJsonCodec() {
        new AbstractJsonCodec<SqlReadResult>(SqlReadResult.class, CODEC_ID) {

            @Override
            public void encodeToJson(SqlReadResult result, JsonObject json) {
                try {
                    int columnCount = result.getColumnCount();
                    // Column names serialization
                    JsonArray namesArray = Json.createArray();
                    for (String name : result.getColumnNames())
                        namesArray.push(name);
                    json.set(COLUMN_NAMES_KEY, namesArray);
                    if (columnCount == -1)
                        columnCount = namesArray.length();
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
                    json.set(COLUMN_TYPES_KEY, typesArray);
                    // values packing and serialization
                    json.set(VALUES_KEY, Json.fromJavaArray(RepeatingValuesCompressor.SINGLETON.packValues(result.inlineValues)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public SqlReadResult decodeFromJson(JsonObject json) {
                // Column names deserialization
                JsonArray namesArray = json.get(COLUMN_NAMES_KEY);
                int columnCount = namesArray.length();
                String[] names = new String[columnCount];
                for (int i = 0; i < columnCount; i++)
                    names[i] = namesArray.getString(i);
                // Types deserialization
                JsonArray typesArray = json.get(COLUMN_TYPES_KEY);
                PrimType[] types = new PrimType[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    String typeName = typesArray.getString(i);
                    if (typeName != null)
                        types[i] = PrimType.valueOf(typeName);
                }
                // Values deserialization
                JsonArray valuesArray = json.get(VALUES_KEY);
                Object[] inlineValues = RepeatingValuesCompressor.SINGLETON.unpackValues(Json.toJavaArray(valuesArray));
                // returning the result as a snapshot
                return new SqlReadResult(inlineValues, names);
            }
        };
    }
}
