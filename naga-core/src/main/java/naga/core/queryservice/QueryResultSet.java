package naga.core.queryservice;

import naga.core.json.*;
import naga.core.json.codec.AbstractJsonCodec;
import naga.core.type.PrimType;
import naga.core.util.Numbers;
import naga.core.util.compression.values.repeat.RepeatedValuesCompressor;

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
        this(values.length / columnCount, columnCount, values, null);
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
        Object value = getValue(rowIndex, getColumnIndex(columnName));
        return Numbers.isNumber(value) ? Numbers.intValue(value) : defaultValue;
    }

    // To be used with Codenameone because generic one raise an error when value is a string
    public boolean getBoolean(int rowIndex, String columnName, boolean defaultValue) {
        Object value = getValue(rowIndex, getColumnIndex(columnName));
        return value == null ? defaultValue : value instanceof Boolean ? (Boolean) value : Boolean.valueOf(value.toString());
    }

    /****************************************************
     *                    Json Codec                    *
     * *************************************************/

    private final static String CODEC_ID = "QueryResultSet";
    private final static String COLUMN_NAMES_KEY = "names";
    private final static String COLUMN_TYPES_KEY = "types";
    private final static String VALUES_KEY = "values";
    private final static String COMPRESSED_VALUES_KEY = "cvalues";

    private final static boolean COMPRESSION = true;

    public static void registerJsonCodec() {
        new AbstractJsonCodec<QueryResultSet>(QueryResultSet.class, CODEC_ID) {

            @Override
            public void encodeToJson(QueryResultSet result, WritableJsonObject json) {
                try {
                    int columnCount = result.getColumnCount();
                    // Column names serialization
                    WritableJsonArray namesArray = json.createJsonArray();
                    for (String name : result.getColumnNames())
                        namesArray.push(name);
                    json.set(COLUMN_NAMES_KEY, namesArray);
                    if (columnCount == -1)
                        columnCount = namesArray.size();
                    // types serialization & values compression
                    WritableJsonArray typesArray = json.createJsonArray();
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
                    if (COMPRESSION)
                        json.set(COMPRESSED_VALUES_KEY, Json.fromJavaArray(RepeatedValuesCompressor.SINGLETON.compress(result.values)));
                    else
                        json.set(VALUES_KEY, result.values);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public QueryResultSet decodeFromJson(JsonObject json) {
                // Column names deserialization
                JsonArray namesArray = json.getArray(COLUMN_NAMES_KEY);
                int columnCount = namesArray.size();
                String[] names = new String[columnCount];
                for (int i = 0; i < columnCount; i++)
                    names[i] = namesArray.getString(i);
                // Types deserialization
                JsonArray typesArray = json.getArray(COLUMN_TYPES_KEY);
                PrimType[] types = new PrimType[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    String typeName = typesArray.getString(i);
                    if (typeName != null)
                        types[i] = PrimType.valueOf(typeName);
                }
                // Values deserialization
                Object[] inlineValues;
                JsonArray valuesArray = json.getArray(VALUES_KEY);
                if (valuesArray != null)
                    inlineValues = Json.toJavaArray(valuesArray);
                else
                    inlineValues = RepeatedValuesCompressor.SINGLETON.uncompress(Json.toJavaArray(json.getArray(COMPRESSED_VALUES_KEY)));
                // returning the result as a snapshot
                return new QueryResultSet(inlineValues, names);
            }
        };
    }
}
