package naga.core.spi.sql;

import naga.core.jsoncodec.AbstractJsonCodec;
import naga.core.spi.json.Json;
import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.JsonObject;
import naga.core.type.PrimType;

import java.util.*;

/**
 * @author Bruno Salmon
 */
public class CompressedSqlReadResult {

    private final String[] columnNames;
    private final Object[][] values;

    public CompressedSqlReadResult(String[] columnNames, Object[][] values) {
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

    private final static String CODEC_ID = "queryRes";
    private final static String COLUMN_NAMES_KEY = "names";
    private final static String COLUMN_TYPES_KEY = "types";
    private final static String VALUES_KEY = "values";
    private final static String REPEATS_KEY = "repeats";

    private final static boolean COMPRESSION = false;

    private static boolean isARepeatValue(Object value) {
        return value == null || value.equals(Boolean.FALSE) || value.equals(Boolean.TRUE) || value.equals(0) || value.equals(1) || value.equals(0L) || value.equals(1L);
    }

    public static void registerJsonCodec() {
        new AbstractJsonCodec<CompressedSqlReadResult>(CompressedSqlReadResult.class, CODEC_ID) {

            @Override
            public void encodeToJson(CompressedSqlReadResult result, JsonObject json) {
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
                    RepeatValuesCompressor repeatValues = COMPRESSION ? new RepeatValuesCompressor() : null;
                    int rowCount = result.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        Object[] row = values[i];
                        JsonArray rowArray = Json.createArray();
                        for (int j = 0; j < columnCount; j++) {
                            Object value = row[j];
                            if (COMPRESSION && isARepeatValue(value))
                                repeatValues.addRepeatValue(value, i, j);
                            else {
                                if (value != null) {
                                    PrimType type = types[j];
                                    if (type == null)
                                        types[j] = type = PrimType.fromObject(value);
                                    if (type == PrimType.DATE)
                                        value = ((Date) value).getTime();
                                }
                                rowArray.push(value);
                            }
                        }
                        rowsArray.push(rowArray);
                    }
                    for (PrimType type : types)
                        typesArray.push(type == null ? null : type.name());
                    json.set(COLUMN_TYPES_KEY, typesArray);
                    json.set(VALUES_KEY, rowsArray);
                    if (COMPRESSION && !repeatValues.isEmpty())
                        json.set(REPEATS_KEY, repeatValues.toRepeatsArray());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public CompressedSqlReadResult decodeFromJson(JsonObject json) {
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
                CompressedRepeatValues repeatValues = new CompressedRepeatValues(json.get(REPEATS_KEY));
                for (int i = 0; i < rowCount; i++) {
                    JsonArray rowArray = rowsArray.getArray(i);
                    int nextNonNullColumnIndex = 0;
                    for (int j = 0; j < columnCount; j++) {
                        Object value;
                        if (repeatValues.isRepeatValue(i, j))
                            value = repeatValues.getValue();
                        else
                            value = rowArray.get(nextNonNullColumnIndex++);
                        if (value != null && types[j] == PrimType.DATE)
                            value = new Date(((Number) value).longValue());
                        values[i][j] = value;
                    }
                }
                // returning the result as a snapshot
                return new CompressedSqlReadResult(names, values);
            }
        };
    }

    static class RepeatValuesCompressor {
        final Map<Object, RepeatValueCompressor> repeats = new HashMap<>();

        void addRepeatValue(Object value, int row, int column) {
            RepeatValueCompressor repeat = repeats.get(value);
            if (repeat == null)
                repeats.put(value, repeat = new RepeatValueCompressor(value));
            repeat.addCell(row, column);
        }

        boolean isEmpty() {
            return repeats.isEmpty();
        }

        JsonArray toRepeatsArray() {
            JsonArray repeatsArray = Json.createArray();
            for (RepeatValueCompressor repeat : repeats.values())
                repeat.appendToRepeatsArray(repeatsArray);
            return repeatsArray;
        }
    }

    static class RepeatValueCompressor {
        final Object value; // the value which is repeated
        final IntArrayCompressor rowsArray = new IntArrayCompressor(); // compressed array of rows where the value is present
        int lastRow = -1;
        IntArrayCompressor columnsArray;
        List<String> compressedColumnsArrays = new ArrayList<>();
        Map<String, IntArrayCompressor> repeatedCompressedColumnsArrays = new HashMap<>();

        private static boolean COLUMN_INDEX_COMPRESSION = true;

        public RepeatValueCompressor(Object value) {
            this.value = value;
        }

        void addCell(int row, int column) {
            if (row != lastRow) {
                flushLastRow();
                rowsArray.pushInt(lastRow = row);
            }
            if (columnsArray == null)
                columnsArray = new IntArrayCompressor();
            columnsArray.pushInt(column);
        }

        private void flushLastRow() {
            if (columnsArray != null) {
                String compressed = columnsArray.compress();
                if (COLUMN_INDEX_COMPRESSION && compressedColumnsArrays.contains(compressed)) {
                    IntArrayCompressor intArrayCompressor = repeatedCompressedColumnsArrays.get(compressed);
                    if (intArrayCompressor == null) {
                        repeatedCompressedColumnsArrays.put(compressed, intArrayCompressor = new IntArrayCompressor());
                        intArrayCompressor.pushInt(compressedColumnsArrays.indexOf(compressed));
                    }
                    intArrayCompressor.pushInt(compressedColumnsArrays.size());
                }
                compressedColumnsArrays.add(compressed);
                columnsArray = null;
            }
        }

        void appendToRepeatsArray(JsonArray repeatsArray) {
            flushLastRow();
            JsonArray unique_allColumnsArray = Json.createArray();
            JsonArray repeat_allColumnsArray = repeatedCompressedColumnsArrays.isEmpty() ? null : Json.createArray();
            for (String compressed : compressedColumnsArrays) {
                IntArrayCompressor intArrayCompressor = repeatedCompressedColumnsArrays.get(compressed);
                if (intArrayCompressor == null)
                    unique_allColumnsArray.push(compressed);
                else if (!intArrayCompressor.done()) {
                    repeat_allColumnsArray.push(compressed);
                    repeat_allColumnsArray.push(intArrayCompressor.compress());
                }
            }
            // Pushing data in the repeatsArray:
            // 1) the value to repeat (ex: null, true, false, 0, 1, ...)
            repeatsArray.push(value);
            // 2) the rows where the value is present represented as a compressed int string (ex: "1-3,5-6")
            String compressedRows = rowsArray.compress();
            if (repeat_allColumnsArray != null)
                compressedRows = 'C' + compressedRows; // C is a mark for column compression
            repeatsArray.push(compressedRows);
            // 3) the columns where the values is present represented as an array of compressed int string (ex ["1,4","1-4","3","5-6","5-6"] columns for respectively rows 1, 2, 3, 5 and 6)
            repeatsArray.push(unique_allColumnsArray);
            // 4) optionally repeated columns data when compression is enabled (ex: ["1-4","0-1","5-6","3-4"] in the example above and in this case allColumnsArray is reduced to ["3"]
            if (repeat_allColumnsArray != null)
                repeatsArray.push(repeat_allColumnsArray);
        }
    }

    static class IntArrayCompressor {
        StringBuffer sb;
        int lastSeqStart;
        int lastInt;
        String compressed;

        void pushInt(int nextInt) {
            if (sb == null)
                sb = new StringBuffer().append(lastSeqStart = lastInt = nextInt);
            else if (nextInt == lastInt + 1)
                lastInt = nextInt;
            else {
                if (nextInt <= lastInt)
                    throw new IllegalArgumentException("Integer must be pushed in the ascending order");
                if (lastInt > lastSeqStart)
                    sb.append('-').append(lastInt);
                sb.append(',').append(lastSeqStart = lastInt = nextInt);
            }
        }

        String compress() {
            if (compressed == null && sb != null) {
                if (lastInt > lastSeqStart)
                    sb.append('-').append(lastInt);
                compressed = sb.toString();
                sb = null;
            }
            return compressed;
        }

        boolean done() {
            return compressed != null;
        }
    }

    static class CompressedRepeatValues {
        final List<CompressedRepeatValue> repeats;
        CompressedRepeatValue fetchedRepeatValue;

        public CompressedRepeatValues(JsonArray repeatsArray, JsonFactory jsonFactory) {
            if (repeatsArray == null)
                repeats = null;
            else {
                repeats = new ArrayList<>();
                for (int i = 0; i < repeatsArray.length();) {
                    Object value = repeatsArray.get(i++);
                    String compressedRows = repeatsArray.getString(i++);
                    boolean compression = compressedRows.startsWith("C");
                    if (compression)
                        compressedRows = compressedRows.substring(1);
                    JsonArray unique_allColumnsArray = repeatsArray.getArray(i++);
                    JsonArray repeated_allColumnsArray = compression ? repeatsArray.getArray(i++) : null;
                    repeats.add(new CompressedRepeatValue(value, compressedRows, unique_allColumnsArray, repeated_allColumnsArray, jsonFactory));
                }
            }
        }

        public CompressedRepeatValues(List<CompressedRepeatValue> repeats) {
            this.repeats = repeats;
        }

        boolean isRepeatValue(int row, int column) {
            if (repeats != null)
                for (CompressedRepeatValue repeat : repeats) {
                    if (repeat.isRepeatValue(row, column)) {
                        fetchedRepeatValue = repeat;
                        return true;
                    }
                }
            return false;
        }

        Object getValue() {
            return fetchedRepeatValue.value;
        }
    }

    static class CompressedRepeatValue {
        final Object value;
        final CompressedIntArray rowsArray;
        final JsonArray allColumnsArray;
        CompressedIntArray columnsArray;
        int rowsIndex;
        int nextValueRow = 0;
        int nextValueColumn = -1;

        public CompressedRepeatValue(Object value, String compressedRows, JsonArray unique_allColumnsArray, JsonArray repeated_allColumnsArray, JsonFactory jsonFactory) {
            this.value = value;
            rowsArray = new CompressedIntArray(compressedRows);
            if (repeated_allColumnsArray == null)
                this.allColumnsArray = unique_allColumnsArray;
            else {
                this.allColumnsArray = Json.createArray();
                List<CompressedRepeatValue> repeatValues = new ArrayList<>();
                for (int i = 0; i < repeated_allColumnsArray.length();) {
                    String repeatValue = repeated_allColumnsArray.getString(i++);
                    JsonArray singleRow = Json.createArray();
                    String repeatIndexes = repeated_allColumnsArray.getString(i++);
                    singleRow.push(repeatIndexes);
                    repeatValues.add(new CompressedRepeatValue(repeatValue, "0", singleRow, null, jsonFactory));
                }
                CompressedRepeatValues compressedColumns = new CompressedRepeatValues(repeatValues);
                int uniqueValueIndex = 0;
                for (int i = 0; ; i++) {
                    Object compressed;
                    if (compressedColumns.isRepeatValue(0, i))
                        compressed = compressedColumns.getValue();
                    else if (uniqueValueIndex < unique_allColumnsArray.length())
                        compressed = unique_allColumnsArray.get(uniqueValueIndex++);
                    else
                        break;
                    this.allColumnsArray.push(compressed);
                }
            }
            nextValueRow = rowsArray.nextInt();
        }

        boolean isRepeatValue(int row, int column) {
            if (nextValueRow != row)
                return false;
            if (columnsArray == null) {
                if (rowsIndex < allColumnsArray.length()) {
                    columnsArray = new CompressedIntArray(allColumnsArray.getString(rowsIndex++));
                    nextValueColumn = columnsArray.nextInt();
                } else
                    nextValueColumn = -1;
            }
            if (column != nextValueColumn)
                return false;
            nextValueColumn = columnsArray.nextInt();
            if (nextValueColumn == -1) {
                nextValueRow = rowsArray.nextInt();
                columnsArray = null;
            }
            return true;
        }
    }

    static class CompressedIntArray {
        final String compressed;
        int pos = 0;
        int lastSeqStart;
        int lastInt;
        int lastSeqEnd;

        public CompressedIntArray(String compressed) {
            this.compressed = compressed;
        }

        boolean hasNext() {
            return compressed != null && (pos != -1 || lastInt <= lastSeqEnd);
        }

        int nextInt() {
            if (!hasNext())
                return -1;
            if (pos == 0 || lastInt > lastSeqEnd) {
                int commaPos = compressed.indexOf(',', pos);
                String commaToken = commaPos != -1 ? compressed.substring(pos, commaPos) : compressed.substring(pos);
                pos = commaPos != -1 ? commaPos + 1 : -1;
                int hyphenPos = commaToken.indexOf('-');
                if (hyphenPos == -1)
                    lastSeqStart = lastInt = lastSeqEnd = Integer.parseInt(commaToken);
                else {
                    lastSeqStart = lastInt = Integer.parseInt(commaToken.substring(0, hyphenPos));
                    lastSeqEnd = Integer.parseInt(commaToken.substring(hyphenPos + 1));
                }
            }
            return lastInt++;
        }
    }


}
