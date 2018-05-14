package naga.platform.services.query.push.diff.impl;

import naga.platform.json.codec.AbstractJsonCodec;
import naga.platform.json.codec.JsonCodecManager;
import naga.platform.json.spi.JsonObject;
import naga.platform.json.spi.WritableJsonObject;
import naga.platform.services.query.QueryResult;
import naga.platform.services.query.QueryResultBuilder;
import naga.platform.services.query.push.diff.QueryResultComparator;
import naga.platform.services.query.push.diff.QueryResultDiff;

/**
 * @author Bruno Salmon
 */
public class QueryResultTranslation implements QueryResultDiff {

    private final QueryResult rowsBefore;
    private final int rowStart;
    private final int rowEnd;
    private final QueryResult rowsAfter;

    public QueryResultTranslation(QueryResult rowsBefore, int rowStart, int rowEnd, QueryResult rowsAfter) {
        this.rowsBefore = rowsBefore;
        this.rowStart = rowStart;
        this.rowEnd = rowEnd;
        this.rowsAfter = rowsAfter;
    }

    public QueryResult getRowsBefore() {
        return rowsBefore;
    }

    public int getRowStart() {
        return rowStart;
    }

    public int getRowEnd() {
        return rowEnd;
    }

    public QueryResult getRowsAfter() {
        return rowsAfter;
    }

    @Override
    public QueryResult applyTo(QueryResult queryResult) {
        int beforeCount = rowsBefore == null ? 0 : rowsBefore.getRowCount();
        int translationCount = rowEnd - rowStart + 1;
        int afterCount = rowsAfter == null ? 0 : rowsAfter.getRowCount();
        int rowCount = beforeCount + translationCount + afterCount;
        int columnCount = queryResult.getColumnCount();
        QueryResultBuilder rsb = QueryResultBuilder.create(rowCount, columnCount);
        QueryResultComparator.copyRows(rowsBefore, 0, beforeCount - 1, rsb, 0);
        QueryResultComparator.copyRows(queryResult, rowStart, rowEnd, rsb, beforeCount);
        QueryResultComparator.copyRows(rowsAfter, 0, afterCount - 1, rsb, beforeCount + translationCount);
        return rsb.build();
    }

    /****************************************************
     *                    Json Codec                    *
     * *************************************************/

    public static final String CODEC_ID = "QueryResultTranslation";
    private static final String ROWS_BEFORE_KEY = "rowsBefore";
    private static final String ROW_START_KEY = "rowStart";
    private static final String ROW_END_KEY = "rowEnd";
    private static final String ROWS_AFTER_KEY = "rowsAfter";

    public static void registerJsonCodec() {
        new AbstractJsonCodec<QueryResultTranslation>(QueryResultTranslation.class, CODEC_ID) {

            @Override
            public void encodeToJson(QueryResultTranslation arg, WritableJsonObject json) {
                encodeKeyIfNotNull(ROWS_BEFORE_KEY, arg.getRowsBefore(), json);
                encodeKey(ROW_START_KEY, arg.getRowStart(), json);
                encodeKey(ROW_END_KEY, arg.getRowEnd(), json);
                encodeKeyIfNotNull(ROWS_AFTER_KEY, arg.getRowsAfter(), json);
            }

            @Override
            public QueryResultTranslation decodeFromJson(JsonObject json) {
                return new QueryResultTranslation(
                        JsonCodecManager.decodeFromJson(json.get(ROWS_BEFORE_KEY)),
                        json.getInteger(ROW_START_KEY),
                        json.getInteger(ROW_END_KEY),
                        JsonCodecManager.decodeFromJson(json.get(ROWS_AFTER_KEY))
                );
            }
        };
    }


}
