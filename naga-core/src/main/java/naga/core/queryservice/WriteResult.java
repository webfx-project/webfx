package naga.core.queryservice;

/**
 * @author Bruno Salmon
 */
public class WriteResult {

    private final int rowCount;

    public WriteResult(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getRowCount() {
        return rowCount;
    }
}
