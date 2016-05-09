package naga.core.composite;

/**
 * @author Bruno Salmon
 */
public interface CompositesFactory extends CompositesParser {

    /**
     * Create an empty native object.
     * @return a new empty native object
     */
    Object createNativeObject();

    /**
     * Create an empty native array.
     * @return a new empty native array
     */
    Object createNativeArray();

    /**
     * Create an empty composite object.
     * @return a new empty composite object
     */
    default WritableCompositeObject createCompositeObject() {
        return nativeToCompositeObject(createNativeObject());
    }

    /**
     * Create an empty composite array.
     * @return a new empty composite array
     */
    default WritableCompositeArray createCompositeArray() {
        return nativeToCompositeArray(createNativeArray());
    }

}
