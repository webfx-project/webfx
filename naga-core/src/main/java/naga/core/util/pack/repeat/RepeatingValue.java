package naga.core.util.pack.repeat;

/**
 * @author Bruno Salmon
 */
class RepeatingValue {
    private final Object value;
    private final IncreasingIntegersTokenReader indexes;
    private int nextIndex;

    public RepeatingValue(Object value, String compressedRows) {
        this.value = value;
        indexes = new IncreasingIntegersTokenReader(compressedRows);
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
