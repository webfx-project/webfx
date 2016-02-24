package naga.core.util.compression.values.repeat;

/**
 * @author Bruno Salmon
 */
class RepeatingValue {
    private final Object value;
    private final SortedIntegersTokenReader indexes;
    private int nextIndex;

    public RepeatingValue(Object value, String compressedRows) {
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
