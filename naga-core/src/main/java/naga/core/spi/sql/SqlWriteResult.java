package naga.core.spi.sql;

/**
 * @author Bruno Salmon
 */
public class SqlWriteResult {

    private final int rowCount;

    public SqlWriteResult(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getRowCount() {
        return rowCount;
    }
}
