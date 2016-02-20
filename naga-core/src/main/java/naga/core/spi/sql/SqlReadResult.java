package naga.core.spi.sql;

import naga.core.jsoncodec.AbstractJsonCodec;
import naga.core.spi.json.Json;
import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonObject;
import naga.core.type.PrimType;
import naga.core.util.pack.ValuesUnpacker;
import naga.core.util.pack.ValuesPacker;
import naga.core.util.pack.repeat.RepeatingValuesPacker;
import naga.core.util.pack.repeat.RepeatingValuesUnpacker;

import java.util.Date;
import java.util.Iterator;

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

    public <T> T getValue(int rowIndex, int columnIndex) {
        return (T) values[rowIndex][columnIndex];
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
                    ValuesPacker packer = new RepeatingValuesPacker();
                    // Walking by column first as this increases the chance of value consecutive repetition (good for compression)
                    for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                        PrimType type = types[columnIndex];
                        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                            Object value = result.values[rowIndex][columnIndex];
                            if (value != null) {
                                if (type == null)
                                    types[columnIndex] = type = PrimType.fromObject(value);
                                if (type == PrimType.DATE)
                                    value = ((Date) value).getTime();
                            }
                            packer.pushValue(value);
                        }
                    }
                    for (PrimType type : types)
                        typesArray.push(type == null ? null : type.name());
                    json.set(COLUMN_TYPES_KEY, typesArray);
                    // values serialization
                    JsonArray valuesArray = Json.createArray();
                    for (Iterator it = packer.packedValues(); it.hasNext(); )
                        valuesArray.push(it.next());
                    json.set(VALUES_KEY, valuesArray);
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
                ValuesUnpacker compressed = new RepeatingValuesUnpacker(valuesArray.iterator());
                Iterator uncompressedValuesIterator = compressed.unpackedValues();
                int rowCount = compressed.unpackedSize() / columnCount;
                Object[][] values = new Object[rowCount][columnCount];
                // Walking by column first (same as encoder)
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                        Object value = uncompressedValuesIterator.next();
                        if (value != null && types[columnIndex] == PrimType.DATE)
                            value = new Date(((Number) value).longValue());
                        values[rowIndex][columnIndex] = value;
                    }
                }
                // returning the result as a snapshot
                return new SqlReadResult(names, values);
            }
        };
    }
}
