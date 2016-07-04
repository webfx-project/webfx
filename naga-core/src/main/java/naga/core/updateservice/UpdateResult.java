package naga.core.updateservice;

/**
 * @author Bruno Salmon
 */
public class UpdateResult {

    private final int rowCount;
    private final Object[] generatedKeys;

    public UpdateResult(int rowCount, Object[] generatedKeys) {
        this.rowCount = rowCount;
        this.generatedKeys = generatedKeys;
    }

    public int getRowCount() {
        return rowCount;
    }

    public Object[] getGeneratedKeys() {
        return generatedKeys;
    }
}
