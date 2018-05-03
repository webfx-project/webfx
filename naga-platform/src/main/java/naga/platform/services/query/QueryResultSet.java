package naga.platform.services.query;

import naga.util.Numbers;
import naga.platform.compression.values.repeat.RepeatedValuesCompressor;
import naga.platform.json.Json;
import naga.platform.json.codec.AbstractJsonCodec;
import naga.platform.json.spi.JsonArray;
import naga.platform.json.spi.JsonObject;
import naga.platform.json.spi.WritableJsonArray;
import naga.platform.json.spi.WritableJsonObject;

import java.util.Arrays;

/**
 * @author Bruno Salmon
 */
public class QueryResultSet {

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
     * Column names of the result set. This information is actually optional and useful only for debugging or when
     * using methods to access values with column names instead of column index (like in the DomainModelLoader)
     */
    private final String[] columnNames;


    public QueryResultSet(int rowCount, int columnCount, Object[] values, String[] columnNames) {
        if (values.length != columnCount * rowCount || columnNames != null && columnNames.length != columnCount)
            throw new IllegalArgumentException("Incoherent sizes in QueryResultSet initialization");
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.columnNames = columnNames;
        this.values = values;
    }

    public QueryResultSet(int columnCount, Object[] values) {
        this(columnCount, values, null);
    }

    public QueryResultSet(int columnCount, Object[] values, String[] columnNames) {
        this(values.length / columnCount, columnCount, values, columnNames);
    }

    public QueryResultSet(Object[] values, String[] columnNames) {
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
        return getInt(rowIndex, getColumnIndex(columnName), defaultValue);
    }

    public int getInt(int rowIndex, int columnIndex, int defaultValue) {
        Object value = getValue(rowIndex, columnIndex);
        return Numbers.isNumber(value) ? Numbers.intValue(value) : defaultValue;
    }

    // To be used with Codenameone because generic one raise an error when value is a string
    public boolean getBoolean(int rowIndex, String columnName, boolean defaultValue) {
        return getBoolean(rowIndex, getColumnIndex(columnName), defaultValue);
    }

    public boolean getBoolean(int rowIndex, int columnIndex, boolean defaultValue) {
        Object value = getValue(rowIndex, columnIndex);
        return value == null ? defaultValue : value instanceof Boolean ? (Boolean) value : Boolean.valueOf(value.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryResultSet that = (QueryResultSet) o;

        if (rowCount != that.rowCount) return false;
        if (columnCount != that.columnCount) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(values, that.values)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(columnNames, that.columnNames);
    }

    @Override
    public int hashCode() {
        int result = rowCount;
        result = 31 * result + columnCount;
        result = 31 * result + Arrays.hashCode(values);
        result = 31 * result + Arrays.hashCode(columnNames);
        return result;
    }

    /****************************************************
     *                    Json Codec                    *
     * *************************************************/

    public final static String CODEC_ID = "QueryResultSet";
    private final static String COLUMN_NAMES_KEY = "names";
    private final static String COLUMN_COUNT_KEY = "colCount";
    private final static String VALUES_KEY = "values";
    private final static String COMPRESSED_VALUES_KEY = "cvalues";

    public static boolean COMPRESSION = true;

    public static void registerJsonCodec() {
        new AbstractJsonCodec<QueryResultSet>(QueryResultSet.class, CODEC_ID) {

            @Override
            public void encodeToJson(QueryResultSet result, WritableJsonObject json) {
                try {
                    int columnCount = result.getColumnCount();
                    // Column names serialization
                    WritableJsonArray namesArray = json.createJsonArray();
                    String[] columnNames = result.getColumnNames();
                    if (columnNames != null) {
                        for (String name : columnNames)
                            namesArray.push(name);
                        json.set(COLUMN_NAMES_KEY, namesArray);
                        columnCount = namesArray.size();
                    }
                    json.set(COLUMN_COUNT_KEY, columnCount);
                    // values packing and serialization
                    if (COMPRESSION)
                        json.set(COMPRESSED_VALUES_KEY, Json.fromJavaArray(RepeatedValuesCompressor.SINGLETON.compress(result.values)));
                    else
                        json.set(VALUES_KEY, Json.fromJavaArray(result.values));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public QueryResultSet decodeFromJson(JsonObject json) {
                //Platform.log("Decoding json result set: " + json);
                Integer columnCount = json.getInteger(COLUMN_COUNT_KEY);
                // Column names deserialization
                String[] names = null;
                JsonArray namesArray = json.getArray(COLUMN_NAMES_KEY);
                if (namesArray != null) {
                    columnCount = namesArray.size();
                    names = new String[columnCount];
                    for (int i = 0; i < columnCount; i++)
                        names[i] = namesArray.getString(i);
                }
                // Values deserialization
                Object[] inlineValues;
                JsonArray valuesArray = json.getArray(VALUES_KEY);
                if (valuesArray != null)
                    inlineValues = Json.toJavaArray(valuesArray);
                else
                    inlineValues = RepeatedValuesCompressor.SINGLETON.uncompress(Json.toJavaArray(json.getArray(COMPRESSED_VALUES_KEY)));
                // returning the result as a snapshot
                return new QueryResultSet(columnCount, inlineValues, names);
            }
        };
    }
}
