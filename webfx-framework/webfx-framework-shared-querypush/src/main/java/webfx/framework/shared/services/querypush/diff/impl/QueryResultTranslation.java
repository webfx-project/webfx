package webfx.framework.shared.services.querypush.diff.impl;

import webfx.framework.shared.services.querypush.diff.QueryResultDiff;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.services.query.QueryResultBuilder;
import webfx.platform.shared.services.serial.SerialCodecManager;
import webfx.platform.shared.services.serial.spi.impl.SerialCodecBase;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.json.WritableJsonObject;
import webfx.framework.shared.services.querypush.diff.QueryResultComparator;

/**
 * @author Bruno Salmon
 */
public final class QueryResultTranslation implements QueryResultDiff {

    private final QueryResult rowsBefore;
    private final int rowStart;
    private final int rowEnd;
    private final QueryResult rowsAfter;
    private final int previousQueryResultVersionNumber;
    private final int finalQueryResultVersionNumber;

    public QueryResultTranslation(QueryResult rowsBefore, int rowStart, int rowEnd, QueryResult rowsAfter, int previousQueryResultVersionNumber, int finalQueryResultVersionNumber) {
        this.rowsBefore = rowsBefore;
        this.rowStart = rowStart;
        this.rowEnd = rowEnd;
        this.rowsAfter = rowsAfter;
        this.previousQueryResultVersionNumber = previousQueryResultVersionNumber;
        this.finalQueryResultVersionNumber = finalQueryResultVersionNumber;
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
    public int getPreviousQueryResultVersionNumber() {
        return previousQueryResultVersionNumber;
    }

    @Override
    public int getFinalQueryResultVersionNumber() {
        return finalQueryResultVersionNumber;
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
        QueryResult rs = rsb.build();
        rs.setVersionNumber(finalQueryResultVersionNumber);
        return rs;
    }

    /****************************************************
     *                   Serial ProvidedSerialCodec                   *
     * *************************************************/

    public static final class ProvidedSerialCodec extends SerialCodecBase<QueryResultTranslation> {

        private static final String CODEC_ID = "QueryResultTranslation";
        private static final String ROWS_BEFORE_KEY = "rowsBefore";
        private static final String ROW_START_KEY = "rowStart";
        private static final String ROW_END_KEY = "rowEnd";
        private static final String ROWS_AFTER_KEY = "rowsAfter";
        private static final String PREVIOUS_VERSION_KEY = "previousVersion";
        private static final String FINAL_VERSION_KEY = "finalVersion";

        public ProvidedSerialCodec() {
            super(QueryResultTranslation.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(QueryResultTranslation arg, WritableJsonObject json) {
            SerialCodecBase.encodeKeyIfNotNull(ROWS_BEFORE_KEY, arg.getRowsBefore(), json);
            SerialCodecBase.encodeKey(ROW_START_KEY, arg.getRowStart(), json);
            SerialCodecBase.encodeKey(ROW_END_KEY, arg.getRowEnd(), json);
            SerialCodecBase.encodeKeyIfNotNull(ROWS_AFTER_KEY, arg.getRowsAfter(), json);
            SerialCodecBase.encodeKey(PREVIOUS_VERSION_KEY, arg.getPreviousQueryResultVersionNumber(), json);
            SerialCodecBase.encodeKey(FINAL_VERSION_KEY, arg.getFinalQueryResultVersionNumber(), json);
        }

        @Override
        public QueryResultTranslation decodeFromJson(JsonObject json) {
            return new QueryResultTranslation(
                    SerialCodecManager.decodeFromJson(json.get(ROWS_BEFORE_KEY)),
                    json.getInteger(ROW_START_KEY),
                    json.getInteger(ROW_END_KEY),
                    SerialCodecManager.decodeFromJson(json.get(ROWS_AFTER_KEY)),
                    json.getInteger(PREVIOUS_VERSION_KEY),
                    json.getInteger(FINAL_VERSION_KEY)
            );
        }
    }
}
