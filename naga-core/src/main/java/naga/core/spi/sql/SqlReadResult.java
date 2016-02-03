package naga.core.spi.sql;

import naga.core.jsoncodec.AbstractJsonCodec;
import naga.core.spi.json.Json;
import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonObject;
import naga.core.type.PrimType;

import java.util.Date;

/**
 * @author Bruno Salmon
 */
public class SqlReadResult {

    private final String[] columnNames;
    private final Object[][] values; // first index = row, second index = column

    public SqlReadResult(String[] columnNames, Object[][] values) {
        this.columnNames = columnNames;
        this.values = values;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return values.length;
    }

    public Object getValue(int rowIndex, int columnIndex) {
        return values[rowIndex][columnIndex];
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
                    // Values & types serialization
                    Object[][] values = result.values;
                    JsonArray rowsArray = Json.createArray();
                    JsonArray typesArray = Json.createArray();
                    PrimType[] types = new PrimType[columnCount];
                    int rowCount = result.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        Object[] row = values[i];
                        JsonArray rowArray = Json.createArray();
                        for (int j = 0; j < columnCount; j++) {
                            Object value = row[j];
                            if (value != null) {
                                PrimType type = types[j];
                                if (type == null)
                                    types[j] = type = PrimType.fromObject(value);
                                if (type == PrimType.DATE)
                                    value = ((Date) value).getTime();
                            }
                            rowArray.push(value);
                        }
                        rowsArray.push(rowArray);
                    }
                    for (PrimType type : types)
                        typesArray.push(type == null ? null : type.name());
                    json.set(COLUMN_TYPES_KEY, typesArray);
                    json.set(VALUES_KEY, rowsArray);
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
                JsonArray rowsArray = json.get(VALUES_KEY);
                int rowCount = rowsArray.length();
                Object[][] values = rowCount > 0 ? new Object[rowCount][columnCount] : null;
                for (int i = 0; i < rowCount; i++) {
                    JsonArray rowArray = rowsArray.getArray(i);
                    int nextNonNullColumnIndex = 0;
                    for (int j = 0; j < columnCount; j++) {
                        Object value = rowArray.get(nextNonNullColumnIndex++);
                        if (value != null && types[j] == PrimType.DATE)
                            value = new Date(((Number) value).longValue());
                        values[i][j] = value;
                    }
                }
                // returning the result as a snapshot
                return new SqlReadResult(names, values);
            }
        };
    }
}
