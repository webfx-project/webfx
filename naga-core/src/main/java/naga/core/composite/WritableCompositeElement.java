package naga.core.composite;

/**
 * @author Bruno Salmon
 */
public interface WritableCompositeElement extends CompositeElement {

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
