package naga.core.spi.sql;

import naga.core.jsoncodec.AbstractJsonCodec;
import naga.core.spi.json.Json;
import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonObject;
import naga.core.type.PrimType;
import naga.core.util.pack.repeat.RepeatingValuesPacker;

/**
 * @author Bruno Salmon
 */
public class SqlReadResult {

    /**
     * Sql column names of the result set. This information is actually optional and useful only for debugging.
     */
    private final String[] columnNames;

    // 2 supported data structures to store values in memory:

    /**
     * 1) Values stored in a matrix (more natural for humans and some APIs).
     * First index = rowIndex, second index = columnIndex
     */
    private Object[][] matrixValues;
    /**
     * 2) Values stored in an inline array (more efficient for compression algorithm).
     * First column, then 2nd column, etc... So inlineIndex = rowIndex + columnIndex * rowCount.
     * (better than 1st row, 2nd row, etc.. for compression algorithm)
     */
    private Object[] inlineValues;
    private final int rowCount;
    private final int columnCount;

    /**
     * Constructor accepting a matrix for values.
     *
     * @param columnNames
     * @param matrixValues
     */
    public SqlReadResult(String[] columnNames, Object[][] matrixValues) {
        this.columnNames = columnNames;
        this.matrixValues = matrixValues;
        columnCount = columnNames.length;
        rowCount = matrixValues.length;
    }

    /**
     * Constructor accepting an inline array for values.
     *
     * @param columnNames
     * @param inlineValues
     */
    public SqlReadResult(String[] columnNames, Object[] inlineValues) {
        this.columnNames = columnNames;
        this.inlineValues = inlineValues;
        columnCount = columnNames.length;
        rowCount = inlineValues.length / columnCount;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public <T> T getValue(int rowIndex, int columnIndex) {
        return (T) (inlineValues != null ? inlineValues[rowIndex + columnIndex * rowCount] : matrixValues[rowIndex][columnIndex]);
    }

    private Object[] getInlineValues() {
        if (inlineValues == null) { // When asked, turning from matrix to inline
            inlineValues = new Object[rowCount * columnCount];
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
                    inlineValues[rowIndex + columnIndex * rowCount] = matrixValues[rowIndex][columnIndex];
            matrixValues = null; // releasing memory
        }
        return inlineValues;
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
                    json.set(VALUES_KEY, Json.fromJavaArray(RepeatingValuesPacker.SINGLETON.packValues(result.getInlineValues())));
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
                Object[] inlineValues = RepeatingValuesPacker.SINGLETON.unpackValues(Json.toJavaArray(valuesArray));
                // returning the result as a snapshot
                return new SqlReadResult(names, inlineValues);
            }
        };
    }
}
