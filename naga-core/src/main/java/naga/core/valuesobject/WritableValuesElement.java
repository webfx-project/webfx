package naga.core.valuesobject;

/**
 * @author Bruno Salmon
 */
public interface WritableValuesElement extends ValuesElement {

    /**
     * Removes all entries.
     */
    void clear();

    /**
     * Removes the first instance of the given value from the list.
     *
     * @return Whether the item was removed.
     */
    //boolean removeValue(Object value);

}
