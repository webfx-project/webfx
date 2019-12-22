package webfx.framework.shared.services.querypush.diff;

import webfx.framework.shared.services.querypush.diff.impl.QueryResultTranslation;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.services.query.QueryResultBuilder;

import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public final class QueryResultComparator {

    public static QueryResultDiff computeDiff(QueryResult rs1, QueryResult rs2) {
        if (rs1 != null && rs2 != null && rs1 != rs2) {
            int columnCount = rs1.getColumnCount();
            if (columnCount == rs2.getColumnCount()) {
                int rowCount1 = rs1.getRowCount();
                int rowCount2 = rs2.getRowCount();
                int rowCount = Math.min(rowCount1, rowCount2);
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                    if (compareRow(columnCount, rs1, 0, rs2, rowIndex)) {
                        int rowStart = 0;
                        int rowEnd = 0;
                        while (rowIndex + rowEnd + 1 < rowCount && compareRow(columnCount, rs1, rowEnd + 1, rs2, rowIndex + rowEnd + 1))
                            rowEnd++;
                        return new QueryResultTranslation(
                                copyRows(rs2, 0, rowIndex - 1),
                                rowStart, rowEnd,
                                copyRows(rs2, rowIndex + rowEnd + 1, rowCount2 - 1),
                                rs1.getVersionNumber(), rs2.getVersionNumber()
                        );

                    } else if (rowIndex != 0 && compareRow(columnCount, rs1, rowIndex, rs2, 0)) {
                        int rowStart = rowIndex;
                        int rowEnd = rowStart;
                        while (rowEnd + 1 < rowCount && compareRow(columnCount, rs1, rowEnd + 1, rs2, rowEnd + 1 - rowIndex))
                            rowEnd++;
                        return new QueryResultTranslation(
                                null,
                                rowStart, rowEnd,
                                copyRows(rs2, rowEnd + 1 - rowIndex, rowCount2 - 1),
                                rs1.getVersionNumber(), rs2.getVersionNumber()
                        );
                    }
                }
            }
        }
        return null;
    }

    private static boolean compareRow(int columnCount, QueryResult rs1, int rowIndex1, QueryResult rs2, int rowIndex2) {
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
            if (!Objects.equals(rs1.getValue(rowIndex1, columnIndex), rs2.getValue(rowIndex2, columnIndex)))
                return false;
        return true;
    }

    public static QueryResult copyRows(QueryResult rs, int rowStart, int rowEnd) {
        if (rs == null || rowEnd < rowStart)
            return null;
        QueryResultBuilder rsb = QueryResultBuilder.create(rowEnd - rowStart + 1, rs.getColumnCount());
        copyRows(rs, rowStart, rowEnd, rsb, 0);
        return rsb.build();
    }

    public static void copyRows(QueryResult rs, int rowStart, int rowEnd, QueryResultBuilder rsb, int dstRowStart) {
        if (rs != null) {
            int columnCount = rs.getColumnCount();
            for (int rowIndex = rowStart, dstRowIndex = dstRowStart; rowIndex <= rowEnd; rowIndex++, dstRowIndex++) {
                for (int colIndex = 0; colIndex < columnCount; colIndex++)
                    rsb.setValue(dstRowIndex, colIndex, rs.getValue(rowIndex, colIndex));
            }
        }
    }
}
