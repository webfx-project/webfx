package dev.webfx.platform.shared.services.query.compression.repeat;

/**
 * @author Bruno Salmon
 */
final class RepeatedValue {
    private final Object value;
    private final SortedIntegersTokenReader indexes;
    private int nextIndex;

    public RepeatedValue(Object value, String compressedRows) {
        this.value = value;
        indexes = new SortedIntegersTokenReader(compressedRows);
        nextIndex = indexes.nextInt();
    }

    public Object getValue() {
        return value;
    }

    boolean isRepeatedAtIndex(int index) {
        if (nextIndex != index)
            return false;
        nextIndex = indexes.nextInt();
        return true;
    }
}
